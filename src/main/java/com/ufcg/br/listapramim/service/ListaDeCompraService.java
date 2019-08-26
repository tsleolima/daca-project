package com.ufcg.br.listapramim.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.br.listapramim.model.Compra;
import com.ufcg.br.listapramim.model.ListaDeCompra;
import com.ufcg.br.listapramim.repository.ListaDeCompraRepository;

@Service
public class ListaDeCompraService {

	@Autowired
	private ListaDeCompraRepository listaDeCompraRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	public List<ListaDeCompra> getListas() {
		return this.listaDeCompraRepository.findAll();
	}

	public ListaDeCompra cadastrarProduto(ListaDeCompra lista) {
		if(this.listaDeCompraRepository.findProdutoByDescritor(lista.getDescritor()) == null) {
			return this.listaDeCompraRepository.save(lista);
		} 
		return null;
	}

	public ListaDeCompra atualizarLista(ObjectId id, ListaDeCompra lista) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findListaBy_id(id);
		if(listaBuscada != null) {
			listaBuscada.setCompras(lista.getCompras());
			listaBuscada.setDescritor(lista.getDescritor());
			listaBuscada.setLocalCompra(lista.getLocalCompra());
			listaBuscada.setValorFinal(lista.getValorFinal());
			ListaDeCompra listaatt = this.listaDeCompraRepository.save(listaBuscada);
			return listaatt;
		} else return listaBuscada;
		
	}

	public ListaDeCompra removerLista(ObjectId id) {
		ListaDeCompra lista = this.listaDeCompraRepository.findListaBy_id(id);
		if(lista != null) this.listaDeCompraRepository.delete(lista);
		return lista;
	}

	public ListaDeCompra getListaCompra(ObjectId id) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findListaBy_id(id);		
		ArrayList<Compra> comprasOrdenadas = new ArrayList<Compra>();
		comprasOrdenadas.addAll(produtosHigienePessoal(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosLimpeza(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosIndustrializados(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosNaoIndustrializados(listaBuscada.getCompras()));
		
		listaBuscada.setCompras(comprasOrdenadas);		
		return listaBuscada;
	}
	
	public ArrayList<Compra> produtosIndustrializados(ArrayList<Compra> compras) {
		return this.produtoService.produtosIndustrializados(compras);
	}

	public ArrayList<Compra> produtosNaoIndustrializados(ArrayList<Compra> compras) {
		return this.produtoService.produtosNaoIndustrializados(compras);
	}
	
	public ArrayList<Compra> produtosLimpeza(ArrayList<Compra> compras) {
		return this.produtoService.produtosLimpeza(compras);
	}
	
	public ArrayList<Compra> produtosHigienePessoal(ArrayList<Compra> compras) {
		return this.produtoService.produtosHigienePessoal(compras);
	}
	
	public ListaDeCompra buscarListaDescritor(String descritor) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findProdutoByDescritor(descritor);
		return listaBuscada;
	}
	
	public ListaDeCompra buscarListaProduto(ObjectId idProduto) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) this.listaDeCompraRepository.findAll();
		ArrayList<ListaDeCompra> listasBuscadas = new ArrayList<ListaDeCompra>();
		for (ListaDeCompra lista : listas) {
			ArrayList<Compra> compras = lista.getCompras();
			for (Compra compra : compras) {
				
			}
		}
	}

	public ArrayList<ListaDeCompra> buscarListaData(String data) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) this.listaDeCompraRepository.findAll();
		ArrayList<ListaDeCompra> listasBuscadas = new ArrayList<ListaDeCompra>();
		for (ListaDeCompra lista : listas) {
			String[] splitedDateList = lista.get_id().getDate().toString().split(" ");
			String dateFormat = splitedDateList[1] + " " + splitedDateList[2] + " " + splitedDateList[5];
			if(data.equals(formatDateList(dateFormat))) {
				listasBuscadas.add(lista);
			}
		}
		
		return listasBuscadas;
	}
	
	private String formatDateList(String string) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(string, formatter);
		return date.toString();
	}
}
