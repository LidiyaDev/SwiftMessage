package com.swiftmessage.swiftmessage.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.RTRequest;
import com.swiftmessage.swiftmessage.dto.RTResponse;
import com.swiftmessage.swiftmessage.dto.Request;
import com.swiftmessage.swiftmessage.dto.Response;

import reactor.core.publisher.Mono;

@Service
public class PaymentRestClient {

  
    private WebClient webclient; 



    public Response autorizeTransaction(Request request) {
        System.out.println("test7");
        webclient = WebClient.builder().build();
        try {
            System.out.println("test1");

            String url = "http://192.168.12.47:8085/cbsservice/transaction";
            return WebClient.create()
                    .post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    // .body(BodyInserters.fromObject(request))
                    .body(Mono.just(request), Request.class)
                    .exchange()
                    .block()
                    .bodyToMono(Response.class)
                    .block();

        } catch (Exception ex) {
            System.out.println("error   " + ex);
            return new Response();
        }
    }

    public RTResponse fincTransaction(RTRequest request) {
        System.out.println("test7");
        webclient = WebClient.builder().build();
        try {
            System.out.println("test1");

            String url = "http://192.168.12.47:6070/cbsservice/transaction/";
            return WebClient.create()
                    .post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    // .body(BodyInserters.fromObject(request))
                    .body(Mono.just(request), RTRequest.class)
                    .exchange()
                    .block()
                    .bodyToMono(RTResponse.class)
                    .block();

        } catch (Exception ex) {
            System.out.println("error   " + ex);
            return new RTResponse();
        }
    }
}