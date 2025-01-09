package com.swiftmessage.swiftmessage.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
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

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.Customer;
import com.swiftmessage.swiftmessage.dto.Rates;
import com.swiftmessage.swiftmessage.dto.Request;
import com.swiftmessage.swiftmessage.dto.Response;
import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.entity.TblTmp;
import com.swiftmessage.swiftmessage.entity.ExRateLocal;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.ExRateLocalService;
import com.swiftmessage.swiftmessage.service.MessageIncomingService;
import com.swiftmessage.swiftmessage.service.PaymentRestClient;
import com.swiftmessage.swiftmessage.service.RateRestClient;
import com.swiftmessage.swiftmessage.service.SwiftMessageService;
import com.swiftmessage.swiftmessage.service.SwiftTransactionService;
import com.swiftmessage.swiftmessage.service.TblTmpService;



@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)

public class FincTransactionController {

    @Autowired
    MessageIncomingService swiftmessagerService;

    @Autowired
    TblTmpService tblTmpService;

    @Autowired
    SwiftTransactionService swifttransactionservice;

    @Autowired
    private FetchedDataController fetchedDataController;

    @Autowired
    PaymentRestClient paymentClient;

    private static final String baseUrl12 = "http://192.168.12.47:6070/getByDateAndCy/";
    private WebClient webclient12 = WebClient.create(baseUrl12);
    RateRestClient rateRestClient = new RateRestClient(webclient12);

    private static final String baseUrl2 = "http://192.168.12.47:6070/getByAccountNo/";
    private WebClient webclient2 = WebClient.create(baseUrl2);
    AccountRestClient accountRestClient = new AccountRestClient(webclient2);

    // @Autowired
    // RateRestClient rateRestClient;

    // @Autowired
    // AccountRestClient accountRestClient;

    private SwiftTransaction selectedTransaction;
    private List<SwiftTransaction> transactionitems;
    private SwiftTransaction newTransaction;
    private List<TblIncomingRecord> fromLastRecord = null;

    private List<AccountInfo> relatedAccountInfo;

    private boolean newaccrnd = false;
    private boolean retaAcc = false;

    private String accountnum;
    private String AC_DESC = null;
    private String CUST_AC_NO;
    private String BRANCH_CODE;
    private String ACCOUNT_CLASS;
    private String ACCY = null;
    private String CUST_NO;

    private String RAC_DESC;
    private String RCUST_AC_NO;
    private String RBRANCH_CODE;
    private String RACCOUNT_CLASS;
    private String RACCY;
    private String RCUST_NO;
    private String etbaccount;

    private double fcyRate;
    private double etbRate;
    private double finalRFcy;
    private double finalretafcy;
    private double finaletb;
    private Double finalRate = null;
    private Double fcy_exrate;

    private double finalAmount;
    private double newFinalAmount;
    private String messAmount;
    private String messCurrency;
    private String messRate;
    private String messAccCurrency;
    private String messValueDate;
    private String othercountryOrigin;
    private Double sellRate;
    private String ourReference = "";
    private String referenceno;
    private Double finalrate = null;

    DecimalFormat df = new DecimalFormat("0.00");

    // private static final String baseUrpy =
    // "http://192.168.12.47:8088/cbsservice/transaction/";//
    // "http://localhost:8085/cbsservice/transaction/";
    // private WebClient webclientpy = WebClient.create(baseUrpy);
    // // PaymentRestClient paymentClient = new PaymentRestClient(webclientpy);

