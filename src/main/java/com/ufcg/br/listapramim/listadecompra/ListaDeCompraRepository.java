package com.ufcg.br.listapramim.listadecompra;

import org.springframework.stereotype.Repository;

import com.ufcg.br.listapramim.usuario.Users;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

@Repository
public interface ListaDeCompraRepository extends ReactiveMongoRepository<ListaDeCompra,ObjectId>{
	
	Mono<ListaDeCompra> findBy_id(ObjectId id);

	Mono<ListaDeCompra> findByDescritor(String descritor);
	
	Flux<ListaDeCompra> findByUser(Users user);

	Mono<ListaDeCompra> findByDescritorAndUser(String descritor, Users user);

}
