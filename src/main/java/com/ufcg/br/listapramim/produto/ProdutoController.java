package com.ufcg.br.listapramim.produto;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ufcg.br.listapramim.usuario.CustomUserDetailsService;
import com.ufcg.br.listapramim.usuario.Users;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"/api/produto"})
public class ProdutoController {
		
	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private ProdutoService produtoService;
	
	// (produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@GetMapping
	@Cacheable("produtos")
	public Flux<Produto> getProdutos(){
		Users user = userService.getUserCurrent();
		Flux<Produto> produtos = this.produtoService.getProdutos(user);
		return produtos;		
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Produto>> getProduto(@PathVariable ObjectId id) {
		Users user = userService.getUserCurrent();
		Mono<Produto> produto = this.produtoService.getProduto(user,id);
		
		return produto.map( p -> ResponseEntity.ok().body(p))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
		
	}
	
	@GetMapping("/ordered")
	public Flux<Produto> getProdutosOrdenados(){
		Users user = userService.getUserCurrent();
		Flux<Produto> produtos = this.produtoService.getProdutosOrdenados(user);
		return produtos;
	}
	
	@GetMapping("/ordered/by")
	public Flux<Produto> getProdutosOrdenadosCategoria(@RequestParam String categoria){
		Users user = userService.getUserCurrent();
		Flux<Produto> produtos = this.produtoService.getProdutosOrdenadosCategoria(user,categoria);
		return produtos;
	}
	
	@GetMapping("/ordered/by/preco")
	public Flux<ItemVenda> getProdutosOrdenadosPreco(){
		Users user = userService.getUserCurrent();
		Flux<ItemVenda> itensVenda = this.produtoService.getProdutosOrdenadosPreco(user);
		return itensVenda;
	}
	
	@GetMapping("/search")
	public Flux<ResponseEntity<Produto>> pesquisaProdutoNome(@RequestParam String nome){
		Users user = userService.getUserCurrent();
		Flux<Produto> produtos = this.produtoService.pesquisaProdutoNome(user,nome);
		return produtos.map( p -> ResponseEntity.ok().body(p))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	@PostMapping
	@CacheEvict(value="produtos", allEntries = true) 
	public Mono<ResponseEntity<Produto>> cadastrarProduto(@RequestBody ProdutoDAO produtoAdd) {
		Users user = userService.getUserCurrent();
		Mono<Produto> produto = this.produtoService.cadastrarProduto(user,produtoAdd);
		return produto.map( p -> ResponseEntity.ok().body(p))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Produto>> atualizarProduto (@PathVariable ObjectId id, @RequestBody Produto produtoAtt) {
		Users user = userService.getUserCurrent();
		Mono<Produto> produto = this.produtoService.atualizarProduto(user,id,produtoAtt);
		return produto.map( p -> ResponseEntity.ok().body(p))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> deletarProduto (@PathVariable ObjectId id) {
		Users user = userService.getUserCurrent();
		Mono<Void> response = this.produtoService.deletarProduto(user,id);
		return response.map( r -> ResponseEntity.ok().build())
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}	
}
