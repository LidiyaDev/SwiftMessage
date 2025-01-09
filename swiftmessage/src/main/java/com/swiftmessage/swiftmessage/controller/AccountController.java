package com.swiftmessage.swiftmessage.controller;

import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
//import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.SwiftMesage;
import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.MessageRestClient;


@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Named("accountController")
public class AccountController {


    // private static final String baseUrl2 = "http://localhost:6070/getByAccountNo/";
    // private WebClient webclient2 = WebClient.create(baseUrl2);
    // AccountRestClient accountRestClient = new AccountRestClient(webclient2);

    @Autowired
    AccountRestClient accountRestClient;
   


    private String accountNumber;


    


    private String AC_DESC;
    private String CUST_AC_NO;
    private String BRANCH_CODE;
    private String ACCOUNT_CLASS;
    private String CCY;
    private String CUST_NO;


    private boolean viewrnd = false;

    

    public String getAC_DESC() {
        return AC_DESC;
    }

    public boolean isViewrnd() {
        return viewrnd;
    }

    public void setViewrnd(boolean viewrnd) {
        this.viewrnd = viewrnd;
    }

    public void setAC_DESC(String aC_DESC) {
        AC_DESC = aC_DESC;
    }

    public String getCUST_AC_NO() {
        return CUST_AC_NO;
    }

    public void setCUST_AC_NO(String cUST_AC_NO) {
        CUST_AC_NO = cUST_AC_NO;
    }

    public String getBRANCH_CODE() {
        return BRANCH_CODE;
    }

    public void setBRANCH_CODE(String bRANCH_CODE) {
        BRANCH_CODE = bRANCH_CODE;
    }

    public String getACCOUNT_CLASS() {
        return ACCOUNT_CLASS;
    }

    public void setACCOUNT_CLASS(String aCCOUNT_CLASS) {
        ACCOUNT_CLASS = aCCOUNT_CLASS;
    }

    public String getCCY() {
        return CCY;
    }

    public void setCCY(String cCY) {
        CCY = cCY;
    }

    public String getCUST_NO() {
        return CUST_NO;
    }

    public void setCUST_NO(String cUST_NO) {
        CUST_NO = cUST_NO;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void retriveAccount(String account)
    { 
        System.out.println(account +"Account");
      AccountInfo accountDto = accountRestClient.getAccountInfo(account);

      viewrnd = true;
      System.out.println(accountDto.getAC_DESC() + "  Account Info List");

        AC_DESC = accountDto.getAC_DESC() ;
        CUST_AC_NO = accountDto.getCUST_AC_NO();
        BRANCH_CODE = accountDto.getBRANCH_CODE();
        ACCOUNT_CLASS = accountDto.getACCOUNT_CLASS() ;
        CCY = accountDto.getCCY();
        CUST_NO = accountDto.getCUST_NO();

    }


   
    

}
