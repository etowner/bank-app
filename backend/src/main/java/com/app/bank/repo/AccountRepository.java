package com.app.bank.repo;

import com.app.bank.model.Account;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, ObjectId> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUsername(String username);

    void deleteAllByUsername(String username);

}
