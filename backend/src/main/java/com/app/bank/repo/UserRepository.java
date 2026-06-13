package com.app.bank.repo;

import com.app.bank.model.User;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    // Finds a usersID and password for authentication purposes, excluding other fields for security
    @Query(fields = "{'username': 1, 'password': 1}")
    Optional<User> findByUsername(String username);


    Optional<User> findWithAccountsByUsername(String username);
}
