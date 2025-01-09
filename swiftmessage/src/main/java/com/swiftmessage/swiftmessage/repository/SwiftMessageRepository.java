package com.swiftmessage.swiftmessage.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;



public interface SwiftMessageRepository extends JpaRepository<TblIncomingRecord, BigDecimal> {
    public List<TblIncomingRecord> findByReference(String ref);

    public List<TblIncomingRecord> findByCreatedBy(String createdBy);

    
}