package com.swiftmessage.swiftmessage.repository;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swiftmessage.swiftmessage.dto.TransactionReport;

public interface TransactionReportRepository extends JpaRepository<TransactionReport, Long> {

    @Query(name = "findAllAccounts", nativeQuery = true)
  List<TransactionReport> getAllAccounts();
//public final static String GET_LOAN_REPORTS = "select (count(tbl_incoming_record.dashen_branch) AS Total,tbl_incoming_record.dashen_branch) from swift_transaction INNER JOIN tbl_incoming_record ON swift_transaction.message = tbl_incoming_record.id GROUP BY tbl_incoming_record.dashen_branch";

   // @Query(value ="select count(tbl_incoming_record.dashen_branch) AS Total,tbl_incoming_record.dashen_branch from swift_transaction INNER JOIN tbl_incoming_record ON swift_transaction.message = tbl_incoming_record.id GROUP BY tbl_incoming_record.dashen_branch",nativeQuery = true)
   // TransactionReport findByPreference();
}