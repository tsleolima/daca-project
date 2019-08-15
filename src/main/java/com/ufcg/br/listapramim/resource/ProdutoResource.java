package com.ufcg.br.listapramim.resource;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Produto> getProdutos(){
		return this.produtoService.getProdutos();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getProduto(@PathVariable ObjectId id) {
		return this.produtoService.getProduto(id);
	}
	
	@GetMapping("/ordered")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenados(){
		return this.produtoService.getProdutosOrdenados();
	}
	
	@GetMapping("/ordered/{categoria}")
	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenadosCategoria(@PathVariable String categoria){
		return this.produtoService.getProdutosOrdenadosCategoria(categoria);
	}
	
	@GetMapping("/ordered/preco")
	public List<ItemVenda> getProdutosOrdenadosPreco(){
		return this.produtoService.getProdutosOrdenadosPreco();
	}
	
	@GetMapping("/search/{nome}")
	public ResponseEntity<ArrayList<Produto>> pesquisaProdutoNome(@PathVariable String nome){
		return this.produtoService.pesquisaProdutoNome(nome);
	}
	
	@PostMapping
	public Produto cadastrarProduto(@RequestBody ProdutoDAO produto) {
		return this.produtoService.cadastrarProduto(produto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizarProduto (@PathVariable ObjectId id, @RequestBody Produto produto) {
		return this.produtoService.atualizarProduto(id,produto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarProduto (@PathVariable ObjectId id) {
		return this.produtoService.deletarProduto(id);
	}
}
