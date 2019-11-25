package com.ufcg.br.listapramim.produto;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
//	
//	@GetMapping("/search")
//	public ResponseEntity<ArrayList<Produto>> pesquisaProdutoNome(@RequestParam String nome){
//		Users user = userService.getUserCurrent();
//		ArrayList<Produto> produtos = this.produtoService.pesquisaProdutoNome(user,nome);
//		if(produtos.size() > 0) {
//			return ResponseEntity.ok().body(produtos);
//		} else {
//			return ResponseEntity.noContent().build();
//		}
//	}
//	
//	@PostMapping
//	@CacheEvict(value="produtos", allEntries = true) 
//	public ResponseEntity<Produto> cadastrarProduto(@RequestBody ProdutoDAO produtoAdd) {
//		Users user = userService.getUserCurrent();
//		Produto produto = this.produtoService.cadastrarProduto(user,produtoAdd);
//		if(produto != null) {
//			return ResponseEntity.ok().body(produto);
//		}
//		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
//	}
//
//	@PutMapping("/{id}")
//	public ResponseEntity<Produto> atualizarProduto (@PathVariable ObjectId id, @RequestBody Produto produtoAtt) {
//		Users user = userService.getUserCurrent();
//		Produto produto = this.produtoService.atualizarProduto(user,id,produtoAtt);
//		if(produto != null) {
//			return ResponseEntity.ok().body(produto);
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//	}
//	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<?> deletarProduto (@PathVariable ObjectId id) {
//		Users user = userService.getUserCurrent();
//		Produto produto = this.produtoService.deletarProduto(user,id);
//		if(produto != null) {
//			return ResponseEntity.ok().build();
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//	}
//	
}
