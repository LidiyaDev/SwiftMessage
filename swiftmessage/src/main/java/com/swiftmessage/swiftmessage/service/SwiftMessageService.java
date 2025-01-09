package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.repository.SwiftMessageRepository;

@Service
public class SwiftMessageService {
    @Autowired
   static
    SwiftMessageRepository swiftmessageJPA;

    public TblIncomingRecord save(TblIncomingRecord swiftmessage)
    {
       return swiftmessageJPA.save(swiftmessage);
    }


    //public TblIncomingRecord saveDouble(SwiftMesage swiftmessage, AccountInfo accountinfo)
    //{
      //TblIncomingRecord incomingMessage 
      // return swiftmessageJPA.save(swiftmessage);
   // }

    

    public List<TblIncomingRecord> findAll() {

       return swiftmessageJPA.findAll();

       

    }

    public List<TblIncomingRecord> findByReference(String reference)
    {

           return swiftmessageJPA.findByReference(reference);
    }

   
    
}