package com.ufcg.br.listapramim.produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ufcg.br.listapramim.listadecompra.Compra;
import com.ufcg.br.listapramim.listadecompra.CompraNomeComparator;
import com.ufcg.br.listapramim.listadecompra.ItemVendaPrecoComparator;
import com.ufcg.br.listapramim.listadecompra.ListaDeCompra;
import com.ufcg.br.listapramim.usuario.Users;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public Flux<Produto> getProdutos (Users user) {
		Flux<Produto> produtos = produtoRepository.findByUser(user);
		return produtos;
	} 
	 
	public Mono<Produto> getProduto(Users user,ObjectId id) {
		Mono<Produto> produto = this.produtoRepository.findBy_id(id);
		Mono<Produto> produtoUser = produto.filter(p -> p.getUser().getId().equals(user.getId()));		
		return produtoUser;
	}	

	public Flux<Produto> getProdutosOrdenados(Users user) {
		Flux<Produto> produtosUser = getProdutos(user);
		produtosUser = produtosUser.sort(new ProdutoNomeComparator());
		return produtosUser;
	}

	public Flux<Produto> getProdutosOrdenadosCategoria(Users user, String categoria) {
		Flux<Produto> produtosUser = getProdutos(user);
		produtosUser = produtosUser
				.filter( produto -> produto.getCategoria().equals(Categoria.valueOf(categoria)));
		
		produtosUser = produtosUser.sort(new ProdutoNomeComparator());
		return produtosUser;
	}

	public Flux<ItemVenda> getProdutosOrdenadosPreco(Users user) {
		Flux<Produto> produtosUser = getProdutos(user);
		Flux<ItemVenda> itensAvenda = produtosUser.flatMap( p -> Flux.fromIterable( p.getMapaDePrecos()));
		
		itensAvenda = itensAvenda.sort(new ItemVendaPrecoComparator());
		return itensAvenda;
	}
	
	public Mono<Produto> cadastrarProduto(Users user, ProdutoDAO produto) {
		Produto updated;
		Mono<Produto> produtoUser = this.produtoRepository.findByNomeAndUser(produto.getNome(), user);
			
		if (produto.getQuantidade() != null) updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo(),produto.getQuantidade());
		else updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo());
		updated.setUser(user);
		
		return produtoUser.filter( p -> p != null ).switchIfEmpty(this.produtoRepository.save(updated));
	}

	public Mono<Produto> atualizarProduto(Users user,ObjectId id, Produto produto) {
		Mono<Produto> produtoBuscado = getProduto(user, id);
		return produtoBuscado.flatMap(p -> 
				{p.setNome(produto.getNome());
				 p.setCategoria(produto.getCategoria());
				 p.setMapaDePrecos(produto.getMapaDePrecos());
				 p.setTipo(produto.getTipo());
				 p.setQuantidade(produto.getQuantidade());
				 Mono<Produto> updated = produtoRepository.save(p);
				 return updated;
				})			
				.switchIfEmpty(Mono.empty());
	}

	public Mono<Void> deletarProduto(Users user,ObjectId id) {
		Mono<Produto> produto = getProduto(user, id);
		return produto
				.flatMap( d -> {
					return produtoRepository.deleteById(id);
				})
				.switchIfEmpty(Mono.empty());
	}

	public Flux<Produto> pesquisaProdutoNome(Users user, String nome) {
		Flux<Produto> produtos = getProdutos(user);		
		return produtos.filter(produto -> 
			produto.getNome().toLowerCase()
			.contains(nome.toLowerCase()));
	}

	public ArrayList<Compra> produtosIndustrializados(ArrayList<Compra> compras) {
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findBy_id(compra.getIdProduto()).block().getCategoria().equals(Categoria.valueOf("ALIMENTOSINDUSTRIALIZADOS")))
				.collect(Collectors.toList());
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;
	}

	public ArrayList<Compra> produtosNaoIndustrializados(ArrayList<Compra> compras) {	
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findBy_id(compra.getIdProduto()).block().getCategoria().equals(Categoria.valueOf("ALIMENTOSNAOINDUSTRIALIZADOS")))
				.collect(Collectors.toList());
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;

	}

	public ArrayList<Compra> produtosLimpeza(ArrayList<Compra> compras) {
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findBy_id(compra.getIdProduto()).block().getCategoria().equals(Categoria.valueOf("LIMPEZA")))
				.collect(Collectors.toList());
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;
	}

	public ArrayList<Compra> produtosHigienePessoal(ArrayList<Compra> compras) {
		ArrayList<Compra> comprasCategoriaSelecionada = (ArrayList<Compra>) compras.stream()
				.filter(compra -> this.produtoRepository.findBy_id(compra.getIdProduto()).block().getCategoria().equals(Categoria.valueOf("HIGIENEPESSOAL")))
				.collect(Collectors.toList());
		
		Collections.sort(comprasCategoriaSelecionada, new CompraNomeComparator());
		return comprasCategoriaSelecionada;
	}

	public Flux<Produto> getProdutosComPreco(Users user,ListaDeCompra lista) {
		Flux<Produto> produtos = Flux.empty();
		for (Compra compra : lista.getCompras()) {
			Mono<Produto> produto = this.getProduto(user,compra.getIdProduto());
			produto.filter( p -> p.getMapaDePrecos() != null).mergeWith(produtos);
		}
		return produtos;
	}
	
}
