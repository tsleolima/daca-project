package com.ufcg.br.listapramim.resource;

import java.util.ArrayList;
import java.util.List;

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

import com.ufcg.br.listapramim.model.ItemVenda;
import com.ufcg.br.listapramim.model.Produto;
import com.ufcg.br.listapramim.model.ProdutoDAO;
import com.ufcg.br.listapramim.service.ProdutoService;

@RestController
@RequestMapping({"/produto"})
public class ProdutoResource {

	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getProdutos(){
		List<Produto> produtos = this.produtoService.getProdutos();
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getProduto(@PathVariable ObjectId id) {
		Produto produto = this.produtoService.getProduto(id);
		System.out.println(id);
		if(produto != null) {
			return ResponseEntity.ok().body(produto);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/ordered")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenados(){
		ArrayList<Produto> produtos = this.produtoService.getProdutosOrdenados();
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/ordered/{categoria}")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenadosCategoria(@PathVariable String categoria){
		ArrayList<Produto> produtos = this.produtoService.getProdutosOrdenadosCategoria(categoria);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/ordered/preco")
	public ResponseEntity<ArrayList<ItemVenda>> getProdutosOrdenadosPreco(){
		ArrayList<ItemVenda> itensVenda = this.produtoService.getProdutosOrdenadosPreco();
		if(itensVenda.size() > 0) {
			return ResponseEntity.ok().body(itensVenda);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/search/{nome}")
	public ResponseEntity<ArrayList<Produto>> pesquisaProdutoNome(@PathVariable String nome){
		ArrayList<Produto> produtos = this.produtoService.pesquisaProdutoNome(nome);
		if(produtos.size() > 0) {
			return ResponseEntity.ok().body(produtos);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<Produto> cadastrarProduto(@RequestBody ProdutoDAO produtoAdd) {
		Produto produto = this.produtoService.cadastrarProduto(produtoAdd);
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
