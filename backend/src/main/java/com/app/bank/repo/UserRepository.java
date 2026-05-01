package com.app.bank.repo;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bank.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    @Query(fields = "{'userID': 1, 'password': 1}")
    Optional<User> findByUserID(String userID);

    // @Query(fields = "{'userID': 1, 'password': 1, 'accounts': 1}")
    Optional<User> findWithAccountsByUserID(String userID);
}
