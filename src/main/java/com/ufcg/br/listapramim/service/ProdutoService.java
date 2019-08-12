package com.ufcg.br.listapramim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ufcg.br.listapramim.repository.ProdutoRepository;
import com.ufcg.br.listapramim.model.Produto;
import com.ufcg.br.listapramim.model.ProdutoDAO;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private NextSequenceService nextSequenceService;

	public List<Produto> getProdutos () {
		return this.produtoRepository.findAll();
	}

	public Produto cadastrarProduto(ProdutoDAO produto) {
		Produto updated;
		if (produto.getQuantidade() != null) updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo(),produto.getQuantidade());
		else updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo());
		updated.setId(nextSequenceService.getNextSequence("customSequences"));
		return this.produtoRepository.save(updated);
	}

	public ResponseEntity<Produto> getProduto(long id) {
		return this.produtoRepository.findById(id)
				.map(produto -> ResponseEntity.ok().body(produto))
				.orElse(ResponseEntity.notFound().build());
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
