package com.ufcg.br.listapramim.produto;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.ufcg.br.listapramim.usuario.Users;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProdutoRepository extends ReactiveMongoRepository<Produto, ObjectId> {
	
	Mono<Produto> findBy_id(ObjectId _id);

	Flux<Produto> findByUser(Users user);
	
	Mono<Produto> findByNomeAndUser(String nome,Users user);
	
}
