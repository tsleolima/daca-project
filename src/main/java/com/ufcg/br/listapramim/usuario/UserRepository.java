package com.ufcg.br.listapramim.usuario;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {

    Users findByEmail(String email);
}