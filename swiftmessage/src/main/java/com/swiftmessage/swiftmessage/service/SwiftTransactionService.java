package com.swiftmessage.swiftmessage.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.math.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.dto.TransactionReport;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.repository.SwiftTransactionRepository;
import com.swiftmessage.swiftmessage.repository.TransactionReportRepository;

@Service
public class SwiftTransactionService {
    

    @Autowired
    SwiftTransactionRepository swiftTransactinJpa;

   


    public SwiftTransaction save(SwiftTransaction swifttransaction)
    {
       return swiftTransactinJpa.save(swifttransaction);
    }


    //public TblIncomingRecord saveDouble(SwiftMesage swiftmessage, AccountInfo accountinfo)
    //{
      //TblIncomingRecord incomingMessage 
      // return swiftmessageJPA.save(swiftmessage);
   // }

    

    public List<SwiftTransaction> findAll() {

       return swiftTransactinJpa.findAll();
       

    }

    public SwiftTransaction findById(BigDecimal id) {

      return swiftTransactinJpa.getById(id);
      

   }

   public List<SwiftTransaction> findByStatus(String status)
   {
      return swiftTransactinJpa.findByStatus(status);
   }

   public List<SwiftTransaction> findByAccountType()
   {
      return swiftTransactinJpa.findByMessageStatus();
   }

   public List<SwiftTransaction> findByAccountTypePending()
   {
      return swiftTransactinJpa.findByMessageStatusPending();
   }

   public List<SwiftTransaction> findByStatusNreta()
   {
      return swiftTransactinJpa.findByMessageStatusNreta();
   }

   public List<SwiftTransaction> findByStatusNretaPending()
   {
      return swiftTransactinJpa.findByMessageStatusNretaPending();
   }

   public List<SwiftTransaction> findByCreatedBy(String createdBy)
   {
      return swiftTransactinJpa.findByCreatedBy(createdBy);
   }

   
   public List<SwiftTransaction> findByBnDates(Date start,Date end)
   {
      return swiftTransactinJpa.findByBnDate(start,end);
   }

   public List<SwiftTransaction> findByBnDatesReta(Date start,Date end)
   {
      return swiftTransactinJpa.findByBnDateReta(start,end);
   }

   public List<SwiftTransaction> findByDateAll(Date start,Date end)
   {
      return swiftTransactinJpa.findByBnDateAll(start, end);
   }

   public List<SwiftTransaction> findByBnDateSingle(Date end)
   {
      System.out.println("Date Given   "+end);
      return swiftTransactinJpa.findByBnDateSingle(end);
   }


   public List<SwiftTransaction> findByCategory(String category)
   {
      return swiftTransactinJpa.findByCategory(category);
   }


   public List<SwiftTransaction> findByAccountClass(String customerType)
   {
      return swiftTransactinJpa.findByAccountClass(customerType);
   }

   public List<SwiftTransaction> findByBranch(String dashenBranch)
   {
      return swiftTransactinJpa.findByAccountClass(dashenBranch);
   }

   public List<SwiftTransaction> findByReference (String reference)
   {
      return swiftTransactinJpa.findByMessageReference(reference);
   }
	
   



   public SwiftTransaction update(SwiftTransaction swiftTrans) {
      SwiftTransaction sh = swiftTransactinJpa.findById(swiftTrans.getId()).orElse(null);
      sh = swiftTrans;
      return swiftTransactinJpa.save(sh);

  }
}