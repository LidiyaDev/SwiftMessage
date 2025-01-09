package com.swiftmessage.swiftmessage.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;

public interface MessageIncomingRepository extends JpaRepository<TblIncomingRecord, BigDecimal> {

   List<TblIncomingRecord> findLastRecord();

   public List<TblIncomingRecord> findByReference(String reference);

   public List<TblIncomingRecord> findByCreatedBy(String createdBy);

   List<TblIncomingRecord> findOtherBankTotal();

   @Query(value = "SELECT SUM(fcy_amount), dashen_branch\r\n" + //
         "FROM tbl_incoming_record\r\n" + //
         "GROUP BY dashen_branch\r\n" + //
         "ORDER BY SUM(fcy_amount) DESC\r\n" + //
         "FETCH FIRST 4 ROWS ONLY", nativeQuery = true)
   List<Object[]> findByTopBranch();

   @Query(value = "SELECT TBL_INCOMING_RECORD.CURRENCY_ID,\r\n" + //
         "SUM(TBL_INCOMING_RECORD.FCY_AMOUNT)\r\n" + //
         "FROM TBL_INCOMING_RECORD\r\n" + //
         "group by TBL_INCOMING_RECORD.CURRENCY_ID", nativeQuery = true)
   List<Object[]> findCurrencyAmount();

   @Query(value = "SELECT TBL_INCOMING_RECORD.ACCOUNT_TYPE,\r\n" + //
         "SUM(TBL_INCOMING_RECORD.FCY_AMOUNT)\r\n" + //
         "FROM TBL_INCOMING_RECORD\r\n" + //
         "group by TBL_INCOMING_RECORD.ACCOUNT_TYPE", nativeQuery = true)
   List<Object[]> findAccountType();

}