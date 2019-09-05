package com.ufcg.br.listapramim.produto;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto,ObjectId>{
	
	Produto findProdutoBy_id(ObjectId _id);

	Produto findProdutoByNome(String nome);
	
}
