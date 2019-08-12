package com.ufcg.br.listapramim.resource;

import java.util.List;

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

import com.ufcg.br.listapramim.model.Produto;
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
	
	@GetMapping(path= {"id"})
	public ResponseEntity<Produto> getProduto(@PathVariable long id) {
		return this.produtoService.getProduto(id);
	}
	
	@PostMapping
	public Produto cadastrarProduto(@RequestBody Produto produto) {
		return this.produtoService.cadastrarProduto(produto);
	}

	@PutMapping(value = {"id"})
	public ResponseEntity<Produto> atualizarProduto (@PathVariable long id, Produto produto) {
		return this.produtoService.atualizarProduto(id,produto);
	}
	
	@DeleteMapping(path = {"id"})
	public ResponseEntity<?> deletarProduto (@PathVariable long id) {
		return this.produtoService.deletarProduto(id);
	}
}
