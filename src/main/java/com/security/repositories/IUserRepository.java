package com.security.repositories;

import com.security.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<UserEntity, Long>{

    // JPA con el nombre ya sabe que buscar
    Optional<UserEntity>findByUsername(String username);

    // Usando Query de JPQL
    //@Query("select u from UserEntity where u.username = ?1") // ?1 indica el primer parametro
    //Optional<UserEntity>findName(String username);
}
