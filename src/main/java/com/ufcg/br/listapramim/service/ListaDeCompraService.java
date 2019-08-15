package com.ufcg.br.listapramim.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ufcg.br.listapramim.model.ListaDeCompra;
import com.ufcg.br.listapramim.repository.ListaDeCompraRepository;

@Service
public class ListaDeCompraService {

	@Autowired
	private ListaDeCompraRepository listaDeCompraRepository;
	
	public List<ListaDeCompra> getListas() {
		return this.listaDeCompraRepository.findAll();
	}

	public ListaDeCompra cadastrarProduto(ListaDeCompra lista) {
		return this.listaDeCompraRepository.save(lista);
	}

	public ResponseEntity<ListaDeCompra> atualizarLista(ObjectId id, ListaDeCompra lista) {
		ListaDeCompra listaBuscada =  this.listaDeCompraRepository.findListaBy_id(id);
		if(listaBuscada != null) {
			listaBuscada.setCompras(lista.getCompras());
			listaBuscada.setDescritor(lista.getDescritor());
			ListaDeCompra listaatt = this.listaDeCompraRepository.save(lista);
			return ResponseEntity.ok().body(listaatt);
		} else return ResponseEntity.notFound().build();
		
	}

}
