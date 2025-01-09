package com.swiftmessage.swiftmessage.repository;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swiftmessage.swiftmessage.entity.TblCorrespondentBank;



public interface TblCorrespondentBankRepository extends JpaRepository<TblCorrespondentBank, BigDecimal> {
    List<TblCorrespondentBank> findByCode(String code);



   List<TblCorrespondentBank> findByCodeandname(String code);
    List < TblCorrespondentBank > findByLikeCode(String bank,String currency);
    List<TblCorrespondentBank> findByName(String name);
}