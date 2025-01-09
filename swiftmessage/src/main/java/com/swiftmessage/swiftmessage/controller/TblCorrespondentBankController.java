package com.swiftmessage.swiftmessage.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.AccountInfoModel;
import com.swiftmessage.swiftmessage.entity.TblCorrespondentBank;
import com.swiftmessage.swiftmessage.service.AccountInfoService;
import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.TblCorrespondentBankService;

@Controller
@ManagedBean
@ViewScoped

public class TblCorrespondentBankController {

     private static final String baseUrl2 = "http://localhost:6070/getByAccountNo/";
    private WebClient webclient2 = WebClient.create(baseUrl2);
    AccountRestClient accountRestClient = new AccountRestClient(webclient2);

    @Autowired
    TblCorrespondentBankService correspondentService;

    @Autowired
    AccountInfoService accountInfoService;

    private TblCorrespondentBank selected;
    private List<TblCorrespondentBank> items;

    private String bname;
    private String bcide;
    private String bremark;
    private String bbaknacc;
private String name;
private String branch;
private String branchCode;
private String currencyacc;


    public TblCorrespondentBankController() {

    }

    @PostConstruct
    public void init() {
        // In case you're updating an existing entity.

        // Or in case you want to create a new entity.
        selected = new TblCorrespondentBank();
    }

    public void prepareSelect(TblCorrespondentBank item) {
        if (item != null) {

            System.out.println("test item" +item);
            selected = item;
        }

    }


     public void hello() {
        System.out.println("----------------------------------------------" + bbaknacc);
        // account = accountNumber;
        try {
           
            AccountInfo accountDto = accountRestClient.getAccountInfo(bbaknacc);
            if (!accountDto.getAC_DESC().equalsIgnoreCase("")) {
                name = accountDto.getAC_DESC();
                branch = accountDto.getBRANCH_CODE();
                
               
                currencyacc = accountDto.getCCY();
                

            } else { 
                
                

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

    

    public String getBbaknacc() {
        return bbaknacc;
    }

    public void setBbaknacc(String bbaknacc) {
        this.bbaknacc = bbaknacc;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBcide() {
        return bcide;
    }

    public void setBcide(String bcide) {
        this.bcide = bcide;
    }

    public String getBremark() {
        return bremark;
    }

    public void setBremark(String bremark) {
        this.bremark = bremark;
    }

    public TblCorrespondentBank getSelected() {
        return selected;
    }

    public void setSelected(TblCorrespondentBank selected) {
        this.selected = selected;
    }

    public List<TblCorrespondentBank> getItems() {
        items = correspondentService.findAll();
        return items;
    }

    public void setItems(List<TblCorrespondentBank> items) {
        this.items = items;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getCurrencyacc() {
        return currencyacc;
    }

    public void setCurrencyacc(String currencyacc) {
        this.currencyacc = currencyacc;
    }

    @RequestMapping("/Bank/View")
    public String list() {
        items = correspondentService.findAll();
        selected = new TblCorrespondentBank();
        System.out.println(selected + "Data");
        return "forward:/Bank/Bankview.xhtml";
    }

    @RequestMapping("/Bank/new")
    public String newBank() {
        selected = new TblCorrespondentBank();
        int some = 12;
        selected.setCreatedBy("Lidiyam");
        System.out.println("lidu ");
        return "forward:/Bank/NewBank.xhtml";
    }

    @RequestMapping("/Bank/edit")
    public String editProfile(@RequestParam BigDecimal id) {
        // BigInteger
        selected = correspondentService.findbyID(id);
        // selected.setTinno("asdfasdf");

        return "forward:/Bank/edit.xhtml";
    }

    public String save() {
        System.out.println("test ");
        selected = new TblCorrespondentBank();
        System.out.println("lidu " + selected);
        if (selected != null) {
            selected.setCode(bcide);
            selected.setName(bname);
            selected.setRemark(bremark);
            selected.setBankAccount(bbaknacc);
            selected.setCreatedBy("Lidiyam");
            selected.setCurrency(currencyacc);
            System.out.println("calue " + selected);
            int some = 12;

            correspondentService.saveNew(selected);
            items = correspondentService.findAll();

            System.out.println("saved");

            return "redirect:/Bank/View";

        } else
            System.out.println("error");

        return "redirect:/Bank/View";

    }

    public String update() {
        if (selected != null) {
            // System.out.println("calue " + selected);
            correspondentService.update(selected);
            items = correspondentService.findAll();
            System.out.println("updated");
            return "forward:/Bank/View";
        } else
            System.out.println("error");
        return "forward:/Bank/View";
 
    }    
  
    public void delete(TblCorrespondentBank delselected) {
        System.out.println("delete" + delselected);
        correspondentService.delete(delselected);
        

    }

    

}