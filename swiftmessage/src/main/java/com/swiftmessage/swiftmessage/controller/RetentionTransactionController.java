package com.swiftmessage.swiftmessage.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.detDSA;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.Customer;
import com.swiftmessage.swiftmessage.dto.Rates;
import com.swiftmessage.swiftmessage.entity.ExRateLocal;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.entity.TblTmp;
import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.ExRateLocalService;
import com.swiftmessage.swiftmessage.service.MessageIncomingService;
import com.swiftmessage.swiftmessage.service.RateRestClient;
import com.swiftmessage.swiftmessage.service.SwiftTransactionService;
import com.swiftmessage.swiftmessage.service.TblTmpService;



@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RetentionTransactionController {

    @Autowired
    MessageIncomingService swiftmessagerService;

    @Autowired
    TblTmpService tblTmpService;

    @Autowired
    SwiftTransactionService swifttransactionservice;

    @Autowired
    FetchedDataController fetchedDataController;

    private static final String baseUrl2 = "http://192.168.12.47:6070/getOtherByAccountNo/";
    private WebClient webclient2 = WebClient.create(baseUrl2);
    AccountRestClient accountRestClient = new AccountRestClient(webclient2);

    private static final String baseUrl23 = "http://192.168.12.47:6070/getByAccountNo/";
    private WebClient webclient23 = WebClient.create(baseUrl23);
    AccountRestClient accountRestClient2 = new AccountRestClient(webclient23);

    private static final String baseUrl12 = "http://192.168.12.47:6070/getByDateAndCy/";
    private WebClient webclient12 = WebClient.create(baseUrl12);
    RateRestClient rateRestClient = new RateRestClient(webclient12);

    // @Autowired
    // AccountRestClient accountRestClient;

    // @Autowired
    // RateRestClient rateRestClient;

    private SwiftTransaction selectedTransaction;
    private List<SwiftTransaction> transactionitems;
    private SwiftTransaction newTransaction;
    private List<TblIncomingRecord> fromLastRecord = null;

    private List<AccountInfo> relatedAccountInfo = new ArrayList<>();

    List<AccountInfo> accountlist;
    private String etbaccount;
    private String AC_DESC;
    private String CUST_AC_NO;
    private String BRANCH_CODE;
    private String ACCOUNT_CLASS;
    private String ACCY;
    private String CUST_NO;

    private double fcyRate;
    private double etbRate;
    private double finalFcy;
    private double finalretafcy;
    private double finaletb;

    private Double finalRate = null;
    private Double fcy_exrate;
    private String referenceno;

    private String messAmount;
    private String messCurrency;
    private String messRate;
    private String messAccCurrency;
    private String messValueDate;

    public List<AccountInfo> getRelatedAccountInfo() {
        if (relatedAccountInfo == null) {
            relatedAccountInfo = new ArrayList<>();
        }
        return relatedAccountInfo;
    }

    public void setRelatedAccountInfo(List<AccountInfo> relatedAccountInfo) {
        this.relatedAccountInfo = relatedAccountInfo;
    }

    public String getMessAmount() {
        return messAmount;
    }

    public void setMessAmount(String messAmount) {
        this.messAmount = messAmount;
    }

    public String getMessCurrency() {
        return messCurrency;
    }

    public void setMessCurrency(String messCurrency) {
        this.messCurrency = messCurrency;
    }

    public String getMessRate() {
        return messRate;
    }

    public void setMessRate(String messRate) {
        this.messRate = messRate;
    }

    public String getMessAccCurrency() {
        return messAccCurrency;
    }

    public void setMessAccCurrency(String messAccCurrency) {
        this.messAccCurrency = messAccCurrency;
    }

    public String getMessValueDate() {
        return messValueDate;
    }

    public void setMessValueDate(String messValueDate) {
        this.messValueDate = messValueDate;
    }

    public List<TblIncomingRecord> getFromLastRecord() {
        return fromLastRecord;
    }

    public void setFromLastRecord(List<TblIncomingRecord> fromLastRecord) {
        this.fromLastRecord = fromLastRecord;
    }

    public String getReferenceno() {
        return referenceno;
    }

    public void setReferenceno(String referenceno) {
        this.referenceno = referenceno;
    }

    public Double getFcy_rate() {
        return fcy_exrate;
    }

    public void setFcy_rate(Double fcy_exrate) {
        this.fcy_exrate = fcy_exrate;
    }

    public Double getFinalRate() {
        return finalRate;
    }

    public void setFinalRate(Double finalRate) {
        this.finalRate = finalRate;
    }

    public SwiftTransaction getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedTransaction(SwiftTransaction selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    public List<SwiftTransaction> getTransactionitems() {
        return transactionitems;
    }

    public void setTransactionitems(List<SwiftTransaction> transactionitems) {
        this.transactionitems = transactionitems;
    }

    public SwiftTransaction getNewTransaction() {
        return newTransaction;
    }

    public void setNewTransaction(SwiftTransaction newTransaction) {
        this.newTransaction = newTransaction;
    }

    @PostConstruct
    public void init() throws ParseException {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        referenceno = paramMap.get("id");

        List<TblTmp> backData = tblTmpService.findByReference(referenceno);

            messAmount = backData.get(0).getFcyAmount().toString();
            messCurrency = backData.get(0).getCurrencyId();
            messRate = backData.get(0).getRate().toString();
            messAccCurrency = backData.get(0).getAccountNumberCurrency();
            messValueDate = backData.get(0).getValueDate().toString();
        sellRate = valueSellRate(messValueDate, messAccCurrency);
        finalFcy=0.0;
        finaletb=0.0;
        AC_DESC = new String();
        ACCY = new String();
        etbaccount = new String();
        System.out.println(referenceno + " Incoming Reffference Number");

       // List<TblTmp> backData = tblTmpService.findByReference(referenceno);
        // fromLastRecord = swiftmessagerService.findByReference(referenceno);

        String recieverAccount = backData.get(0).getBeneficiary();

       // String accCut = recieverAccount.substring(1, 10);
       // System.out.println(accCut + "Sub String");
        relatedAccountInfo = new ArrayList<>();
       // relatedAccountInfo = accountRestClient.getOtherByAccountName(accCut);
        System.out.println(relatedAccountInfo + "List");

    }

    public void clearMessage()
    {
        
    }
    double sellRate = 0.0;

    
    

    public double getSellRate() {

        return sellRate;
    }

    public void setSellRate(double sellRate) {
        this.sellRate = sellRate;
    }

    public double getFinalFcy() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

            referenceno = paramMap.get("id");

            System.out.println(referenceno + " Incoming Reffference Number");

            List<TblTmp> backData = tblTmpService.findByReference(referenceno);

            messAmount = backData.get(0).getFcyAmount().toString();
            messCurrency = backData.get(0).getCurrencyId();
            messRate = backData.get(0).getRate().toString();
            messAccCurrency = backData.get(0).getAccountNumberCurrency();
            messValueDate = backData.get(0).getValueDate().toString();

            double d = Double.parseDouble(messAmount);
            double rateExcha = Double.parseDouble(messRate);

            if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

                finalFcy = (fcyRate * d) / 100;
                finalRate = rateExcha;
                fcy_exrate = 1.0;
            } else {

                double changeAmount = (fcyRate * d) / 100;
                double tempAmount = changeAmount * rateExcha;

                sellRate = valueSellRate(messValueDate, messAccCurrency);

                finalFcy = tempAmount / sellRate;
                System.out.println("finalFcy---------"+finalFcy);

                fcy_exrate = finalFcy / tempAmount;
                finalRate = rateExcha;

            }

        } catch (Exception ex) {

        }
        return finalFcy;
    }

    @Autowired
    ExRateLocalService exRateLocalService;
    public Double valueSellRate(String ValueDate, String Currency) throws ParseException {
        System.out.println("First /here"+Currency);
        Double value = 0.0;
        String result = ValueDate.split(" ")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = formatter.parse(result);
        DateFormat df = new SimpleDateFormat("dd-MMM-yy");
        String dateToString = df.format(date1);

        List<Rates> rate = new ArrayList<>();
         List<ExRateLocal> localListRate = new ArrayList<>();

        rate = rateRestClient.retriveRatesByDateAndCy(dateToString,Currency);

        if (rate.size() == 0 || rate ==null) {
            System.out.println("here");
            localListRate = exRateLocalService.findbyDateCcy(Currency, dateToString);
            for (int i = 0; i < localListRate.size(); i++) {

                if (localListRate.get(i).getRATE_TYPE().equals("BANK_SEL")) {

                   // System.out.println("Local Rate ="+localListRate.get(i));
                    value = Double.valueOf(localListRate.get(i).getMID_RATE());

                }

            }

            //rate = rateRestClient.retriveRatesByDateAndCyHis(dateToString,Currency);

        }

        for (int i = 0; i < rate.size(); i++) {
            if (rate.get(i).getRATE_TYPE().equals("BANK_SEL")) {

                value = rate.get(i).getMID_RATE();
                System.out.println(value + "Sale rate of that day");
            }

        }

        return value;

    }

    public Double valueBuyRate(String ValueDate, String Currency) throws ParseException {

        System.out.println("NOW HERE");
        Double rateValue = 0.0;
        
        String result = ValueDate.split(" ")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = formatter.parse(result);
        DateFormat df = new SimpleDateFormat("dd-MMM-yy");
        String dateToString = df.format(date1);
        List<Rates> rate = new ArrayList<>();
        List<ExRateLocal> localListRate =new ArrayList<>();

        rate = rateRestClient.retriveRatesByDateAndCy(dateToString, Currency);

        if (rate.size() == 0 || rate == null) {

            System.out.println("11111111111111111111");
            localListRate = exRateLocalService.findbyDateCcy(Currency, dateToString);
            for (int i = 0; i < localListRate.size(); i++) {

                System.out.println("2222222222222222222222222");
                if (localListRate.get(i).getRATE_TYPE().equals("BANK_BUY")) {

                    rateValue = Double.valueOf(localListRate.get(i).getMID_RATE());

                }

            }

        }
        else{
            for (int i = 0; i < rate.size(); i++) {

                System.out.println("4444444444444444444444");
                if (rate.get(i).getRATE_TYPE().equals("BANK_BUY")) {

                    rateValue = rate.get(i).getMID_RATE();

                }

            }
        }

        return rateValue;

    }

    public void setFinalFcy(double finalFcy) {
        this.finalFcy = finalFcy;
    }

    public double getFinaletb() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

            String messAmount = paramMap.get("amount");
            String messCurrency = paramMap.get("messcu");
            String messRate = paramMap.get("rateAmount");
            String messAccCurrency = paramMap.get("accCurrency");
            String messValueDate = paramMap.get("valueDate");

            double d = Double.parseDouble(messAmount);
            double rateExcha = Double.parseDouble(messRate);

            if (messCurrency.equalsIgnoreCase("ETB")) {

                double tempAmount = (etbRate * d) / 100;
                finaletb = rateExcha * tempAmount;
                finalRate = rateExcha;
            }

        } catch (Exception ex) {

        }
        return finaletb;
    }

    public void setFinaletb(double finaletb) {
        this.finaletb = finaletb;
    }

    public double getFcyRate() {
        return fcyRate;
    }

    public void setFcyRate(double fcyRate) {
        this.fcyRate = fcyRate;
    }

    public double getEtbRate() {
        return etbRate;
    }

    public void setEtbRate(double etbRate) {
        this.etbRate = etbRate;
    }

    public String getAC_DESC() {
        return AC_DESC;
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

    public String getACCY() {
        return ACCY;
    }

    public void setACCY(String aCCY) {
        ACCY = aCCY;
    }

    public String getCUST_NO() {
        return CUST_NO;
    }

    public void setCUST_NO(String cUST_NO) {
        CUST_NO = cUST_NO;
    }

    public String getEtbaccount() {
        return etbaccount;
    }

    public void setEtbaccount(String etbaccount) {
        this.etbaccount = etbaccount;
    }

    public List<AccountInfo> getAccountlist(String name) {

        String accCut = name.substring(1, 10);
        System.out.println(accCut + "Sub String");
        relatedAccountInfo = new ArrayList<>();
       // relatedAccountInfo = accountRestClient.getOtherByAccountName(accCut);
        System.out.println(relatedAccountInfo + "List");
        return relatedAccountInfo;
    }

    public void setAccountlist(List<AccountInfo> accountlist) {
        this.accountlist = accountlist;
    }

    public void accountListData(String accNum) {

        String accCut = accNum.substring(1, 9);
        System.out.println(accCut + "Sub String");
        accountlist = accountRestClient.getOtherByAccountNo(accCut);
        System.out.println(accountlist + "List");
    }

    public void searchAcc() {
        System.out.println(etbaccount + "Account 123");
        AccountInfo accountDto = accountRestClient.getAccountInfo(etbaccount);

        if (accountDto != null) {
            System.out.println(accountDto.getAC_DESC() + "  Account Info List");

            AC_DESC = accountDto.getAC_DESC();
            CUST_AC_NO = accountDto.getCUST_AC_NO();
            BRANCH_CODE = accountDto.getBRANCH_CODE();
            ACCOUNT_CLASS = accountDto.getACCOUNT_CLASS();
            ACCY = accountDto.getCCY();
            CUST_NO = accountDto.getCUST_NO();
        }
    }

    public void calculateAmount() throws ParseException {

        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        referenceno = paramMap.get("id");

        System.out.println(referenceno + " Incoming Reference Number");

        List<TblTmp> backData = tblTmpService.findByReference(referenceno);

        // String messAmount = fromLastRecord.get(0).getFcyAmount().toString();
        // String messCurrency = fromLastRecord.get(0).getCurrencyId();
        // String messRate = fromLastRecord.get(0).getRate().toString();
        // String messAccCurrency = fromLastRecord.get(0).getAccountNumberCurrency();
        // String messValueDate = fromLastRecord.get(0).getValueDate().toString();
        etbRate = 100 - fcyRate;
        double d = Double.parseDouble(messAmount);
        double exrate = Double.parseDouble(messRate);
        DecimalFormat df = new DecimalFormat("0.00");

        double input = 1205.6358;

        System.out.println("salary : " + input);

        System.out.println("salary : " + df.format(input));

        if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

            double etbfullamount = d * exrate;
            //finaletb = Double.parseDouble(df.format((etbfullamount * etbRate) / 100));


            finaletb = etbfullamount * etbRate / 100;

            BigDecimal bd1 = BigDecimal.valueOf(finaletb);
            bd1 = bd1.setScale(2, RoundingMode.HALF_UP);

            finaletb = bd1.doubleValue();

            double tempetbAmount = (fcyRate * etbfullamount) / 100;

            finalFcy = tempetbAmount / exrate;

            BigDecimal bd = BigDecimal.valueOf(finalFcy);
            bd = bd.setScale(2, RoundingMode.HALF_UP);

            finalFcy = bd.doubleValue();
        
            
            
            //finalFcy = Math.round(tempetbAmount / exrate);
            // double tempetbAmount = (etbRate * d)/100;
            // finaletb = exrate*tempetbAmount;
            finalRate = exrate;
            fcy_exrate = exrate;

        } 
        else if(messCurrency.equalsIgnoreCase("ETB"))
        {
            
            finaletb = ((d * etbRate) / 100);

            BigDecimal bd2 = BigDecimal.valueOf(finaletb);
            bd2 = bd2.setScale(2, RoundingMode.HALF_UP);

            finaletb=bd2.doubleValue();

            double tempetbAmount = (fcyRate * d) / 100;
            double buyexRate = valueBuyRate(messValueDate, messAccCurrency);

           
            
            finalFcy = (tempetbAmount / exrate);

            BigDecimal bd3 = BigDecimal.valueOf(finalFcy);
            bd3 = bd3.setScale(2, RoundingMode.HALF_UP);

            finalFcy = bd3.doubleValue();


            finalRate=buyexRate;
            fcy_exrate=buyexRate;
        }
        else {
            double etbfullamount = d * exrate;
             
           // finaletb = Double.parseDouble(df.format((etbfullamount * etbRate) / 100));
            double tempetbAmount = (fcyRate * etbfullamount) / 100;

            // d//ouble changeAmount = (fcyRate * d)/100;
            // double tempAmount = changeAmount * exrate;

             //sellRate = valueSellRate(messValueDate, messAccCurrency);
            System.out.println("sellrate"+sellRate);

           
            finalFcy = (tempetbAmount / sellRate);

            BigDecimal bd4 = BigDecimal.valueOf(finalFcy);
            bd4 = bd4.setScale(2, RoundingMode.HALF_UP);

            finalFcy = bd4.doubleValue();

            System.out.println("finalFCY______"+finalFcy);

            if(messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY") || messCurrency.equalsIgnoreCase("AED")
                        || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK") || messCurrency.equalsIgnoreCase("DKK")
                         || messCurrency.equalsIgnoreCase("SAR"))
                         {
                            finalFcy = (finalFcy / 100);

                            BigDecimal bd5 = BigDecimal.valueOf(finalFcy);
                            bd5 = bd5.setScale(2, RoundingMode.HALF_UP);
                            finalFcy = bd5.doubleValue();
                            
                         }

            System.out.println("finalFcy====="+finalFcy);

            System.out.println("tempetbAmount==="+tempetbAmount);

            // double tempetbAmount = (etbRate * d)/100;
            //finaletb = Double.parseDouble(df.format(((etbfullamount/sellRate) * etbRate) / 100));
            //finaletb = Double.parseDouble(df.format(exrate * tempetbAmount));
            //finaletb = Double.parseDouble(df.format((etbfullamount * etbRate)/100));
            finaletb = ((etbfullamount * etbRate)/100);

            BigDecimal bd6 = BigDecimal.valueOf(finaletb);
            bd6 = bd6.setScale(2, RoundingMode.HALF_UP);
            
            finaletb = bd6.doubleValue();
            if(messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY") || messCurrency.equalsIgnoreCase("AED")
                        || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK") || messCurrency.equalsIgnoreCase("DKK")
                         || messCurrency.equalsIgnoreCase("SAR"))
                         {
                            finaletb = (finaletb / 100);

                            BigDecimal bd7 = BigDecimal.valueOf(finaletb);
                            bd7 = bd7.setScale(2, RoundingMode.HALF_UP);
                            finaletb = bd7.doubleValue();

                            // finaletb = Double.parseDouble(df.format(finaletb / 100));
                         }

            System.out.println("finaletb======"+finaletb);

            finalRate = exrate;
            fcy_exrate = sellRate;

        }
    }

    public void calculateOtherRetaAmount(double messAmount, String messCurrency, double messRate,
            String messAccCurrency, String messValueDate) throws ParseException {

        // String messAmount = fromLastRecord.get(0).getFcyAmount().toString();
        // String messCurrency = fromLastRecord.get(0).getCurrencyId();
        // String messRate = fromLastRecord.get(0).getRate().toString();
        // String messAccCurrency = fromLastRecord.get(0).getAccountNumberCurrency();
        // String messValueDate = fromLastRecord.get(0).getValueDate().toString();
        etbRate = 100 - fcyRate;
        double d = messAmount;
        double exrate = messRate;
        DecimalFormat df = new DecimalFormat("0.00");

        double input = 1205.6358;

        System.out.println("salary : " + input);

        System.out.println("salary : " + df.format(input));

        if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

            double etbfullamount = d * exrate;
            // finaletb = Double.parseDouble(df.format((etbfullamount * etbRate) / 100));
            finaletb = (((etbfullamount * etbRate) / 100));

            BigDecimal bd8 = BigDecimal.valueOf(finaletb);
            bd8 = bd8.setScale(2, RoundingMode.HALF_UP);
            finaletb = bd8.doubleValue();


            double tempetbAmount = (fcyRate * etbfullamount) / 100;
            // finalFcy = Double.parseDouble(df.format(tempetbAmount / exrate));
            finalFcy = (tempetbAmount / exrate);

            BigDecimal bd9 = BigDecimal.valueOf(finalFcy);
            bd9 = bd9.setScale(2, RoundingMode.HALF_UP);
            finalFcy = bd9.doubleValue();
            // double tempetbAmount = (etbRate * d)/100;
            // finaletb = exrate*tempetbAmount;
            finalRate = exrate;
            fcy_exrate = exrate;

        } else {
            double etbfullamount = d * exrate;
            finaletb = Double.parseDouble(df.format((etbfullamount * etbRate) / 100));
            double tempetbAmount = (fcyRate * etbfullamount) / 100;

            // d//ouble changeAmount = (fcyRate * d)/100;
            // double tempAmount = changeAmount * exrate;

             sellRate = valueSellRate(messValueDate, messAccCurrency);

             finalFcy = (tempetbAmount / sellRate);

             BigDecimal bd10 = BigDecimal.valueOf(finalFcy);
            bd10 = bd10.setScale(2, RoundingMode.HALF_UP);
            finalFcy = bd10.doubleValue();


            // finalFcy = Double.parseDouble(df.format(tempetbAmount / sellRate));

            // double tempetbAmount = (etbRate * d)/100;
            // finaletb = Double.parseDouble(df.format(exrate * tempetbAmount));
            finaletb = (exrate * tempetbAmount);

            BigDecimal bd11 = BigDecimal.valueOf(finaletb);
            bd11 = bd11.setScale(2, RoundingMode.HALF_UP);
            finaletb=bd11.doubleValue();


            finalRate = exrate;
            fcy_exrate = sellRate;

        }
    }

    public void calculateETBAmount(String ammount, String messageccy, String acCcY, String valuedate, double exrate) {

        // double d = Double.parseDouble(ammount);
        DecimalFormat df = new DecimalFormat("0.00");
        double d = Double.parseDouble(ammount);

        if (messageccy.equalsIgnoreCase("ETB")) {

            double etbfullamount = d * exrate;
            double tempAmount = (etbRate * etbfullamount) / 100;

            finaletb = Double.parseDouble(df.format(exrate * tempAmount));
        }
    }

    public String saveMeesage(TblTmp temp) {
        // TblIncomingRecord mesRecord = new TblIncomingRecord();
        System.out.println("calue " + temp);

        System.out.println(temp.getAccountType() + "Account Type");
        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        RefreshableKeycloakSecurityContext c = (RefreshableKeycloakSecurityContext) request

                .getAttribute(KeycloakSecurityContext.class.getName());

        String usern = c.getToken().getPreferredUsername();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        referenceno = paramMap.get("id");

        System.out.println(temp.getReference() + "-------------from last page reference ");
        // fromLastRecord = swiftmessagerService.findByReference(temp.getReference());
        // System.out.println(fromLastRecord.get(0).toString() + "----------------last
        // Record");

        TblIncomingRecord realData = new TblIncomingRecord();

        realData.setAccountNumber(temp.getAccountNumber());

        // tempitems.setIsDashenBranch("0");
        realData.setOrderingCustomer(temp.getOrderingCustomer());
        realData.setOtherBankBranch(temp.getOtherBankBranch());
        realData.setRate(temp.getRate());
        realData.setRecordState(null);
        realData.setReference(temp.getReference());
        realData.setRegistrationDate(temp.getRegistrationDate());
        realData.setRegDate(new Date());
        realData.setSenderBank(temp.getSenderBank());
        realData.setAccountNumberCurrency(temp.getAccountNumberCurrency());
        realData.setValueDate(temp.getValueDate());
        realData.setAccountType(temp.getAccountType());
        realData.setBeneficiary(temp.getBeneficiary());
        realData.setDashenBranch(temp.getDashenBranch());
        realData.setCorrespondentBank(temp.getCorrespondentBank());
        realData.setFcyAmount(temp.getFcyAmount());
        realData.setCurrencyId(temp.getCurrencyId());
        realData.setCreatedBy(usern);
        realData.setPaymentPurpose(temp.getPaymentPurpose());
        realData.setTotalServiceCharge(temp.getTotalServiceCharge());

        // tempitems.setIsDashenBranch((Integer.toString(otherbanktrue)));
        realData.setCountryOfOrigin(temp.getCountryOfOrigin());

        System.out.println(temp.getAccountType() + "-------------from last page reference ");

        realData.setAccountNumber(temp.getAccountNumber());
        DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String formattedDate = df.format(Calendar.getInstance().getTime());
        List<TblIncomingRecord> lastrecord = swiftmessagerService.findLast();
        int lastref = lastrecord.get(0).getId().intValue();
        Long current = lastrecord.get(0).getRecordState();

        if (current == null) {

            long longValue = 1;

            current = longValue;

        }

        else

        {

            current++;

        }

        String FORMAT = "%05d";

        int currentSequenceNumber = 1;

        String banksReferenceString = String.format("%s%s/%s", "DB/RETA/", String.format(FORMAT, current), "24");
        String ourReference = "DB/RETA/" + lastref + 1 + "/" + formattedDate;
        System.out.println(ourReference + "  Reference");
        realData.setBankReference(banksReferenceString);
        realData.setRecordState(current);
        // mesRecord.setIsDashenBranch("0");
        realData.setRate(finalRate);
        System.out.println(realData.getAccountNumber() + " Test");
        // temp.setCountryOfOrigin(othercountryOrigin);
        swiftmessagerService.save(realData);
        newTransaction = new SwiftTransaction();
        if (newTransaction != null) {
            System.out.println("calue " + newTransaction);
            System.out.println(CUST_NO + "CUST NUMBER");
            String customerNumber = accountRestClient.getAccountInfo(realData.getAccountNumber())
                    .getCUST_NO();
            Customer cust = accountRestClient.getCustomeCategory(customerNumber);
            newTransaction.setMessage(realData);

            newTransaction.setCustomerType(cust.getCUSTOMER_CATEGORY());
            newTransaction.setMessage(realData);
            newTransaction.setCreatedBy(usern);
            newTransaction.setStatus("Pending");
            newTransaction.setTransactionNumber("");
            newTransaction.setLcyPercentage(etbRate);
            newTransaction.setFcyPercentage(fcyRate);
            newTransaction.setBirrAmount(finaletb);
            newTransaction.setFcyAmount(finalFcy);
            newTransaction.setRetaBirrAccount(etbaccount);
            newTransaction.setRetaBirrAccountName(AC_DESC);
            newTransaction.setRetaAccountCurrency(ACCY);
            newTransaction.setFcyRate(fcy_exrate);

            SwiftTransaction trans = swifttransactionservice.save(newTransaction);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Data saved", "Data has been saved successfully..." + trans.getMessage().getBankReference()));
            System.out.println("saved");

            cust = null;
 
            etbRate = 0.0;
            fcyRate = 0.0;
            finaletb = 0.0;
            finalFcy = 0.0;
            etbaccount = null;
            AC_DESC = null;
            ACCY = null;
            fcy_exrate = 0.0;
            fetchedDataController.clearSearch();
            return "redirect:/Message/index.xhtml";
        } else
            System.out.println("error");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data has not been saved..."));
        return "redirect:/Message/index.xhtml";
    }

    public String saveRetaMeesage(TblIncomingRecord temp, String otheracc, String othername, String othercurrency) {
        // TblIncomingRecord mesRecord = new TblIncomingRecord();
        System.out.println("calue " + temp);

        System.out.println(temp.getAccountType() + "Account Type");
        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        RefreshableKeycloakSecurityContext c = (RefreshableKeycloakSecurityContext) request

                .getAttribute(KeycloakSecurityContext.class.getName());

        String usern = c.getToken().getPreferredUsername();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        referenceno = paramMap.get("id");

        System.out.println(temp.getReference() + "-------------from last page reference ");
        fromLastRecord = swiftmessagerService.findByReference(temp.getReference());
        System.out.println(fromLastRecord.get(0).toString() + "----------------last Record");

        fromLastRecord.get(0).setCreatedBy(usern);
        fromLastRecord.get(0).setRegistrationDate(new Date());

        fromLastRecord.get(0).setAccountNumber(temp.getAccountNumber());
        DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String formattedDate = df.format(Calendar.getInstance().getTime());
        List<TblIncomingRecord> lastrecord = swiftmessagerService.findLast();
        int lastref = lastrecord.get(0).getId().intValue();
        String ourReference = "DB/RETA/" + lastref + 1 + "/" + formattedDate;
        System.out.println(ourReference + "  Reference");
        fromLastRecord.get(0).setBankReference(ourReference);
        // mesRecord.setIsDashenBranch("0");
        fromLastRecord.get(0).setOrderingCustomer(temp.getOrderingCustomer());
        fromLastRecord.get(0).setOtherBankBranch(temp.getOtherBankBranch());
        fromLastRecord.get(0).setRate(temp.getRate());
        fromLastRecord.get(0).setRecordState(null);
        fromLastRecord.get(0).setReference(temp.getReference());
        fromLastRecord.get(0).setRegDate(temp.getRegDate());
        fromLastRecord.get(0).setSenderBank(temp.getSenderBank());
        fromLastRecord.get(0).setAccountNumberCurrency(temp.getAccountNumberCurrency());
        fromLastRecord.get(0).setValueDate(temp.getValueDate());
        fromLastRecord.get(0).setAccountType(temp.getAccountType());
        fromLastRecord.get(0).setBeneficiary(temp.getBeneficiary());
        fromLastRecord.get(0).setDashenBranch(temp.getDashenBranch());
        fromLastRecord.get(0).setCorrespondentBank(temp.getCorrespondentBank());
        fromLastRecord.get(0).setFcyAmount(temp.getFcyAmount());
        fromLastRecord.get(0).setCurrencyId(temp.getCurrencyId());
        fromLastRecord.get(0).setPaymentPurpose(temp.getPaymentPurpose());

        fromLastRecord.get(0).setCountryOfOrigin(temp.getCountryOfOrigin());
        fromLastRecord.get(0).setRate(finalRate);
        System.out.println(fromLastRecord.get(0).getAccountNumber() + " Test");
        // temp.setCountryOfOrigin(othercountryOrigin);
        swiftmessagerService.save(fromLastRecord.get(0));
        newTransaction = new SwiftTransaction();
        if (newTransaction != null) {
            System.out.println("calue " + newTransaction);
            System.out.println(CUST_NO + "CUST NUMBER");
            String customerNumber = accountRestClient.getAccountInfo(fromLastRecord.get(0).getAccountNumber())
                    .getCUST_NO();
            Customer cust = accountRestClient.getCustomeCategory(customerNumber);
            newTransaction.setMessage(fromLastRecord.get(0));

            newTransaction.setCustomerType(cust.getCUSTOMER_CATEGORY());
            newTransaction.setMessage(fromLastRecord.get(0));
            newTransaction.setCreatedBy(usern);
            newTransaction.setStatus("Pending");
            newTransaction.setTransactionNumber("");
            newTransaction.setLcyPercentage(etbRate);
            newTransaction.setFcyPercentage(fcyRate);
            newTransaction.setBirrAmount(finaletb);
            newTransaction.setFcyAmount(finalFcy);
            newTransaction.setRetaBirrAccount(etbaccount);
            newTransaction.setRetaBirrAccountName(AC_DESC);
            newTransaction.setFcyRate(fcy_exrate);
            newTransaction.setOtherAccountCurrency(othercurrency);
            newTransaction.setOtherAccountName(othername);
            newTransaction.setOtherAccountNumber(otheracc);

            SwiftTransaction trans = swifttransactionservice.save(newTransaction);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Data saved", "Data has been saved successfully..." + trans.getMessage().getBankReference()));
            System.out.println("saved");
            return "redirect:/Message/index.xhtml";
        } else
            System.out.println("error");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data has not been saved..."));
        return "redirect:/Message/index.xhtml";
    }

    public double getFinalretafcy(double messAmount, String messCurrency, double messRate, String messAccCurrency,
            String messValueDate) {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

            referenceno = paramMap.get("id");

            System.out.println(referenceno + " Incoming Reference Number");

            fromLastRecord = swiftmessagerService.findByReference(referenceno);

            // messAmount = fromLastRecord.get(0).getFcyAmount().toString();
            // messCurrency = fromLastRecord.get(0).getCurrencyId();
            // messRate = fromLastRecord.get(0).getRate().toString();
            // messAccCurrency = fromLastRecord.get(0).getAccountNumberCurrency();
            // messValueDate = fromLastRecord.get(0).getValueDate().toString();

            double d = messAmount;
            double rateExcha = messRate;

            if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

                finalFcy = (fcyRate * d) / 100;
                finalRate = rateExcha;
                fcy_exrate = 1.0;
            } else {

                double changeAmount = (fcyRate * d) / 100;
                double tempAmount = changeAmount * rateExcha;

                 sellRate = valueSellRate(messValueDate, messAccCurrency);

                finalFcy = tempAmount / sellRate;

                fcy_exrate = finalFcy / tempAmount;
                finalRate = rateExcha;

            }

        } catch (Exception ex) {

        }
        return finalretafcy;
    }

    public void setFinalretafcy(double finalretafcy) {
        this.finalretafcy = finalretafcy;
    }

}