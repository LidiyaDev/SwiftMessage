package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.entity.FetchedData;
import com.swiftmessage.swiftmessage.repository.FetchedDataRepository;



@Service
public class FetchedDataService {
    @Autowired
    FetchedDataRepository fetchedDataRepository;
    
    public void saveMessage(FetchedData data)
    {
        fetchedDataRepository.save(data);
    }

    public void deleteMessage()
    {
        fetchedDataRepository.deleteAll();
    }

    public List<FetchedData> byReference(String reference)
    {
        return fetchedDataRepository.findByReference(reference);
    }
}
