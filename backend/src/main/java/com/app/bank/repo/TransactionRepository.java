package com.app.bank.repo;
import com.app.bank.model.Transaction;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {

    List<Transaction> findByAccountNumber(String accountNumber);

    void deleteAllByAccountNumber(String accountNumber);
    
}



