package com.swiftmessage.swiftmessage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.AccountInfoModel;






@Service
public class AccountInfoService {

    private WebClient client;
    String url = "http://192.168.12.47:8383/accountDetail/model/";

    public AccountInfoModel getAccountInfo(String accountNumber) {
        client = WebClient.builder()
                .build();
        AccountInfoModel model = client.get()
                .uri(url + accountNumber)
                .retrieve()
                .bodyToMono(AccountInfoModel.class).block();
        return model;

    }

}