    @PostConstruct
    public void init() throws ParseException {

        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

            referenceno = paramMap.get("id");

            System.out.println(referenceno + " Incoming Reference Number");

            List<TblTmp> backData = tblTmpService.findByReference(referenceno);
            // fromLastRecord = swiftmessagerService.findByReference(referenceno);

            System.out.println("info on back=====" + backData.get(0).getAccountNumber());
            messAmount = backData.get(0).getFcyAmount().toString();
            messCurrency = backData.get(0).getCurrencyId();
            messRate = backData.get(0).getRate().toString();
            messAccCurrency = backData.get(0).getAccountNumberCurrency();
            messValueDate = backData.get(0).getValueDate().toString();

            sellRate = valueSellRate(messValueDate, messAccCurrency);

            newaccrnd = false;
            accountnum = null;
            RAC_DESC = null;
            RACCY = null;
            BRANCH_CODE = null;
            // sellRate = null;
            finalAmount = 0.0;
            etbaccount = new String();
            newTransaction = null;
            retaAcc = false;
            AC_DESC = "";

            ACCY = "";
            relatedAccountInfo = new ArrayList<>();

            fcyRate = 0.0;
            etbRate = 0.0;
            finalRFcy = 0.0;
            finalretafcy = 0.0;
            finaletb = 0.0;
            finalRate = 0.0;
            fcy_exrate = 0.0;
        }

    }

    @GetMapping("/clear")
    public String clearForm() {
        // Logic to clear any necessary data
        // For example, clearing session attributes
        newaccrnd = false;
        accountnum = null;
        RAC_DESC = null;
        RACCY = null;
        BRANCH_CODE = null;
        sellRate = null;
        finalAmount = 0.0;
        etbaccount = new String();
        newTransaction = null;
        retaAcc = false;
        AC_DESC = "";

        ACCY = "";
        relatedAccountInfo = new ArrayList<>();

        fcyRate = 0.0;
        etbRate = 0.0;
        finalRFcy = 0.0;
        finalretafcy = 0.0;
        finaletb = 0.0;
        finalRate = 0.0;
        fcy_exrate = 0.0;
        FetchedDataController data = new FetchedDataController();
        data.clearSearch();

        // Add a flash attribute to show a message on the first page
        // redirectAttributes.addFlashAttribute("message", "Form cleared
        // successfully!");

        return "forward:/Message/NewMessage.xhtml"; // Redirect to the first page
    }

    public List<AccountInfo> getAccountlist(String name) {

        String accCut = name.substring(1, 10);
        System.out.println(accCut + "Sub String");
        List<AccountInfo> accountlist = accountRestClient.getOtherByAccountName(accCut);
        System.out.println(accountlist + "List");
        return accountlist;
    }

    public void searchAcc() {
        System.out.println(etbaccount + "Account 123");
        AccountInfo accountDto = accountRestClient.getAccountInfo(etbaccount);

        if (accountDto != null) {
            System.out.println(accountDto.getAC_DESC() + "  Account Info List");

            RAC_DESC = accountDto.getAC_DESC();
            RCUST_AC_NO = accountDto.getCUST_AC_NO();
            RBRANCH_CODE = accountDto.getBRANCH_CODE();
            RACCOUNT_CLASS = accountDto.getACCOUNT_CLASS();
            RACCY = accountDto.getCCY();
            RCUST_NO = accountDto.getCUST_NO();

        }
    }

    public void changeinSellRate() {
        System.out.println("Changed sell other rate====" + retaSellingRate);
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

        System.out.println("salary : " + d);

        System.out.println("salary : " + df.format(input));

        if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

            double etbfullamount = d * exrate;
            // finaletb = Double.parseDouble(df.format((etbfullamount * etbRate) / 100));
            finaletb = ((etbfullamount * etbRate) / 100);

            BigDecimal bdb1 = BigDecimal.valueOf(finaletb);
            bdb1 = bdb1.setScale(2, RoundingMode.HALF_UP);
            finaletb = bdb1.doubleValue();

            double tempetbAmount = (fcyRate * etbfullamount) / 100;

            System.out.println("tempetbAmount----------" + tempetbAmount);
            // finalRFcy = Double.parseDouble(df.format(tempetbAmount / exrate));
            finalRFcy = (tempetbAmount / exrate);

            BigDecimal bdb = BigDecimal.valueOf(finalRFcy);
            bdb = bdb.setScale(2, RoundingMode.HALF_UP);
            finalRFcy = bdb.doubleValue();
            System.out.println("finalRFcy----------" + finalRFcy);
            // double tempetbAmount = (etbRate * d)/100;
            // finaletb = exrate*tempetbAmount;
            finalRate = exrate;
            fcy_exrate = exrate;

        } else {

            double etbfullamount = d * exrate;
            // finaletb = Double.parseDouble(df.format((etbfullamount * etbRate) / 100));
            finaletb = ((etbfullamount * etbRate) / 100);
            BigDecimal bdb1 = BigDecimal.valueOf(finaletb);
            bdb1 = bdb1.setScale(2, RoundingMode.HALF_UP);
            finaletb = bdb1.doubleValue();

            if (messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY")
                    || messCurrency.equalsIgnoreCase("AED")
                    || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK")
                    || messCurrency.equalsIgnoreCase("DKK")
                    || messCurrency.equalsIgnoreCase("SAR")) {

                        System.out.println("HERE   1");
                finaletb = (finaletb / 100);
                BigDecimal bdb = BigDecimal.valueOf(finaletb);
                bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                finaletb = bdb.doubleValue();
                // finaletb = Double.parseDouble(df.format(finaletb / 100));
            }
            double tempetbAmount = (fcyRate * etbfullamount) / 100;

            // d//ouble changeAmount = (fcyRate * d)/100;
            // double tempAmount = changeAmount * exrate;

            System.out.println("date++++" + messValueDate);

            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yy");

            // Parse the input string to Date object
            Date date = inputFormat.parse(messValueDate);

            // Create a SimpleDateFormat object with desired output date format
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Format the Date object to the desired output format
            String outputDate = outputFormat.format(date);
            double buyexRate = valueBuyRate(outputDate, messAccCurrency);
            // sellRate = valueSellRate(outputDate, messAccCurrency);
            System.out.println("tempetbAmount----------" + tempetbAmount);
            if (messCurrency.equalsIgnoreCase("ETB")) {
                finalRFcy = (tempetbAmount / buyexRate);
                BigDecimal bdb = BigDecimal.valueOf(finalRFcy);
                bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                finalRFcy = bdb.doubleValue();
                // finalRFcy = Double.parseDouble(df.format(tempetbAmount / buyexRate));
                fcy_exrate = buyexRate;
            } else {
                // finalRFcy = Double.parseDouble(df.format(tempetbAmount / sellRate));
                finalRFcy = (tempetbAmount / retaSellingRate);
                BigDecimal bdb4 = BigDecimal.valueOf(finalRFcy);
                bdb4 = bdb4.setScale(2, RoundingMode.HALF_UP);
                finalRFcy = bdb4.doubleValue();

                if (messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY")
                        || messCurrency.equalsIgnoreCase("AED")
                        || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK")
                        || messCurrency.equalsIgnoreCase("DKK")
                        || messCurrency.equalsIgnoreCase("SAR")) {

                            System.out.println("HERE   2");
                    finalRFcy = (finalRFcy / 100);

                    BigDecimal bdb = BigDecimal.valueOf(finalRFcy);
                    bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                    finalRFcy = bdb.doubleValue();
                    // finalRFcy = Double.parseDouble(df.format(finalRFcy / 100));
                }
                fcy_exrate = retaSellingRate;
            }
            System.out.println("finalRFcy----------" + finalRFcy);
            // double tempetbAmount = (etbRate * d)/100;
            // finaletb = Double.parseDouble(df.format(exrate * tempetbAmount));
            finalRate = exrate;

        }
    }

    public String saveRetaMeesage(TblTmp temp, String otheracc, String othername, String othercurrency)
            throws ParseException {
        // TblIncomingRecord mesRecord = new TblIncomingRecord();
        System.out.println("calue " + temp);

        System.out.println(temp.getAccountType() + "Account Type");
        FacesMessage message = null;

        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        RefreshableKeycloakSecurityContext c = (RefreshableKeycloakSecurityContext) request

                .getAttribute(KeycloakSecurityContext.class.getName());

        String usern = c.getToken().getPreferredUsername();

        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        referenceno = paramMap.get("id");

        System.out.println("referenceno=========" + referenceno);

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
            newTransaction.setFcyAmount(finalRFcy);
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

            newaccrnd = false;
            accountnum = null;
            RAC_DESC = null;
            RACCY = null;
            BRANCH_CODE = null;
            sellRate = null;
            finalAmount = 0.0;
            temp = null;
            newTransaction = null;
            retaAcc = false;
            AC_DESC = "";

            ACCY = "";

            return "redirect:/Message/index.xhtml";
        } else
            System.out.println("error");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Data has not been saved..."));
        reset();
        fetchedDataController.clearSearch();
        return "redirect:/Message/index.xhtml";
    }

    public List<AccountInfo> getRelatedAccountInfo() {
        if (relatedAccountInfo == null) {
            relatedAccountInfo = new ArrayList<>();
        }
        return relatedAccountInfo;
    }

    public void setRelatedAccountInfo(List<AccountInfo> relatedAccountInfo) {
        this.relatedAccountInfo = relatedAccountInfo;
    }

    public String getRAC_DESC() {
        return RAC_DESC;
    }

    public void setRAC_DESC(String rAC_DESC) {
        RAC_DESC = rAC_DESC;
    }

    public String getRCUST_AC_NO() {
        return RCUST_AC_NO;
    }

    public void setRCUST_AC_NO(String rCUST_AC_NO) {
        RCUST_AC_NO = rCUST_AC_NO;
    }

    public String getRBRANCH_CODE() {
        return RBRANCH_CODE;
    }

    public void setRBRANCH_CODE(String rBRANCH_CODE) {
        RBRANCH_CODE = rBRANCH_CODE;
    }

    public String getRACCOUNT_CLASS() {
        return RACCOUNT_CLASS;
    }

    public void setRACCOUNT_CLASS(String rACCOUNT_CLASS) {
        RACCOUNT_CLASS = rACCOUNT_CLASS;
    }

    public String getRACCY() {
        return RACCY;
    }

    public void setRACCY(String rACCY) {
        RACCY = rACCY;
    }

    public String getRCUST_NO() {
        return RCUST_NO;
    }

    public void setRCUST_NO(String rCUST_NO) {
        RCUST_NO = rCUST_NO;
    }

    public String getEtbaccount() {
        return etbaccount;
    }

    public void setEtbaccount(String etbaccount) {
        this.etbaccount = etbaccount;
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

    public double getFinalRFcy() {
        return finalRFcy;
    }

    public void setFinalRFcy(double finalRFcy) {
        this.finalRFcy = finalRFcy;
    }

    public double getFinalretafcy() {
        return finalretafcy;
    }

    public void setFinalretafcy(double finalretafcy) {
        this.finalretafcy = finalretafcy;
    }

    public double getFinaletb() {
        return finaletb;
    }

    public void setFinaletb(double finaletb) {
        this.finaletb = finaletb;
    }

    public Double getFinalRate() {
        return finalRate;
    }

    public void setFinalRate(Double finalRate) {
        this.finalRate = finalRate;
    }

    public Double getFcy_exrate() {
        return fcy_exrate;
    }

    public void setFcy_exrate(Double fcy_exrate) {
        this.fcy_exrate = fcy_exrate;
    }

    public Double getFinalrate() {
        return finalrate;
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

    public void setFinalrate(Double finalrate) {
        this.finalrate = finalrate;
    }

    public String getOurReference() {
        return ourReference;
    }

    public void setOurReference(String ourReference) {
        this.ourReference = ourReference;
    }

    public Double getSellRate() {
        return sellRate;
    }

    public void setSellRate(Double sellRate) {
        this.sellRate = sellRate;
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

    public String getOthercountryOrigin() {
        return othercountryOrigin;
    }

    public void setOthercountryOrigin(String othercountryOrigin) {
        this.othercountryOrigin = othercountryOrigin;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
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

    public boolean isNewaccrnd() {
        return newaccrnd;
    }

    public void setNewaccrnd(boolean newaccrnd) {
        this.newaccrnd = newaccrnd;
    }

    public void newAccount() {

        System.out.println(newaccrnd);

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

    public double getNewFinalAmount() throws ParseException {

        return newFinalAmount;
    }

    public void setNewFinalAmount(double newFinalAmount) {
        this.newFinalAmount = newFinalAmount;
    }

    @RequestMapping("/Transaction/pay")
    public String edit(@RequestParam BigDecimal id) {

        Request transaction = new Request();
        transaction.setSOURCE("IBEXT1");
        transaction.setUBSCOMP("FCUBS");

        transaction.setUSERID("CTSUSER");
        transaction.setBRANCH("387");
        transaction.setMODULEID("RT");
        transaction.setSERVICE("FCUBSRTService");
        transaction.setOPERATION("CreateTransaction");
        transaction.setOPERATION("CreateTransaction");

        Response response = paymentClient.autorizeTransaction(transaction);

        System.out.println(response.toString() + "     response");

        System.out.println(response.getMSGSTAT() + "     Status");

        return "forward:/Transaction/index.xhtml";

    }

    public double getFinalAmount() throws ParseException {
        if (!newaccrnd) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

                referenceno = paramMap.get("id");

                System.out.println(referenceno + " Incoming Reference Number");

                List<TblTmp> backData = tblTmpService.findByReference(referenceno);
                // fromLastRecord = swiftmessagerService.findByReference(referenceno);

                System.out.println("info on back=====" + backData.get(0).getAccountNumber());
                messAmount = backData.get(0).getFcyAmount().toString();
                messCurrency = backData.get(0).getCurrencyId();
                messRate = backData.get(0).getRate().toString();
                messAccCurrency = backData.get(0).getAccountNumberCurrency();
                messValueDate = backData.get(0).getValueDate().toString();

                double d = Double.parseDouble(messAmount);

                double rateExcha = Double.parseDouble(messRate);

                System.out.println(newaccrnd + "Other Account");

                System.out.println("projected ID : ---------------" + messAmount);
                System.out.println("projected ID : ---------------" + messCurrency);
                System.out.println("projected ID : ---------------" + messRate);

                if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

                    // finalAmount = Double.parseDouble(df.format(d));
                    finalAmount = (d);

                    BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                    bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                    finalAmount = bdb.doubleValue();

                    finalrate = rateExcha;
                    System.out.println(finalrate + "  final rate");

                } else if (messCurrency.equalsIgnoreCase("ETB")) {
                    System.out.println("HERE 123");
                    System.out.println("amount" + d);
                    System.out.println("Rate" + backData.get(0).getRate());
                    finalAmount = (d / backData.get(0).getRate());

                    BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                    bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                    finalAmount = bdb.doubleValue();

                    // finalAmount = Double.parseDouble(df.format( d/backData.get(0).getRate()));
                    finalrate = rateExcha;
                    System.out.println(finalrate + "  ETB CASE");

                } else {

                    if (messAccCurrency.equalsIgnoreCase("ETB")) {
                        finalAmount = (rateExcha * d);

                        BigDecimal bdb4 = BigDecimal.valueOf(finalAmount);
                        bdb4 = bdb4.setScale(2, RoundingMode.HALF_UP);
                        finalAmount = bdb4.doubleValue();

                        // finalAmount = Double.parseDouble(df.format(rateExcha * d));
                        if (messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY")
                                || messCurrency.equalsIgnoreCase("AED")
                                || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK")
                                || messCurrency.equalsIgnoreCase("DKK")
                                || messCurrency.equalsIgnoreCase("SAR")) {


                            System.out.println("Here Unit 100");
                            finalAmount = (finalAmount / 100);

                            BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                            bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                            finalAmount = bdb.doubleValue();
                            // finalAmount = finalAmount = Double.parseDouble(df.format(finalAmount/100));
                        }
                        finalrate = rateExcha;
                        System.out.println(finalrate + "  final rate890");
                    }

                    else {

                        double tempETB = d * rateExcha;

                        sellRate = valueSellRate(messValueDate, messAccCurrency);

                        // finalAmount = Double.parseDouble(df.format(tempETB / sellRate));
                        finalAmount = (tempETB / sellRate);

                        BigDecimal bdb2 = BigDecimal.valueOf(finalAmount);
                        bdb2 = bdb2.setScale(2, RoundingMode.HALF_UP);
                        finalAmount = bdb2.doubleValue();

                        if (messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY")
                                || messCurrency.equalsIgnoreCase("AED")
                                || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK")
                                || messCurrency.equalsIgnoreCase("DKK")
                                || messCurrency.equalsIgnoreCase("SAR")) {

                            System.out.println("Here Uni 6");
                            // finalAmount = finalAmount = Double.parseDouble(df.format(finalAmount/100));
                            finalAmount = (finalAmount / 100);

                            BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                            bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                            finalAmount = bdb.doubleValue();
                        }

                        finalrate = sellRate;

                        System.out.println(finalAmount + "Final divided amount");
                        System.out.println(finalrate + "  final rate 678");

                    }
                }

            } catch (Exception ex) {

            }
        }

        return finalAmount;
    }

    public void calculateFinalAmount()
            throws ParseException {

        // messAmount = ammount;
        // messCurrency = messageccy;
        // messRate = "" + exrate;
        // messAccCurrency = acCcY;
        // messValueDate = valuedate;

        double d = Double.parseDouble(messAmount);
        double rateExcha = Double.parseDouble(messRate);
        if (!newaccrnd) {

            System.out.println(newaccrnd + "Other Account");

            System.out.println("projected ID : ---------------" + messAmount);
            System.out.println("projected ID : ---------------" + messCurrency);
            System.out.println("projected ID : ---------------" + messRate);

            if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

                // finalAmount = Double.parseDouble(df.format(d));
                finalAmount = (d);

                BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                finalAmount = bdb.doubleValue();
                finalrate = rateExcha;
                System.out.println(finalrate + "  final rate");
            } else {
                if (messAccCurrency.equalsIgnoreCase("ETB")) {
                    finalAmount = (rateExcha * d);

                    BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                    bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                    finalAmount = bdb.doubleValue();
                    // finalAmount = Double.parseDouble(df.format(rateExcha * d));
                    finalrate = rateExcha;
                    System.out.println(finalrate + "  final rate");
                }

                else {

                    double tempETB = d * rateExcha;

                    sellRate = valueSellRate(messValueDate, messAccCurrency);

                    finalAmount = (tempETB / sellRate);

                    BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                    bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                    finalAmount = bdb.doubleValue();
                    // finalAmount = Double.parseDouble(df.format(tempETB / sellRate));

                    finalrate = sellRate;
                    System.out.println(finalrate + "  final rate");

                }
            }
        }

        else {
            if (messCurrency.equalsIgnoreCase(messAccCurrency)) {

                finalAmount = (d);

                BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                finalAmount = bdb.doubleValue();
                // finalAmount = Double.parseDouble(df.format(d));
                finalrate = rateExcha;
            } else {
                if (messAccCurrency.equalsIgnoreCase("ETB")) {
                    finalAmount = (rateExcha * d);

                    BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                    bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                    finalAmount = bdb.doubleValue();
                    // finalAmount = Double.parseDouble(df.format(rateExcha * d));
                    finalrate = rateExcha;
                }

                else {

                    double tempETB = d * rateExcha;

                    sellRate = valueSellRate(messValueDate, messAccCurrency);

                    finalAmount = (tempETB / sellRate);

                    BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                    bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                    finalAmount = bdb.doubleValue();
                    // finalAmount = Double.parseDouble(df.format(tempETB / sellRate));
                    finalrate = sellRate;
                    System.out.println(finalAmount + " final amount");

                }
            }
        }

    }

    @Autowired
    ExRateLocalService exRateLocalService;

    public Double valueSellRate(String ValueDate, String Currency) throws ParseException {

        System.out.println("HERE" + ValueDate);
        Double value = 0.0;
        String result = ValueDate.split(" ")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = formatter.parse(result);
        DateFormat df = new SimpleDateFormat("dd-MMM-yy");
        String dateToString = df.format(date1);
        List<Rates> rate = new ArrayList<>();
        List<ExRateLocal> localListRate = new ArrayList<>();

        rate = rateRestClient.retriveRatesByDateAndCy(dateToString, Currency);

        System.out.println("Size Rate   " + rate);
        if (rate.size() == 0 || rate == null) {
            System.out.println("check here 2");
            localListRate = exRateLocalService.findbyDateCcy(Currency, dateToString);
            System.out.println("List of fetched rates==" + localListRate);
            for (int i = 0; i < localListRate.size(); i++) {

                if (localListRate.get(i).getRATE_TYPE().equals("BANK_SEL")) {

                    System.out.println("Local Rate =" + localListRate.get(i));
                    value = Double.valueOf(localListRate.get(i).getMID_RATE());

                }

            }
            // rate = rateRestClient.retriveRatesByDateAndCyHis(dateToString,Currency);

        } else {
            for (int i = 0; i < rate.size(); i++) {
                if (rate.get(i).getRATE_TYPE().equals("BANK_SEL")) {

                    value = rate.get(i).getSALE_RATE();
                    // System.out.println(value + "Sale rate of that day");
                }

            }
        }

        return value;

    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
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
        List<ExRateLocal> localListRate = new ArrayList<>();

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

        } else {
            for (int i = 0; i < rate.size(); i++) {

                System.out.println("4444444444444444444444");
                if (rate.get(i).getRATE_TYPE().equals("BANK_BUY")) {

                    rateValue = rate.get(i).getMID_RATE();

                }

            }
        }

        return rateValue;

    }

    @RequestMapping("/FINC/reta")
    public String findRetaAcc() {
        System.out.println("test");
        return "forward:/Message/retention.xhtml";
    }

    private boolean retaSelling = false;
    private double retaSellingRate = 0.0;

    public double getRetaSellingRate() {
        return retaSellingRate;
    }

    public void setRetaSellingRate(double retaSellingRate) {
        this.retaSellingRate = retaSellingRate;
    }

    public boolean isRetaSelling() {
        return retaSelling;
    }

    public void setRetaSelling(boolean retaSelling) {
        this.retaSelling = retaSelling;
    }

    public void retriveAccount()
            throws ParseException {
        fcyRate = 0.0;
        etbRate = 0.0;
        finalRFcy = 0.0;
        finalretafcy = 0.0;
        finaletb = 0.0;
        finalRate = 0.0;
        fcy_exrate = 0.0;

        retaAcc = false;
        AccountInfo accountDto = new AccountInfo();
        
        accountDto = accountRestClient.getAccountInfo(accountnum);
        

        if (accountDto != null) {
            if (accountDto.getACCOUNT_CLASS().contains("RET")) {
                retaAcc = true;
                retaSelling = true;
                AC_DESC = accountDto.getAC_DESC();
                CUST_AC_NO = accountDto.getCUST_AC_NO();
                BRANCH_CODE = accountDto.getBRANCH_CODE();
                ACCOUNT_CLASS = accountDto.getACCOUNT_CLASS();
                ACCY = accountDto.getCCY();
                CUST_NO = accountDto.getCUST_NO();

                retaSellingRate = valueSellRate(messValueDate, ACCY);
                String accCut = AC_DESC.substring(1, 10);
                System.out.println(accCut + "Sub String");
                relatedAccountInfo = new ArrayList<>();
                // relatedAccountInfo = accountRestClient.getOtherByAccountName(accCut);
                System.out.println(relatedAccountInfo + "List");

                // findRetaAcc();
            } else {

                retaAcc = false;
                System.out.println(accountDto.getAC_DESC() + "  Account Info List");

                AC_DESC = accountDto.getAC_DESC();
                CUST_AC_NO = accountDto.getCUST_AC_NO();
                BRANCH_CODE = accountDto.getBRANCH_CODE();
                ACCOUNT_CLASS = accountDto.getACCOUNT_CLASS();
                ACCY = accountDto.getCCY();
                CUST_NO = accountDto.getCUST_NO();

                FacesContext context = FacesContext.getCurrentInstance();
                Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
                System.out.println("zzzzzzz123456789");
                // messAmount = ammount;
                // messCurrency = messageccy;
                // messRate = "" + exrate;
                // messAccCurrency = acCcY;
                // messValueDate = valuedate;
                double d = Double.parseDouble(messAmount);

                System.out.println();
                double rateExcha = Double.parseDouble(messRate);

                // System.out.println(newaccrnd + "Other Account");

                System.out.println("projected ID : ---------------" + messAmount);
                System.out.println("projected ID : ---------------" + messCurrency);
                System.out.println("projected ID : ---------------" + messRate);

                if (messCurrency.equalsIgnoreCase(ACCY)) {

                    finalAmount = (d);

                    BigDecimal bdb1 = BigDecimal.valueOf(finalAmount);
                    bdb1 = bdb1.setScale(2, RoundingMode.HALF_UP);
                    finalAmount = bdb1.doubleValue();
                    // finalAmount = Double.parseDouble(df.format(d));
                    finalrate = rateExcha;
                }

                else {
                    if (ACCY.equalsIgnoreCase("ETB")) {
                        // finalAmount = Double.parseDouble(df.format(rateExcha * d));
                        finalAmount = (rateExcha * d);

                        BigDecimal bdb1 = BigDecimal.valueOf(finalAmount);
                        bdb1 = bdb1.setScale(2, RoundingMode.HALF_UP);
                        finalAmount = bdb1.doubleValue();
                        if (messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY")
                                || messCurrency.equalsIgnoreCase("AED")
                                || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK")
                                || messCurrency.equalsIgnoreCase("DKK")
                                || messCurrency.equalsIgnoreCase("SAR")) {


                            System.out.println("Here uuuuuuuuu 100");
                            // finalAmount = finalAmount = Double.parseDouble(df.format(finalAmount/100));
                            finalAmount = (finalAmount / 100);

                            BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                            bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                            finalAmount = bdb.doubleValue();
                        }
                        finalrate = rateExcha;
                    } else if (messCurrency.equalsIgnoreCase("ETB") && !ACCY.equalsIgnoreCase("ETB")) {

                        System.out.println("Here Then");
                        double buyexRate = valueBuyRate(messValueDate, ACCY);
                        System.out.println("Buy rate" + buyexRate);
                        // finalAmount = Double.parseDouble(df.format(d/buyexRate));
                        finalAmount = (d / buyexRate);

                        BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                        bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                        finalAmount = bdb.doubleValue();

                        System.out.println("Now final amount" + finalAmount);
                        finalrate = buyexRate;
                        System.out.println("Final Rate      " + finalrate);

                    }

                    else {

                        double tempETB = d * rateExcha;
                        System.out.println("Check here!!!!");
                        sellRate = valueSellRate(messValueDate, ACCY);
                        System.out.println("sell rate after" + sellRate);
                        System.out.println("date" + messValueDate);
                        System.out.println("salerate 123 " + sellRate);
                        System.out.println(tempETB);
                        // finalAmount = Double.parseDouble(df.format(tempETB / sellRate));
                        finalAmount = (tempETB / sellRate);

                        BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                        bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                        finalAmount = bdb.doubleValue();

                        if (messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY")
                                || messCurrency.equalsIgnoreCase("AED")
                                || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK")
                                || messCurrency.equalsIgnoreCase("DKK")
                                || messCurrency.equalsIgnoreCase("SAR")) {



                            System.out.println("Here nnnnnnnnn 100");
                            // finalAmount = finalAmount = Double.parseDouble(df.format(finalAmount/100));
                            finalAmount = (finalAmount / 100);

                            BigDecimal bda = BigDecimal.valueOf(finalAmount);
                            bda = bda.setScale(2, RoundingMode.HALF_UP);
                            finalAmount = bda.doubleValue();
                        }
                        System.out.println(finalAmount + "  final amount");
                        System.out.println("date" + messValueDate);
                        finalrate = sellRate;
                        System.out.println("buy rate 123 " + finalrate);

                    }
                }

                //////
            }

        } else {

            AC_DESC = "";
            CUST_AC_NO = "";
            BRANCH_CODE = "";
            ACCOUNT_CLASS = "";
            ACCY = "";
            CUST_NO = "";
        }
    }

    public void saveMeesage(TblTmp temp) throws ParseException {

        FacesMessage message = null;

        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        RefreshableKeycloakSecurityContext c = (RefreshableKeycloakSecurityContext) request

                .getAttribute(KeycloakSecurityContext.class.getName());

        String usern = c.getToken().getPreferredUsername();

        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        referenceno = paramMap.get("id");

        System.out.println("referenceno=========" + referenceno);

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

        // tempitems.setIsDashenBranch((Integer.toString(otherbanktrue)));
        realData.setCountryOfOrigin(temp.getCountryOfOrigin());

        System.out.println(temp.getAccountType() + "-------------from last page reference ");
        // fromLastRecord = swiftmessagerService.findByReference(temp.getReference());
        // System.out.println(fromLastRecord.get(0).toString()+"----------------last
        // Record");
        // fromLastRecord.get(0).setCreatedBy(usern);
        // fromLastRecord.get(0).setRegistrationDate(new Date());
        // temp.setRate(rateExcha);
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

        String banksReferenceString = String.format("%s%s/%s", "DB/FINC/", String.format(FORMAT, current), "24");
        ourReference = "DB/FINC/" + lastref + 1 + "/" + formattedDate;
        System.out.println(ourReference + "  Reference");
        realData.setBankReference(banksReferenceString);
        realData.setRecordState(current);
        // temp.setCountryOfOrigin(othercountryOrigin);
        swiftmessagerService.save(realData);
        System.out.println(realData.getId() + "message id");
        newTransaction = new SwiftTransaction();
        if (newTransaction != null) {

            System.out.println(CUST_NO + "CUST NUMBER");
            String customerNumber = accountRestClient.getAccountInfo(realData.getAccountNumber()).getCUST_NO();
            Customer cust = accountRestClient.getCustomeCategory(customerNumber);
            newTransaction.setMessage(realData);

            newTransaction.setCustomerType(cust.getCUSTOMER_CATEGORY());
            newTransaction.setCreatedBy(usern);
            newTransaction.setStatus("Pending");
            newTransaction.setTransactionNumber("");
            newTransaction.setFcyRate(finalrate);
            if (newaccrnd) {

                newTransaction.setOtherAccountNumber(accountnum);
                newTransaction.setOtherAccountName(AC_DESC);
                newTransaction.setOtherAccountCurrency(ACCY);

                if (ACCY.equalsIgnoreCase("ETB")) {
                    System.out.println("test");
                    newTransaction.setBirrAmount(finalAmount);
                } else {
                    newTransaction.setFcyAmount(finalAmount);
                }

            } else {

                if (newTransaction.getMessage().getAccountNumberCurrency().equalsIgnoreCase("ETB")) {
                    newTransaction.setBirrAmount(finalAmount);
                } else {
                    System.out.println("USED");
                    newTransaction.setFcyAmount(finalAmount);

                    System.out.println(finalAmount);
                }
            }

            SwiftTransaction trans = swifttransactionservice.save(newTransaction);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Data saved", "Data has been saved successfully..." + trans.getMessage().getBankReference()));

        }
        newaccrnd = false;
        accountnum = null;
        AC_DESC = null;
        ACCY = null;
        BRANCH_CODE = null;
        sellRate = null;
        finalAmount = 0.0;
        temp = null;
        newTransaction = null;
        reset();
        fetchedDataController.clearSearch();

    }

    public void changeRate() {
        System.out.println("Changed Sell Rate=======" + sellRate);

        double tempETB = Double.parseDouble(messAmount) * Double.parseDouble(messRate);

        System.out.println("Changed Sell Rate333=======" + sellRate);

        finalAmount = (tempETB / sellRate);

        BigDecimal bd = BigDecimal.valueOf(finalAmount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        finalAmount = bd.doubleValue();

        if (messCurrency.equalsIgnoreCase("CHF") || messCurrency.equalsIgnoreCase("JPY")
                    || messCurrency.equalsIgnoreCase("AED")
                    || messCurrency.equalsIgnoreCase("SEK") || messCurrency.equalsIgnoreCase("NOK")
                    || messCurrency.equalsIgnoreCase("DKK")
                    || messCurrency.equalsIgnoreCase("SAR")) {

                        System.out.println("HERE   7");
                        finalAmount = (finalAmount / 100);
                BigDecimal bdb = BigDecimal.valueOf(finalAmount);
                bdb = bdb.setScale(2, RoundingMode.HALF_UP);
                finalAmount = bdb.doubleValue();
                // finaletb = Double.parseDouble(df.format(finaletb / 100));
            }

        // finalAmount = Double.parseDouble(df.format(tempETB / sellRate));
        finalrate = sellRate;

    }

    public void reset() throws ParseException {

        newaccrnd = false;
        accountnum = null;
        RAC_DESC = null;
        RACCY = null;
        BRANCH_CODE = null;
        sellRate = null;
        finalAmount = getFinalAmount();
        etbaccount = new String();
        newTransaction = null;
        retaAcc = false;
        AC_DESC = "";

        ACCY = "";
        relatedAccountInfo = new ArrayList<>();

        fcyRate = 0.0;
        etbRate = 0.0;
        finalRFcy = 0.0;
        finalretafcy = 0.0;
        finaletb = 0.0;
        finalRate = 0.0;
        fcy_exrate = 0.0;
    }

    public void saveTransaction(SwiftTransaction tempTransaction) {

    }

    /**
     * @return boolean return the retaAcc
     */
    public boolean isRetaAcc() {
        return retaAcc;
    }

    /**
     * @param retaAcc the retaAcc to set
     */
    public void setRetaAcc(boolean retaAcc) {
        this.retaAcc = retaAcc;
    }

}