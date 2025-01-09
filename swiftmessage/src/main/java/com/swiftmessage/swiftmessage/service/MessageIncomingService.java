package com.swiftmessage.swiftmessage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;

import java.math.BigDecimal;
import java.util.List;

import com.swiftmessage.swiftmessage.repository.MessageIncomingRepository;

@Service
public class MessageIncomingService {
    @Autowired
    MessageIncomingRepository incomingJPA;

    public List<TblIncomingRecord> findAll()
    {
        return incomingJPA.findAll();
    }

    public TblIncomingRecord save(TblIncomingRecord t)
    {
        return incomingJPA.save(t);
    }

    public void deletedata (BigDecimal id)
    {
        incomingJPA.deleteById(id);
    }

    public List<TblIncomingRecord> findLast()
    {
        return incomingJPA.findLastRecord();
    }
    public List<TblIncomingRecord> findByReference(String reference)
    {

           return incomingJPA.findByReference(reference);
    }

    public List<TblIncomingRecord> findByCreatedBy(String createdBy)
    {
        return incomingJPA.findByCreatedBy(createdBy);
    }

    public List<Object[]> findTopBranch()
    {
        return incomingJPA.findByTopBranch();
    }
    public List<TblIncomingRecord> findOtherBank()
    {
        return incomingJPA.findOtherBankTotal();
    }

    public List<Object[]> findCurrencyAmount()
    {
        return incomingJPA.findCurrencyAmount();
    }

    public List<Object[]> findAccountType()
    {
        return incomingJPA.findAccountType();
    }
}