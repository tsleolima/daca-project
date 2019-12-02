package com.ufcg.br.listapramim.listadecompra;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"/api/listacompra"})
public class ListaDeCompraController {
	
	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private ListaDeCompraService listaDeCompraService;
	
	@GetMapping
	@Cacheable("listas")
	public Flux<ListaDeCompra> getListas(){
		Users user = userService.getUserCurrent();
		Flux<ListaDeCompra> listas = listaDeCompraService.getListas(user);
		return listas;
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<ListaDeCompra>> getListaCompra(@PathVariable ObjectId id){
		Users user = userService.getUserCurrent();
		Mono<ListaDeCompra> lista = this.listaDeCompraService.getListaCompra(user,id);
		
		return lista.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	@PostMapping
	@CacheEvict(value="listas", allEntries = true) 
	public Mono<ResponseEntity<ListaDeCompra>> cadastrarLista (@RequestBody ListaDeCompra listaAdd) {
		Users user = userService.getUserCurrent();

		Mono<ListaDeCompra> lista = this.listaDeCompraService.cadastrarLista(user,listaAdd);

		return lista.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<ListaDeCompra>> atualizarLista(@PathVariable ObjectId id, @RequestBody ListaDeCompra listaAtt){
		Users user = userService.getUserCurrent();
		Mono<ListaDeCompra> lista = this.listaDeCompraService.atualizarLista(user, id, listaAtt);
		
		return lista.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> removerLista(@PathVariable ObjectId id) {
		Users user = userService.getUserCurrent();
		Mono<Void> lista = this.listaDeCompraService.removerLista(user,id);
		
		return lista.map( l -> ResponseEntity.ok().build())
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	@GetMapping("/search/descritor/{descritor}")
	public Flux<ResponseEntity<ListaDeCompra>> buscarListaDescritor(@PathVariable String descritor){
		Users user = userService.getUserCurrent();
		Flux<ListaDeCompra> listasUser = this.listaDeCompraService.getListas(user);
		
		return listasUser.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	@GetMapping("/search/data/{data}") // yyyy-mm-dd
	public Flux<ResponseEntity<ListaDeCompra>> buscarListaData(@PathVariable String data){
		Users user = userService.getUserCurrent();
		Flux<ListaDeCompra> listasUser = this.listaDeCompraService.buscarListaData(user,data);
		
		return listasUser.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	@GetMapping("/search/produto/{idProduto}")
	public Flux<ResponseEntity<ListaDeCompra>> buscarListaProduto(@PathVariable ObjectId idProduto){
		Users user = userService.getUserCurrent();
		Flux<ListaDeCompra> listasUser = this.listaDeCompraService.buscarListaProduto(user,idProduto);

		return listasUser.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	@GetMapping("/automatic/recent")
	public Mono<ResponseEntity<ListaDeCompra>> gerarUltimaListaFinalizada(){
		Users user = userService.getUserCurrent();
		Mono<ListaDeCompra> listasUser = this.listaDeCompraService.gerarUltimaListaFinalizada(user);

		return listasUser.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
	}
	
	@GetMapping("/automatic/itemcompra/{idProduto}")
	public Mono<ResponseEntity<ListaDeCompra>> gerarListaItemCompra(@PathVariable ObjectId idProduto){
		Users user = userService.getUserCurrent();
		Mono<ListaDeCompra> lista = this.listaDeCompraService.gerarListaItemCompra(user,idProduto);

		return lista.map( l -> ResponseEntity.ok().body(l))
				.switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
	}
	
//	@GetMapping("/automatic/maisfrequentes")
//	public Flux<ResponseEntity<ListaDeCompra>> gerarListaProdutosMaisFrequentes(){
//		Users user = userService.getUserCurrent();
//		Flux<ListaDeCompra> lista = this.listaDeCompraService.gerarListaProdutosMaisFrequentes(user);
//
//		return lista.map( l -> ResponseEntity.ok().body(l))
//				.switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
//	}
//	
//	@GetMapping("/suggestion/{id}")
//	public Flux<ResponseEntity<SugestaoDAO>> sugerirLocalDeCompra(@PathVariable ObjectId id){
//		Users user = userService.getUserCurrent();
//		Flux<SugestaoDAO> sugestoes = this.listaDeCompraService.sugerirLocalDeCompra(user,id);
//
//		return sugestoes.map( s -> ResponseEntity.ok().body(s))
//				.switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
//	}
//	
}
