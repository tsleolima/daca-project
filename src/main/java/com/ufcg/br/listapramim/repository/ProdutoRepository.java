package com.ufcg.br.listapramim.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ufcg.br.listapramim.model.Produto;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto,Long>{
	
}
