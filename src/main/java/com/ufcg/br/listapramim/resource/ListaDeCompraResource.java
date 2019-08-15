package com.ufcg.br.listapramim.resource;

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

import com.ufcg.br.listapramim.model.ListaDeCompra;
import com.ufcg.br.listapramim.service.ListaDeCompraService;

@RestController
@RequestMapping({"/listacompra"})
public class ListaDeCompraResource {

	@Autowired
	private ListaDeCompraService listaDeCompraService;
	
	@GetMapping
	public List<ListaDeCompra> getListas(){
		return listaDeCompraService.getListas();
	}
	
	@PostMapping
	public ListaDeCompra cadastrarProduto (@RequestBody ListaDeCompra lista) {
		return this.listaDeCompraService.cadastrarProduto(lista);
	}
	
	/* adicionar validacao para descritor repetido e duplicatas em compras */
	@PutMapping({"/id"})
	public ResponseEntity<ListaDeCompra> atualizarLista(@PathVariable ObjectId id, @RequestBody ListaDeCompra lista){
		return this.listaDeCompraService.atualizarLista(id,lista);
	}
	
	@DeleteMapping({"/id"})
	public ResponseEntity<ListaDeCompra> removerLista(@PathVariable ObjectId id) {
		return this.listaDeCompraService.removerLista(id);
	}
	
}
