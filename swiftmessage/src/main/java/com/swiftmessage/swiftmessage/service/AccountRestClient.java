package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.Customer;
import com.swiftmessage.swiftmessage.dto.DistrictInfoDto;


public class AccountRestClient {
    private WebClient webclient;

    

    public AccountRestClient(WebClient webclient) {
        this.webclient = webclient;
    }


    public DistrictInfoDto getDistrictName(String branch){
       


        String GET_EMPLOYEE_BY_ID = "http://192.168.12.47:8254/getDistricInfo/"+branch;
      return  webclient.get().uri(GET_EMPLOYEE_BY_ID, branch)
                .retrieve()
                .bodyToMono(DistrictInfoDto.class)
                .block();

    }

    public AccountInfo retriveEmployeeById(String accno){
       


        String GET_EMPLOYEE_BY_ID = "http://192.168.12.47:8254/getByAccountNo/"+accno;
      return  webclient.get().uri(GET_EMPLOYEE_BY_ID, accno)
                .retrieve()
                .bodyToMono(AccountInfo.class)
                .block();

    }

    public AccountInfo getAccountInfo(String accountNumber) {

        webclient = WebClient.builder()

                .build();

                AccountInfo model = webclient.get()

                .uri("http://192.168.12.47:8254/getByAccountNo/" + accountNumber)

                .retrieve()

                .bodyToMono(AccountInfo.class).block();

        return model;



    }

    public List<AccountInfo> getOtherByAccountNo(String accountNumber){
       


        String GET_EMPLOYEE_BY_ID = "http://192.168.12.47:8254/getOtherByAccountNo/"+accountNumber;

        System.out.println(GET_EMPLOYEE_BY_ID);

        List<AccountInfo> listAccount = webclient.get().uri(GET_EMPLOYEE_BY_ID, accountNumber)
        .retrieve()
        .bodyToFlux(AccountInfo.class)
                .collectList()
                .block();

                return listAccount;
       

    }


     public List<AccountInfo> getOtherByAccountName(String name){
       


        String GET_EMPLOYEE_BY_ID = "http://192.168.12.47:8254/getOtherByAccountName/"+ name;

        System.out.println(GET_EMPLOYEE_BY_ID);

        List<AccountInfo> listAccount = webclient.get().uri(GET_EMPLOYEE_BY_ID, name)
        .retrieve()
        .bodyToFlux(AccountInfo.class)
                .collectList()
                .block();

                return listAccount;
       

    }
    public Customer getCustomeCategory(String customerNumber) {

        webclient = WebClient.builder()

                .build();

                Customer model = webclient.get()

                .uri("http://192.168.12.47:8254/getCustomerCategory/" + customerNumber)

                .retrieve()

                .bodyToMono(Customer.class).block();

        return model;



    }
    
}
