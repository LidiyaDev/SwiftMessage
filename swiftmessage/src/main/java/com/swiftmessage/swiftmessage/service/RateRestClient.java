package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.Rates;


public class RateRestClient {
    private WebClient webclient;

    
    public RateRestClient(WebClient webclient) {
        this.webclient = webclient;
    }


    public List<Rates> retriveRatesByDateAndCy(String date,String ccy){
       


        String GET_EMPLOYEE_BY_ID = "http://192.168.12.47:8254/getByDateAndCy/"+date+"/"+ccy;

        System.out.println(GET_EMPLOYEE_BY_ID);

        List<Rates> listRates = webclient.get().uri(GET_EMPLOYEE_BY_ID, date,ccy)
        .retrieve()
        .bodyToFlux(Rates.class)
                .collectList()
                .block();

                return listRates;
       

    }

    public List<Rates> retriveRatesByDateAndCyHis(String date,String ccy){
       


        String GET_EMPLOYEE_BY_ID = "http://192.168.12.47:8254/getByDateAndCyHis/"+date+"/"+ccy;

        System.out.println(GET_EMPLOYEE_BY_ID);

        List<Rates> listRates = webclient.get().uri(GET_EMPLOYEE_BY_ID, date,ccy)
        .retrieve()
        .bodyToFlux(Rates.class)
                .collectList()
                .block();

                return listRates;
       

    }


    
}