package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.entity.TblTmp;
import com.swiftmessage.swiftmessage.repository.MessageIncomingRepository;
import com.swiftmessage.swiftmessage.repository.TblTmpRepository;

@Service
public class TblTmpService {
    @Autowired
    TblTmpRepository incomingJPA;


    public List<TblTmp> findAll()
    {
        return incomingJPA.findAll();
    }

    public TblTmp save(TblTmp t)
    {
        return incomingJPA.save(t);
    }

    public List<TblTmp> findLast()
    {
        return incomingJPA.findLastRecord();
    }
    public List<TblTmp> findByReference(String reference)
    {

        System.out.println("reference======"+reference);
        System.out.println("reference data======"+incomingJPA.findByReference(reference));
           return incomingJPA.findByReference(reference);
    }

    public List<TblTmp> findByCreatedBy(String createdBy)
    {
        return incomingJPA.findByCreatedBy(createdBy);
    }

    
}
