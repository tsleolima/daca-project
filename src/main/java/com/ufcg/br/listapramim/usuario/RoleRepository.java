package com.ufcg.br.listapramim.usuario;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role,ObjectId> {
	
    Role findByRole(String role);

}
