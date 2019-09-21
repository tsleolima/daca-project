package com.ufcg.br.listapramim.usuario;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, ObjectId> {

    Users findByEmail(String email);
}