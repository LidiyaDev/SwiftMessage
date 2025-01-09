package com.swiftmessage.swiftmessage.controller;

import java.util.List;

import javax.annotation.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.service.SwiftTransactionService;

@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AdviceForTransaction {
    @Autowired
    SwiftTransactionService swifttransactionservice;

    private SwiftTransaction items;

    

    @RequestMapping("/Transaction/advice")
    public String newProfile() {
       

        return "forward:/Transaction/TransactionListForAdvice.xhtml";
    }


    public void prepareSelect(SwiftTransaction item) {
        if (item != null) {

            System.out.println("test item" + item);
            items = item;
        }

    }





















   

    /**
     * @return SwiftTransaction return the items
     */
    public SwiftTransaction getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(SwiftTransaction items) {
        this.items = items;
    }

}