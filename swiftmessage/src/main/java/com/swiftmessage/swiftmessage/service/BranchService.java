package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.Branch;

@Service
public class BranchService {

    private WebClient client;
    String url = "http://172.20.0.6:3123/branch/";

    public List<Branch> getBranchInfo() {

        client = WebClient.builder()
                .build();
        List<Branch> response = client.get()
                .uri(url)
                // .body(Mono.just(customerDetailReq), String.class)
                .retrieve()
                .bodyToFlux(Branch.class).collectList().block();
        // log.info(response.toString());
        return response;

    }

    public Branch requestTransactionBranchCheck(

            String transactionBranchCheckRequest) {

        

        client = WebClient.builder()

                .build();

        Branch paymentResponse = client.get()

                .uri("http://192.168.12.47:8383/accountDetail/branch/" + transactionBranchCheckRequest)

                .retrieve()

                .bodyToMono(Branch.class).block();

       

        return paymentResponse;

    }

}