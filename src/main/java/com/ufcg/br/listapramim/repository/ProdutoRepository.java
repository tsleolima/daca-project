package com.ufcg.br.listapramim.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ufcg.br.listapramim.model.Produto;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto,ObjectId>{
	
	Produto findProdutoBy_id(ObjectId _id);

	Produto findProdutoByNome(String nome);
	
}
