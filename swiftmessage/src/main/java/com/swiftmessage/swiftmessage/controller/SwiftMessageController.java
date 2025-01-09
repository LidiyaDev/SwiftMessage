package com.swiftmessage.swiftmessage.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.service.SwiftMessageService;


@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SwiftMessageController {
    @Autowired
    SwiftMessageService swiftmessagerService;
   

    private TblIncomingRecord selected;
    private List<TblIncomingRecord> items;

    private String account = null;

    public SwiftMessageController() {
    }

    

          

    public void save() {
        if (selected != null) {
            System.out.println("calue " + selected);
            int some = 12;
            selected.setCreatedBy("Lidiyam");
            selected.setRegistrationDate(new Date());
            swiftmessagerService.save(selected);
            System.out.println("saved");
        } else
            System.out.println("error");

    }


    public void searchAccount() {
       
            // System.out.println("calue " + selected);
            //int some = 12;
            //selected.setCreatedBy(BigInteger.valueOf(some));
           // selected.setRegistrationDate(new Date());
            //swiftmessagerService.save(selected);
           // accountinfoservice.connect();
            System.out.println("searchAccount " + account);


    }

    public TblIncomingRecord getSelected() {
        return selected;
    }

    public void setSelected(TblIncomingRecord selected) {
        this.selected = selected;
    }

    public List<TblIncomingRecord> getItems() {
        return items;
    }

    public void setItems(List<TblIncomingRecord> items) {
        this.items = items;
    } 

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    

    
}
