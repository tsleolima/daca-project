package com.ufcg.br.listapramim.produto;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping({"/api/produto"})
public class ProdutoController {
	
	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping
	@Cacheable("produtos")
	public ResponseEntity<List<Produto>> getProdutos(){
        simulateSlowService();
		Users user = userService.getUserCurrent();
		List<Produto> produtos = this.produtoService.getProdutos(user);
		if(produtos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/semcache")
	public ResponseEntity<List<Produto>> getProdutosSemCache(){
        simulateSlowService();
		Users user = userService.getUserCurrent();
		List<Produto> produtos = this.produtoService.getProdutos(user);
		if(produtos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	private void simulateSlowService() {
        try {
            long time = 500L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }		
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getProduto(@PathVariable ObjectId id) {
		Users user = userService.getUserCurrent();
		Produto produto = this.produtoService.getProduto(user,id);
		if(produto != null) {
			return ResponseEntity.ok().body(produto);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/ordered")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenados(){
		Users user = userService.getUserCurrent();
		ArrayList<Produto> produtos = this.produtoService.getProdutosOrdenados(user);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/ordered/by")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenadosCategoria(@RequestParam String categoria){
		Users user = userService.getUserCurrent();
		ArrayList<Produto> produtos = this.produtoService.getProdutosOrdenadosCategoria(user,categoria);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/ordered/by/preco")
	public ResponseEntity<ArrayList<ItemVenda>> getProdutosOrdenadosPreco(){
		Users user = userService.getUserCurrent();
		ArrayList<ItemVenda> itensVenda = this.produtoService.getProdutosOrdenadosPreco(user);
		if(itensVenda.size() > 0) {
			return ResponseEntity.ok().body(itensVenda);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<ArrayList<Produto>> pesquisaProdutoNome(@RequestParam String nome){
		Users user = userService.getUserCurrent();
		ArrayList<Produto> produtos = this.produtoService.pesquisaProdutoNome(user,nome);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	@CacheEvict(value="produtos", allEntries = true) 
	public ResponseEntity<Produto> cadastrarProduto(@RequestBody ProdutoDAO produtoAdd) {
		Users user = userService.getUserCurrent();
		Produto produto = this.produtoService.cadastrarProduto(user,produtoAdd);
		if(produto != null) {
			return ResponseEntity.ok().body(produto);
		}
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizarProduto (@PathVariable ObjectId id, @RequestBody Produto produtoAtt) {
		Users user = userService.getUserCurrent();
		Produto produto = this.produtoService.atualizarProduto(user,id,produtoAtt);
		if(produto != null) {
			return ResponseEntity.ok().body(produto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarProduto (@PathVariable ObjectId id) {
		Users user = userService.getUserCurrent();
		Produto produto = this.produtoService.deletarProduto(user,id);
		if(produto != null) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
