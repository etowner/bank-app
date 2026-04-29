package com.app.bank.repo;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.bank.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> getUserByUserID(String userID);

}
