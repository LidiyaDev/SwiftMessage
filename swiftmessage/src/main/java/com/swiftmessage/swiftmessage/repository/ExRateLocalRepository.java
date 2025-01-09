package com.swiftmessage.swiftmessage.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swiftmessage.swiftmessage.entity.ExRateLocal;




public interface ExRateLocalRepository extends JpaRepository<ExRateLocal, Long> {
    
      @Query(value ="select * from history_rate where ccy1 =?1 and rate_date =?2",nativeQuery = true)
      List<ExRateLocal> findByPreference(String ccy,String date);
}
