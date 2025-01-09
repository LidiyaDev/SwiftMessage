package com.swiftmessage.swiftmessage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.service.SwiftTransactionService;

import org.springframework.context.annotation.ScopedProxyMode;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Controller
public class GeneralReportController {

    @Autowired
    SwiftTransactionService swifttransactionservice;

    private List<SwiftTransaction> nretaitems;

    private String reportBy;
    private String branchCode;
    private String category;
    private String account;

    private boolean branchrnd = false;
    private boolean accountrnd = false;
    private boolean categorrnd = false;
    private boolean daternd = false;

    private Date from;
    private Date to;

    public void requestByreportBy() {

        if (reportBy.equalsIgnoreCase("All")) {
            branchrnd = false;
            accountrnd = false;
            categorrnd = false;
            daternd = false;
        }

        if (reportBy.equalsIgnoreCase("Branch")) {
            branchrnd = true;
            accountrnd = false;
            categorrnd = false;
            daternd = false;
        }
        if (reportBy.equalsIgnoreCase("Customer Category")) {
            branchrnd = false;
            accountrnd = false;
            categorrnd = true;
            daternd = false;
        }
        if (reportBy.equalsIgnoreCase("Account Class")) {
            branchrnd = false;
            accountrnd = true;
            categorrnd = false;
            daternd = false;
        }

        if (reportBy.equalsIgnoreCase("Date")) {
            branchrnd = false;
            accountrnd = false;
            categorrnd = false;
            daternd = true;
        }
    }

    public void reportByDate() {
        System.out.println("test");

        System.out.println(from);
        nretaitems = swifttransactionservice.findByDateAll(from, to);

        System.out.println(nretaitems + "Items");
    }

    public void reportByAccount() {
        System.out.println("account  "+account);

            System.out.println(from);
            nretaitems = swifttransactionservice.findByAccountClass(account);

            System.out.println(nretaitems + "Items");
    }

    public void reportByCategory() {
        System.out.println("category   "+category); 

            System.out.println(from);
            nretaitems = swifttransactionservice.findByCategory(category);

            System.out.println(nretaitems + "Items");
    }

    public void reportByBranch() {
        System.out.println("test");

            System.out.println(from);
            nretaitems = swifttransactionservice.findByBranch(branchCode);

            System.out.println(nretaitems + "Items");
    }

    @RequestMapping("/Report/general")
    public String reportTableGeneral() {

        if (reportBy.equalsIgnoreCase("All")) {

        }

        if (reportBy.equalsIgnoreCase("Branch")) {
            System.out.println("test");

            System.out.println(from);
            nretaitems = new ArrayList<>();
            nretaitems = swifttransactionservice.findByBranch(branchCode);

            System.out.println(nretaitems + "Items");
        }
        if (reportBy.equalsIgnoreCase("Customer Category")) {
            System.out.println("category   "+category); 

            System.out.println(from);
            nretaitems = new ArrayList<>();
            nretaitems = swifttransactionservice.findByCategory(category);

            System.out.println(nretaitems + "Items");
        }
        if (reportBy.equalsIgnoreCase("Account Class")) {
            System.out.println("account  "+account);

            System.out.println(from);
            nretaitems = new ArrayList<>();
            nretaitems = swifttransactionservice.findByAccountClass(account);

            System.out.println(nretaitems + "Items");
        }

        if (reportBy.equalsIgnoreCase("Date")) {
            System.out.println("test");

            System.out.println(from);
            nretaitems = new ArrayList<>();
            nretaitems = swifttransactionservice.findByDateAll(from, to);

            System.out.println(nretaitems + "Items");
        }

        return "forward:/Report/SwiftInBirr.xhtml";
    }

    public List<SwiftTransaction> getNretaitems() {
        return nretaitems;
    }

    public void setNretaitems(List<SwiftTransaction> nretaitems) {
        this.nretaitems = nretaitems;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return String return the reportBy
     */
    public String getReportBy() {
        return reportBy;
    }

    /**
     * @param reportBy the reportBy to set
     */
    public void setReportBy(String reportBy) {
        this.reportBy = reportBy;
    }

    /**
     * @return boolean return the branchrnd
     */
    public boolean isBranchrnd() {
        return branchrnd;
    }

    /**
     * @param branchrnd the branchrnd to set
     */
    public void setBranchrnd(boolean branchrnd) {
        this.branchrnd = branchrnd;
    }

    /**
     * @return boolean return the accountrnd
     */
    public boolean isAccountrnd() {
        return accountrnd;
    }

    /**
     * @param accountrnd the accountrnd to set
     */
    public void setAccountrnd(boolean accountrnd) {
        this.accountrnd = accountrnd;
    }

    /**
     * @return boolean return the categorrnd
     */
    public boolean isCategorrnd() {
        return categorrnd;
    }

    /**
     * @param categorrnd the categorrnd to set
     */
    public void setCategorrnd(boolean categorrnd) {
        this.categorrnd = categorrnd;
    }

    /**
     * @return boolean return the daternd
     */
    public boolean isDaternd() {
        return daternd;
    }

    /**
     * @param daternd the daternd to set
     */
    public void setDaternd(boolean daternd) {
        this.daternd = daternd;
    }

    /**
     * @return Date return the from
     */
    public Date getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(Date from) {
        this.from = from;
    }

    /**
     * @return Date return the to
     */
    public Date getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(Date to) {
        this.to = to;
    }

    /**
     * @return String return the branchCode
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * @param branchCode the branchCode to set
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * @return String return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return String return the account
     */
    public String getAccount() {
        return account;
    }

}
