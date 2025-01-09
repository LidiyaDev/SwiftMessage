package com.swiftmessage.swiftmessage.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swiftmessage.swiftmessage.dto.TransactionReport;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;

public interface SwiftTransactionRepository extends JpaRepository<SwiftTransaction, BigDecimal> {

    

    public Optional<SwiftTransaction> findById(BigDecimal id);

    List<SwiftTransaction> findByMessageStatus();

    List<SwiftTransaction> findByMessageStatusPending();

    List<SwiftTransaction> findByMessageStatusNreta();

    List<SwiftTransaction> findByMessageStatusNretaPending();

    List<SwiftTransaction> findByStatus(String status);

    List<SwiftTransaction> findByBnDate(Date start,Date end);


    List<SwiftTransaction> findByBnDateAll(Date start,Date end);

    List<SwiftTransaction> findByBnDateSingle(Date end);


    List<SwiftTransaction> findByBnDateReta(Date start,Date end);

    List<SwiftTransaction> findByCreatedBy(String createdBy);
    

    List<SwiftTransaction> findByCategory(String customerType);

    List<SwiftTransaction> findByAccountClass(String accountType);

    List<SwiftTransaction> findByBranch(String dashenBranch);

    List<SwiftTransaction> findByMessageReference(String reference);

    
}