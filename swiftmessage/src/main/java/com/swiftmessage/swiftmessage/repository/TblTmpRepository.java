package com.swiftmessage.swiftmessage.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.entity.TblTmp;

public interface TblTmpRepository extends JpaRepository<TblTmp, BigDecimal> {
    
    List<TblTmp> findLastRecord();

   public List<TblTmp> findByReference(String reference);

   public List<TblTmp> findByCreatedBy(String createdBy);
}
