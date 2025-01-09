package com.swiftmessage.swiftmessage.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swiftmessage.swiftmessage.entity.TblCountryOfOrigin;

public interface TblCountryOfOriginRepository extends JpaRepository<TblCountryOfOrigin, BigDecimal> {
    
}