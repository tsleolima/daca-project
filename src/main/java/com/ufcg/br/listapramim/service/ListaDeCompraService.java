package com.ufcg.br.listapramim.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
		return this.listaDeCompraRepository.save(lista);
	}

	public ResponseEntity<ListaDeCompra> atualizarLista(ObjectId id, ListaDeCompra lista) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findListaBy_id(id);
		if(listaBuscada != null) {
			listaBuscada.setCompras(lista.getCompras());
			listaBuscada.setDescritor(lista.getDescritor());
			listaBuscada.setLocalCompra(lista.getLocalCompra());
			listaBuscada.setValorFinal(lista.getValorFinal());
			this.listaDeCompraRepository.delete(listaBuscada);
			ListaDeCompra listaatt = this.listaDeCompraRepository.save(lista);
			return ResponseEntity.ok().body(listaatt);
		} else return ResponseEntity.notFound().build();
		
	}

	public ResponseEntity<ListaDeCompra> removerLista(ObjectId id) {
		ListaDeCompra lista = this.listaDeCompraRepository.findListaBy_id(id);
		if(lista != null) {
			 this.listaDeCompraRepository.delete(lista);
			 return ResponseEntity.ok().build();
		} else return ResponseEntity.notFound().build();
	}

	public ResponseEntity<ListaDeCompra> getListaCompra(ObjectId id) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findListaBy_id(id);		
		ArrayList<Compra> comprasOrdenadas = new ArrayList<Compra>();
		comprasOrdenadas.addAll(produtosHigienePessoal(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosLimpeza(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosIndustrializados(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosNaoIndustrializados(listaBuscada.getCompras()));
		
		listaBuscada.setCompras(comprasOrdenadas);		
		return ResponseEntity.ok().body(listaBuscada);
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
}
