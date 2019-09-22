package com.ufcg.br.listapramim.listadecompra;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
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

import com.ufcg.br.listapramim.usuario.Users;

@RestController
@RequestMapping({"/api/listacompra"})
public class ListaDeCompraController {

	@Autowired
	private ListaDeCompraService listaDeCompraService;
	
	@GetMapping
	public ResponseEntity<List<ListaDeCompra>> getListas(HttpServletRequest request){
		Users user = (Users) request.getAttribute("user");
		List<ListaDeCompra> listas = listaDeCompraService.getListas(user);
		if(listas.size() > 0) {
			return ResponseEntity.ok().body(listas);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ListaDeCompra> getListaCompra(HttpServletRequest request,@PathVariable ObjectId id){
		Users user = (Users) request.getAttribute("user");
		ListaDeCompra lista = this.listaDeCompraService.getListaCompra(user,id);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<ListaDeCompra> cadastrarLista (HttpServletRequest request ,@RequestBody ListaDeCompra listaAdd) {
		Users user = (Users) request.getAttribute("user");
		Set<ConstraintViolation<ListaDeCompra>> validate = Validation.buildDefaultValidatorFactory().getValidator().validate(listaAdd);
		if (!validate.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		ListaDeCompra lista = this.listaDeCompraService.cadastrarLista(user,listaAdd);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		}
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ListaDeCompra> atualizarLista(HttpServletRequest request ,@PathVariable ObjectId id, @RequestBody ListaDeCompra listaAtt){
		Users user = (Users) request.getAttribute("user");
		ListaDeCompra lista = this.listaDeCompraService.atualizarLista(user, id, listaAtt);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerLista(HttpServletRequest request ,@PathVariable ObjectId id) {
		Users user = (Users) request.getAttribute("user");
		ListaDeCompra lista = this.listaDeCompraService.removerLista(user,id);
		if(lista != null) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/search/descritor/{descritor}")
	public ResponseEntity<ListaDeCompra> buscarListaDescritor(HttpServletRequest request,@PathVariable String descritor){
		Users user = (Users) request.getAttribute("user");
		ArrayList<ListaDeCompra> listasUser = (ArrayList<ListaDeCompra>) this.listaDeCompraService.getListas(user);
		return ResponseEntity.ok().body(this.listaDeCompraService.findProdutoByDescritor(listasUser, descritor));
	}
	
	@GetMapping("/search/data/{data}") // yyyy-mm-dd
	public ResponseEntity<ArrayList<ListaDeCompra>> buscarListaData(HttpServletRequest request,@PathVariable String data){
		Users user = (Users) request.getAttribute("user");
		return ResponseEntity.ok().body(this.listaDeCompraService.buscarListaData(user,data));
	}
	
	@GetMapping("/search/produto/{idProduto}")
	public ResponseEntity<ArrayList<ListaDeCompra>> buscarListaProduto(HttpServletRequest request,@PathVariable ObjectId idProduto){
		Users user = (Users) request.getAttribute("user");
		return ResponseEntity.ok().body(this.listaDeCompraService.buscarListaProduto(user,idProduto));
	}
	
	@GetMapping("/automatic/recent")
	public ResponseEntity<ListaDeCompra> gerarUltimaListaFinalizada(HttpServletRequest request){
		Users user = (Users) request.getAttribute("user");
		ListaDeCompra lista = this.listaDeCompraService.gerarUltimaListaFinalizada(user);
		if(lista != null) return ResponseEntity.ok().body(lista);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/automatic/itemcompra/{idProduto}")
	public ResponseEntity<ListaDeCompra> gerarListaItemCompra(HttpServletRequest request,@PathVariable ObjectId idProduto){
		Users user = (Users) request.getAttribute("user");
		ListaDeCompra lista = this.listaDeCompraService.gerarListaItemCompra(user,idProduto);
		if(lista != null) return ResponseEntity.ok().body(lista);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/automatic/maisfrequentes")
	public ResponseEntity<ListaDeCompra> gerarListaProdutosMaisFrequentes(HttpServletRequest request){
		Users user = (Users) request.getAttribute("user");
		ListaDeCompra lista = this.listaDeCompraService.gerarListaProdutosMaisFrequentes(user);
		if(lista != null) return ResponseEntity.ok().body(lista);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/suggestion/{id}")
	public ResponseEntity<ArrayList<SugestaoDAO>> sugerirLocalDeCompra(HttpServletRequest request,@PathVariable ObjectId id){
		Users user = (Users) request.getAttribute("user");
		ArrayList<SugestaoDAO> sugestoes = this.listaDeCompraService.sugerirLocalDeCompra(user,id);
		if(sugestoes != null) return ResponseEntity.ok().body(sugestoes);
		return ResponseEntity.noContent().build();
	}
	
}
