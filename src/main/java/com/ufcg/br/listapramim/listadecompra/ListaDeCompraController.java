package com.ufcg.br.listapramim.listadecompra;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

import com.ufcg.br.listapramim.usuario.CustomUserDetailsService;
import com.ufcg.br.listapramim.usuario.Users;

@RestController
@RequestMapping({"/api/listacompra"})
public class ListaDeCompraController {
	
	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private ListaDeCompraService listaDeCompraService;
	
	@GetMapping
	@Cacheable("listas")
	public ResponseEntity<List<ListaDeCompra>> getListas(){
        simulateSlowService();
		Users user = userService.getUserCurrent();
		List<ListaDeCompra> listas = listaDeCompraService.getListas(user);
		if(listas.size() > 0) {
			return ResponseEntity.ok().body(listas);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/semcache")
	public ResponseEntity<List<ListaDeCompra>> getListasSemCache(){
		Users user = userService.getUserCurrent();
		List<ListaDeCompra> listas = listaDeCompraService.getListas(user);
		if(listas.size() > 0) {
			return ResponseEntity.ok().body(listas);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	private void simulateSlowService() {
        try {
            long time = 500L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ListaDeCompra> getListaCompra(@PathVariable ObjectId id){
		Users user = userService.getUserCurrent();
		ListaDeCompra lista = this.listaDeCompraService.getListaCompra(user,id);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	@CacheEvict(value="listas", allEntries = true) 
	public ResponseEntity<ListaDeCompra> cadastrarLista (@RequestBody ListaDeCompra listaAdd) {
		Users user = userService.getUserCurrent();
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
	public ResponseEntity<ListaDeCompra> atualizarLista(@PathVariable ObjectId id, @RequestBody ListaDeCompra listaAtt){
		Users user = userService.getUserCurrent();
		ListaDeCompra lista = this.listaDeCompraService.atualizarLista(user, id, listaAtt);
		if(lista != null) {
			return ResponseEntity.ok().body(lista);
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerLista(@PathVariable ObjectId id) {
		Users user = userService.getUserCurrent();
		ListaDeCompra lista = this.listaDeCompraService.removerLista(user,id);
		if(lista != null) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/search/descritor/{descritor}")
	public ResponseEntity<ListaDeCompra> buscarListaDescritor(@PathVariable String descritor){
		Users user = userService.getUserCurrent();
		ArrayList<ListaDeCompra> listasUser = (ArrayList<ListaDeCompra>) this.listaDeCompraService.getListas(user);
		return ResponseEntity.ok().body(this.listaDeCompraService.findProdutoByDescritor(listasUser, descritor));
	}
	
	@GetMapping("/search/data/{data}") // yyyy-mm-dd
	public ResponseEntity<ArrayList<ListaDeCompra>> buscarListaData(@PathVariable String data){
		Users user = userService.getUserCurrent();
		return ResponseEntity.ok().body(this.listaDeCompraService.buscarListaData(user,data));
	}
	
	@GetMapping("/search/produto/{idProduto}")
	public ResponseEntity<ArrayList<ListaDeCompra>> buscarListaProduto(@PathVariable ObjectId idProduto){
		Users user = userService.getUserCurrent();
		return ResponseEntity.ok().body(this.listaDeCompraService.buscarListaProduto(user,idProduto));
	}
	
	@GetMapping("/automatic/recent")
	public ResponseEntity<ListaDeCompra> gerarUltimaListaFinalizada(){
		Users user = userService.getUserCurrent();
		ListaDeCompra lista = this.listaDeCompraService.gerarUltimaListaFinalizada(user);
		if(lista != null) return ResponseEntity.ok().body(lista);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/automatic/itemcompra/{idProduto}")
	public ResponseEntity<ListaDeCompra> gerarListaItemCompra(@PathVariable ObjectId idProduto){
		Users user = userService.getUserCurrent();
		ListaDeCompra lista = this.listaDeCompraService.gerarListaItemCompra(user,idProduto);
		if(lista != null) return ResponseEntity.ok().body(lista);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/automatic/maisfrequentes")
	public ResponseEntity<ListaDeCompra> gerarListaProdutosMaisFrequentes(){
		Users user = userService.getUserCurrent();
		ListaDeCompra lista = this.listaDeCompraService.gerarListaProdutosMaisFrequentes(user);
		if(lista != null) return ResponseEntity.ok().body(lista);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/suggestion/{id}")
	public ResponseEntity<ArrayList<SugestaoDAO>> sugerirLocalDeCompra(@PathVariable ObjectId id){
		Users user = userService.getUserCurrent();
		ArrayList<SugestaoDAO> sugestoes = this.listaDeCompraService.sugerirLocalDeCompra(user,id);
		if(sugestoes != null) return ResponseEntity.ok().body(sugestoes);
		return ResponseEntity.noContent().build();
	}
	
}
