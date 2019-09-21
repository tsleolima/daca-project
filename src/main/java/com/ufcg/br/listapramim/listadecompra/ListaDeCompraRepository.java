package com.ufcg.br.listapramim.listadecompra;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface ListaDeCompraRepository extends MongoRepository<ListaDeCompra,ObjectId>{
	
	ListaDeCompra findListaBy_id(ObjectId id);

	ListaDeCompra findProdutoByDescritor(String descritor);
	
	List<ListaDeCompra> findListaByUser(ObjectId id);

}
