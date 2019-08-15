package com.ufcg.br.listapramim.repository;

import org.springframework.stereotype.Repository;

import com.ufcg.br.listapramim.model.ListaDeCompra;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface ListaDeCompraRepository extends MongoRepository<ListaDeCompra,ObjectId>{
	
	ListaDeCompra findListaBy_id(ObjectId id);

}
