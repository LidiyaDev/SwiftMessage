package com.swiftmessage.swiftmessage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.SwiftMesage;

@Service
public class MessageRestClient {
    private WebClient webclient;

    

    public SwiftMesage getMessageInfo(String referenceNumber) {

        webclient = WebClient.builder()

                .build();

                SwiftMesage model = webclient.get()

                .uri("http://192.168.12.47:9998/getByRefference/" + referenceNumber)

                .retrieve()

                .bodyToMono(SwiftMesage.class).block();

        return model;



    }
}