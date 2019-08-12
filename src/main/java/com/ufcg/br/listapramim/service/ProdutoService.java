package com.ufcg.br.listapramim.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ufcg.br.listapramim.model.Categoria;
import com.ufcg.br.listapramim.model.Produto;
import com.ufcg.br.listapramim.model.ProdutoDAO;
import com.ufcg.br.listapramim.model.ProdutoNomeComparator;
import com.ufcg.br.listapramim.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private NextSequenceService nextSequenceService;

	public List<Produto> getProdutos () {
		return this.produtoRepository.findAll();
	}
	
	public ResponseEntity<Produto> getProduto(long id) {
		return this.produtoRepository.findById(id)
				.map(produto -> ResponseEntity.ok().body(produto))
				.orElse(ResponseEntity.notFound().build());
	}	

	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenados() {
		ArrayList<Produto> produtos = (ArrayList<Produto>) this.produtoRepository.findAll();
		Collections.sort(produtos, new ProdutoNomeComparator());
		return ResponseEntity.ok().body(produtos);
	}

	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenadosCategoria(String categoria) {
		ArrayList<Produto> produtos = (ArrayList<Produto>) this.produtoRepository.findAll();
		produtos = (ArrayList<Produto>) produtos.stream()
				.filter( produto -> produto.getCategoria().equals(Categoria.valueOf(categoria)))
				.collect(Collectors.toList());
		
		Collections.sort(produtos, new ProdutoNomeComparator());
		return ResponseEntity.ok().body(produtos);
	}

	public ResponseEntity<ArrayList<Produto>> getProdutosOrdenadosPreco() {
		
	}
	
	public Produto cadastrarProduto(ProdutoDAO produto) {
		Produto updated;
		if (produto.getQuantidade() != null) updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo(),produto.getQuantidade());
		else updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo());
		updated.setId(nextSequenceService.getNextSequence("customSequences"));
		return this.produtoRepository.save(updated);
	}

	public ResponseEntity<Produto> atualizarProduto(long id, Produto produto) {
		return this.produtoRepository.findById(id)
				.map(produtoBuscado -> {
					produtoBuscado.setNome(produto.getNome());
					produtoBuscado.setCategoria(produto.getCategoria());
					produtoBuscado.setQuantidade(produto.getQuantidade());
					produtoBuscado.setTipo(produto.getTipo());
					produtoBuscado.setMapaDePrecos(produto.getMapaDePrecos());
					Produto updated = produtoRepository.save(produtoBuscado);
					return ResponseEntity.ok().body(updated);
				}).orElse(ResponseEntity.notFound().build());
	}

	public ResponseEntity<?> deletarProduto(long id) {
		return this.produtoRepository.findById(id)
				.map(produto -> {
					produtoRepository.delete(produto);
					return ResponseEntity.ok().build();
				}).orElse(ResponseEntity.notFound().build());
				
	}
}
