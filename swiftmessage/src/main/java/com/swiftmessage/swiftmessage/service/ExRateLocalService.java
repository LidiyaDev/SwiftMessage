package com.swiftmessage.swiftmessage.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.annotations.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.entity.ExRateLocal;
import com.swiftmessage.swiftmessage.repository.ExRateLocalRepository;

@Service
public class ExRateLocalService {

    @Autowired
    ExRateLocalRepository exRateLocalRepository;

    public void saveLocal(ExRateLocal localDate) {
        exRateLocalRepository.save(localDate);
    }

    public void saveList(List<ExRateLocal> listData) {
        exRateLocalRepository.saveAll(listData);
    }

    public List<ExRateLocal> findbyDateCcy(String ccy, String date) {
        System.out.println("Date+++++++++" + date);
        System.out.println("ccy+++++++++" + ccy);
        List<ExRateLocal> fetchedData = exRateLocalRepository.findByPreference(ccy, date);
        System.out.println("ccyfetchedData" + fetchedData);
        if (fetchedData == null || fetchedData.size() == 0) {
            for (int i = 1; i <= 3; i++) {

                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MMM-yy");

                LocalDate currentDate = LocalDate.parse(date, formatter2);
                currentDate = currentDate.plusDays(-i);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
                String formatedDate = currentDate.format(formatter);
                System.out.println("formated DAte" + formatedDate);
               
                    fetchedData = exRateLocalRepository.findByPreference(ccy, formatedDate);
                    System.out.println();
                    if (fetchedData.isEmpty()) {
                        System.out.println("Here now");
                        // System.out.println( "fetccccccccccc"+fetchedData.get(0).getRATE_DATE());
                        continue;
                    }
                    else
                    {
                        break;
                    }
                    
                
            }
        }
        return fetchedData;
    }

}
