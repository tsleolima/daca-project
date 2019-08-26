package com.ufcg.br.listapramim.resource;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<List<ListaDeCompra>> getListas(){
		List<ListaDeCompra> listas = listaDeCompraService.getListas();
		if(listas.size() > 0) {
			return ResponseEntity.ok().body(listas);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ListaDeCompra> getListaCompra(@PathVariable ObjectId id){
		ListaDeCompra lista = this.listaDeCompraService.getListaCompra(id);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<ListaDeCompra> cadastrarProduto (@RequestBody ListaDeCompra listaAdd) {
		ListaDeCompra lista = this.listaDeCompraService.cadastrarProduto(listaAdd);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		}
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ListaDeCompra> atualizarLista(@PathVariable ObjectId id, @RequestBody ListaDeCompra listaAtt){
		ListaDeCompra lista = this.listaDeCompraService.atualizarLista(id, listaAtt);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerLista(@PathVariable ObjectId id) {
		ListaDeCompra lista = this.listaDeCompraService.removerLista(id);
		if(lista != null) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/search/descritor/{descritor}")
	public ResponseEntity<ListaDeCompra> buscarListaDescritor(@PathVariable String descritor){
		return ResponseEntity.ok().body(this.listaDeCompraService.buscarListaDescritor(descritor));
	}
	
	@GetMapping("/search/data/{data}")
	public ResponseEntity<ArrayList<ListaDeCompra>> buscarListaData(@PathVariable String data){
		return ResponseEntity.ok().body(this.listaDeCompraService.buscarListaData(data));
	}
	
	@GetMapping("/search/idproduto/{idProduto}")
	public ResponseEntity<ListaDeCompra> buscarListaProduto(@PathVariable ObjectId idProduto){
		return ResponseEntity.ok().body(this.listaDeCompraService.buscarListaProduto(idProduto));
	}
	
}
