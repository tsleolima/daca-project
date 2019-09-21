package com.ufcg.br.listapramim.produto;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ufcg.br.listapramim.usuario.Users;

@RestController
@RequestMapping({"/api/produto"})
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getProdutos(HttpServletRequest request){
		Users user = (Users) request.getAttribute("user");
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
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getProduto(@PathVariable ObjectId id) {
		Produto produto = this.produtoService.getProduto(id);
		if(produto != null) {
			return ResponseEntity.ok().body(produto);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/ordered")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenados(HttpServletRequest request){
		Users user = (Users) request.getAttribute("user");
		ArrayList<Produto> produtos = this.produtoService.getProdutosOrdenados(user);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/ordered/{categoria}")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenadosCategoria(HttpServletRequest request, @PathVariable String categoria){
		Users user = (Users) request.getAttribute("user");
		ArrayList<Produto> produtos = this.produtoService.getProdutosOrdenadosCategoria(user,categoria);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/ordered/preco")
	public ResponseEntity<ArrayList<ItemVenda>> getProdutosOrdenadosPreco(HttpServletRequest request){
		Users user = (Users) request.getAttribute("user");
		ArrayList<ItemVenda> itensVenda = this.produtoService.getProdutosOrdenadosPreco(user);
		if(itensVenda.size() > 0) {
			return ResponseEntity.ok().body(itensVenda);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/search/{nome}")
	public ResponseEntity<ArrayList<Produto>> pesquisaProdutoNome(HttpServletRequest request, @PathVariable String nome){
		Users user = (Users) request.getAttribute("user");
		ArrayList<Produto> produtos = this.produtoService.pesquisaProdutoNome(user,nome);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<Produto> cadastrarProduto(HttpServletRequest request,@RequestBody ProdutoDAO produtoAdd) {
		Users user = (Users) request.getAttribute("user");
		Produto produto = this.produtoService.cadastrarProduto(user,produtoAdd);
		if(produto != null) {
			return ResponseEntity.ok().body(produto);
		}
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizarProduto (@PathVariable ObjectId id, @RequestBody Produto produtoAtt) {
		Produto produto = this.produtoService.atualizarProduto(id,produtoAtt);
		if(produto != null) {
			return ResponseEntity.ok().body(produto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarProduto (@PathVariable ObjectId id) {
		Produto produto = this.produtoService.deletarProduto(id);
		if(produto != null) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
