package com.ufcg.br.listapramim.produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.br.listapramim.listadecompra.Compra;
import com.ufcg.br.listapramim.listadecompra.CompraNomeComparator;
import com.ufcg.br.listapramim.listadecompra.ItemVendaPrecoComparator;
import com.ufcg.br.listapramim.listadecompra.ListaDeCompra;
import com.ufcg.br.listapramim.usuario.Users;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public List<Produto> getProdutos (Users user) {
		List<Produto> produtos = this.produtoRepository.findAll();
		List<Produto> produtosUser = new ArrayList<Produto>();
		for (int s = 0; s < produtos.size(); s++) {
			if (produtos.get(s).getUser().getId().equals(user.getId())) {
				produtosUser.add(produtos.get(s));
			}
		}
		return produtosUser;
	}
	
	public Produto getProduto(ObjectId id) {
		return this.produtoRepository.findProdutoBy_id(id);
	}	

	public ArrayList<Produto> getProdutosOrdenados(Users user) {
		ArrayList<Produto> produtosUser = (ArrayList<Produto>) getProdutos(user);
		Collections.sort(produtosUser, new ProdutoNomeComparator());
		return produtosUser;
	}

	public ArrayList<Produto> getProdutosOrdenadosCategoria(Users user, String categoria) {
		ArrayList<Produto> produtos = (ArrayList<Produto>) getProdutos(user);
		produtos = (ArrayList<Produto>) produtos.stream()
				.filter( produto -> produto.getCategoria().equals(Categoria.valueOf(categoria)))
				.collect(Collectors.toList());
		
		Collections.sort(produtos, new ProdutoNomeComparator());
		return produtos;
	}

	public ArrayList<ItemVenda> getProdutosOrdenadosPreco(Users user) {
		ArrayList<Produto> produtos = (ArrayList<Produto>) getProdutos(user);
		ArrayList<ItemVenda> itensAvenda = new ArrayList<ItemVenda>();
		for (Produto p : produtos) {
			for (ItemVenda i : p.getMapaDePrecos()) {
				itensAvenda.add(i);
			}
		}
		Collections.sort(itensAvenda,new ItemVendaPrecoComparator());
		return itensAvenda;
	}
	
	public Produto cadastrarProduto(Users user, ProdutoDAO produto) {
		Produto updated;
		ArrayList<Produto> produtosUser = (ArrayList<Produto>) getProdutos(user);
		if(findProdutoByNome(produtosUser,produto.getNome()) == null) {
			
			if (produto.getQuantidade() != null) updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo(),produto.getQuantidade());
			else updated = new Produto(produto.getNome(),produto.getCategoria(),produto.getTipo());
			
			System.out.println(user.getId().toString());
			updated.setUser(user);
			return this.produtoRepository.save(updated);
			
		} 
		return null;
	}

	private Object findProdutoByNome(ArrayList<Produto> produtosUser, String nome) {
		Produto produtofinal = null;
		for (Produto produto : produtosUser) {
			if(produto.getNome().equals(nome)) {
				produtofinal = produto;
			}
		}
		return produtofinal;
	}

	public Produto atualizarProduto(ObjectId id, Produto produto) {
		Produto produtoBuscado = this.produtoRepository.findProdutoBy_id(id);
		if(produtoBuscado != null) {
			produtoBuscado.setNome(produto.getNome());
			produtoBuscado.setCategoria(produto.getCategoria());
			produtoBuscado.setQuantidade(produto.getQuantidade());
			produtoBuscado.setTipo(produto.getTipo());
			produtoBuscado.setMapaDePrecos(produto.getMapaDePrecos());
			Produto updated = produtoRepository.save(produtoBuscado);
			return updated;}
		else
			return produtoBuscado;
	}

	public Produto deletarProduto(ObjectId id) {
		Produto produto = produtoRepository.findProdutoBy_id(id);
		if(produto != null) produtoRepository.delete(produto);
		return produto;
				
	}

	public ArrayList<Produto> pesquisaProdutoNome(Users user, String nome) {
		ArrayList<Produto> produtos = (ArrayList<Produto>) getProdutos(user);
		ArrayList<Produto> saida = new ArrayList<Produto>();
		for (Produto produto : produtos) {
			String[] palavrasProduto = produto.getNome().split(" ");
			for (String palavra : palavrasProduto) {
				if(palavra.toLowerCase().equals(nome.toLowerCase())) {
					saida.add(produto);
				}
			}
		}
		
		return saida;
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

	public ArrayList<Produto> getProdutosComPreco(ListaDeCompra lista) {
		ArrayList<Produto> produtos = new ArrayList<Produto>();
		for (Compra compra : lista.getCompras()) {
			Produto produto = this.getProduto(compra.getIdProduto());
			if(produto.getMapaDePrecos() != null) {
				produtos.add(produto);
			}
		}
		return produtos;
	}
	
}
