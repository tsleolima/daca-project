package com.ufcg.br.listapramim.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ufcg.br.listapramim.model.Compra;
import com.ufcg.br.listapramim.model.ItemVenda;
import com.ufcg.br.listapramim.model.Produto;
import com.ufcg.br.listapramim.model.ProdutoDAO;
import com.ufcg.br.listapramim.model.enums.Categoria;
import com.ufcg.br.listapramim.repository.ProdutoRepository;
import com.ufcg.br.listapramim.util.CompraNomeComparator;
import com.ufcg.br.listapramim.util.ItemVendaPrecoComparator;
import com.ufcg.br.listapramim.util.ProdutoNomeComparator;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public List<Produto> getProdutos () {
		return this.produtoRepository.findAll();
	}
	
	public ResponseEntity<Produto> getProduto(ObjectId id) {
		Produto produto = produtoRepository.findProdutoBy_id(id);
		if (produto != null) return ResponseEntity.ok().body(produto);
		else return ResponseEntity.notFound().build();
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

	public List<ItemVenda> getProdutosOrdenadosPreco() {
		System.out.println("cheguei aqui veei primeiro");
		ArrayList<Produto> produtos = (ArrayList<Produto>) this.produtoRepository.findAll();
		System.out.println("cheguei aqui veei segundo");
		ArrayList<ItemVenda> itensAvenda = new ArrayList<ItemVenda>();
		for (Produto p : produtos) {
			for (ItemVenda i : p.getMapaDePrecos()) {
				itensAvenda.add(i);
			}
		}
		Collections.sort(itensAvenda,new ItemVendaPrecoComparator());
		return itensAvenda;
	}
	
	public Produto cadastrarProduto(ProdutoDAO produto) {
		Produto updated;
		if (produto.getQuantidade() != null) updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo(),produto.getQuantidade());
		else updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo());
		return this.produtoRepository.save(updated);
	}

	public ResponseEntity<Produto> atualizarProduto(ObjectId id, Produto produto) {
		Produto produtoBuscado = this.produtoRepository.findProdutoBy_id(id);
		if(produtoBuscado != null) {
			produtoBuscado.setNome(produto.getNome());
			produtoBuscado.setCategoria(produto.getCategoria());
			produtoBuscado.setQuantidade(produto.getQuantidade());
			produtoBuscado.setTipo(produto.getTipo());
			produtoBuscado.setMapaDePrecos(produto.getMapaDePrecos());
			Produto updated = produtoRepository.save(produtoBuscado);
			return ResponseEntity.ok().body(updated);}
		else
			return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> deletarProduto(ObjectId id) {
		Produto produto = produtoRepository.findProdutoBy_id(id);
		if(produto != null) {
			produtoRepository.delete(produto);
			return ResponseEntity.ok().build();}
		else 
			return ResponseEntity.notFound().build();
				
	}

	public ResponseEntity<ArrayList<Produto>> pesquisaProdutoNome(String nome) {
		ArrayList<Produto> produtos = (ArrayList<Produto>) this.produtoRepository.findAll();
		ArrayList<Produto> saida = new ArrayList<Produto>();
		for (Produto produto : produtos) {
			String[] palavrasProduto = produto.getNome().split(" ");
			for (String palavra : palavrasProduto) {
				if(palavra.toLowerCase().equals(nome)) {
					saida.add(produto);
				}
			}
		}
		
		return ResponseEntity.ok().body(saida);
	}

	public ArrayList<Compra> produtosIndustrializados(ArrayList<Compra> compras) {
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findProdutoBy_id(compra.getIdProduto()).getCategoria().equals(Categoria.valueOf("ALIMENTOSINDUSTRIALIZADOS")))
				.collect(Collectors.toList());
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;
	}

	public ArrayList<Compra> produtosNaoIndustrializados(ArrayList<Compra> compras) {
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findProdutoBy_id(compra.getIdProduto()).getCategoria().equals(Categoria.valueOf("ALIMENTOSNAOINDUSTRIALIZADOS")))
				.collect(Collectors.toList());	
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;
	}

	public ArrayList<Compra> produtosLimpeza(ArrayList<Compra> compras) {
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findProdutoBy_id(compra.getIdProduto()).getCategoria().equals(Categoria.valueOf("LIMPEZA")))
				.collect(Collectors.toList());
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;
	}

	public ArrayList<Compra> produtosHigienePessoal(ArrayList<Compra> compras) {
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findProdutoBy_id(compra.getIdProduto()).getCategoria().equals(Categoria.valueOf("HIGIENEPESSOAL")))
				.collect(Collectors.toList());
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;
	}
	
}
