package com.swiftmessage.swiftmessage.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swiftmessage.swiftmessage.entity.FetchedData;
import java.util.List;



public interface FetchedDataRepository extends JpaRepository<FetchedData, BigDecimal> {
    
    public List<FetchedData> findByReference(String reference);
}
