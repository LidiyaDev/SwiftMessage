package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.repository.TblCountryOfOriginRepository;
import com.swiftmessage.swiftmessage.entity.TblCountryOfOrigin;

@Service
public class TblCountryOfOriginService {
    @Autowired
    TblCountryOfOriginRepository countryJPA;


    public TblCountryOfOrigin saveNew(TblCountryOfOrigin country)
    {
       return countryJPA.save(country);
    }


    public List<TblCountryOfOrigin> findAll() {
        return countryJPA.findAll();
    }
    public void delete(TblCountryOfOrigin country)
    {
        countryJPA.deleteAll();
    }
}