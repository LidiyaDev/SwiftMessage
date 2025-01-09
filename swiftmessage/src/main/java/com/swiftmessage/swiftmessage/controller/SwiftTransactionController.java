package com.swiftmessage.swiftmessage.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import com.swiftmessage.swiftmessage.dto.BatchMaster;
import com.swiftmessage.swiftmessage.dto.MultiJrnl;
import com.swiftmessage.swiftmessage.dto.RTRequest;
import com.swiftmessage.swiftmessage.dto.RTResponse;
import com.swiftmessage.swiftmessage.dto.Request;
import com.swiftmessage.swiftmessage.dto.Response;
import com.swiftmessage.swiftmessage.entity.FetchedData;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.entity.TblCorrespondentBank;
import com.swiftmessage.swiftmessage.service.PaymentRestClient;
import com.swiftmessage.swiftmessage.service.SwiftTransactionService;
import com.swiftmessage.swiftmessage.service.TblCorrespondentBankService;



import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.FetchedDataService;
import com.swiftmessage.swiftmessage.dto.AccountInfo;

@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SwiftTransactionController {

    @Autowired
    SwiftTransactionService swifttransactionservice;

    @Autowired
    TblCorrespondentBankService corrService;

    @Autowired
    PaymentRestClient paymentClientrt;

    @Autowired
    FetchedDataService fetchedDataService;

    private SwiftTransaction selected;
    private List<SwiftTransaction> items;
    private List<SwiftTransaction> retaitems;
    private List<SwiftTransaction> nretaitems;
    private List<SwiftTransaction> mulselect;

    private String username;
    private double lcyAmount;

    private String otherAccNo;
    private String otherAccName;
    private String otherAccCurrency;

    private String originalAccNo;
    private String orginalAccName;
    private String orginalAccCurrency;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (selected != null) {
            otherAccCurrency = new String();
            otherAccName = new String();
            otherAccNo = new String();
            List<FetchedData> originalData = fetchedDataService.byReference(selected.getMessage().getReference());
            if (originalData.size() == 1) {
                originalAccNo = originalData.get(0).getAccountNumber();
                orginalAccName = originalData.get(0).getBeneficiary();
                if (originalData.get(0).getAccountNumber().equalsIgnoreCase(selected.getMessage().getAccountNumber())) {
                    if (selected.getOtherAccountNumber() != null) {
                        otherAccNo = selected.getOtherAccountNumber();
                        otherAccCurrency = selected.getOtherAccountCurrency();
                        otherAccName = selected.getOtherAccountName();

                    }
                } else {
                    otherAccNo = selected.getMessage().getAccountNumber();
                    otherAccCurrency = selected.getMessage().getAccountNumberCurrency();
                    otherAccName = selected.getMessage().getBeneficiary();
                }

            }
        }

    }

    public String getOtherAccNo() {
        if (selected != null) {

            otherAccNo = new String();
            List<FetchedData> originalData = fetchedDataService.byReference(selected.getMessage().getReference());
            if (originalData.size() == 1) {

                if (originalData.get(0).getAccountNumber().equalsIgnoreCase(selected.getMessage().getAccountNumber())) {
                    if (selected.getOtherAccountNumber() != null) {
                        otherAccNo = selected.getOtherAccountNumber();

                    }
                } else {
                    otherAccNo = selected.getMessage().getAccountNumber();

                }

            }
        }

        return otherAccNo;
    }

    public void setOtherAccNo(String otherAccNo) {
        this.otherAccNo = otherAccNo;
    }

    public String getOtherAccName() {
        if (selected != null) {

            otherAccName = new String();

            List<FetchedData> originalData = fetchedDataService.byReference(selected.getMessage().getReference());
            if (originalData.size() == 1) {

                if (originalData.get(0).getAccountNumber().equalsIgnoreCase(selected.getMessage().getAccountNumber())) {
                    if (selected.getOtherAccountNumber() != null) {

                        otherAccName = accountRestClient.getAccountInfo(otherAccNo).getAC_DESC();

                    }
                } else {

                    otherAccName = accountRestClient.getAccountInfo(otherAccNo).getAC_DESC();
                }

            }
        }
        return otherAccName;
    }

    public void setOtherAccName(String otherAccName) {
        this.otherAccName = otherAccName;
    }

    public String getOtherAccCurrency() {
        if (selected != null) {
            otherAccCurrency = new String();

            List<FetchedData> originalData = fetchedDataService.byReference(selected.getMessage().getReference());
            if (originalData.size() == 1) {

                if (originalData.get(0).getAccountNumber().equalsIgnoreCase(selected.getMessage().getAccountNumber())) {
                    if (selected.getOtherAccountNumber() != null) {

                        otherAccCurrency = accountRestClient.getAccountInfo(otherAccNo).getCCY();

                    }
                } else {

                    otherAccCurrency = accountRestClient.getAccountInfo(otherAccNo).getCCY();

                }

            }
        }
        return otherAccCurrency;
    }

    public void setOtherAccCurrency(String otherAccCurrency) {
        this.otherAccCurrency = otherAccCurrency;
    }

    public String getOriginalAccNo() {
        if (selected != null) {

            List<FetchedData> originalData = fetchedDataService.byReference(selected.getMessage().getReference());
            if (originalData.size() > 0) {
                originalAccNo = originalData.get(0).getAccountNumber();

            }
        }
        return originalAccNo;
    }

    public void setOriginalAccNo(String originalAccNo) {
        this.originalAccNo = originalAccNo;
    }

    public String getOrginalAccName() {
        if (selected != null) {

            List<FetchedData> originalData = fetchedDataService.byReference(selected.getMessage().getReference());
            if (originalData.size() > 0) {
                originalAccNo = originalData.get(0).getAccountNumber();
                if (accountRestClient.getAccountInfo(originalAccNo) != null) {
                    orginalAccName = accountRestClient.getAccountInfo(originalAccNo).getAC_DESC();

                }

            }
        }
        return orginalAccName;
    }

    public void setOrginalAccName(String orginalAccName) {
        this.orginalAccName = orginalAccName;
    }

    public String getOrginalAccCurrency() {
        if (selected != null) {

            List<FetchedData> originalData = fetchedDataService.byReference(selected.getMessage().getReference());
            if (originalData.size() > 0) {
                originalAccNo = originalData.get(0).getAccountNumber();
                if (accountRestClient.getAccountInfo(originalAccNo) != null) {

                    orginalAccCurrency = accountRestClient.getAccountInfo(originalAccNo).getCCY();
                }

            }
        }
        return orginalAccCurrency;
    }

    public void setOrginalAccCurrency(String orginalAccCurrency) {
        this.orginalAccCurrency = orginalAccCurrency;
    }

    String baseUrl2 = "http://192.168.12.47:6070/getByAccountNo/";
    WebClient webclient2 = WebClient.create(baseUrl2);
    AccountRestClient accountRestClient = new AccountRestClient(webclient2);
    // @Autowired
    // AccountRestClient accountRestClient;

    DecimalFormat df = new DecimalFormat("0.00");

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLcyAmount() {
        if (selected != null) {
            if (selected.getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                lcyAmount = selected.getMessage().getFcyAmount();
            } else {
                lcyAmount = selected.getMessage().getRate() * selected.getMessage().getFcyAmount();
            }

        }

        return lcyAmount;
    }

    public void setLcyAmount(double lcyAmount) {
        this.lcyAmount = lcyAmount;
    }

    public List<SwiftTransaction> getMulselect() {
        return mulselect;
    }

    public void setMulselect(List<SwiftTransaction> mulselect) {
        this.mulselect = mulselect;
    }

    public List<SwiftTransaction> getNretaitems() {
        return nretaitems;
    }

    public void setNretaitems(List<SwiftTransaction> nretaitems) {
        this.nretaitems = nretaitems;
    }

    public List<SwiftTransaction> getRetaitems() {

        return retaitems;
    }

    public void setRetaitems(List<SwiftTransaction> retaitems) {
        this.retaitems = retaitems;
    }

    public SwiftTransaction getSelected() {
        return selected;
    }

    public void setSelected(SwiftTransaction selected) {
        this.selected = selected;
    }

    public List<SwiftTransaction> getItems() {
        return items;
    }

    public void setItems(List<SwiftTransaction> items) {
        this.items = items;
    }

    public void prepareSelect(SwiftTransaction item) {
        if (item != null) {

            System.out.println("test item" + item);
            selected = item;
        }

    }

    @RequestMapping("/Transaction/View")
    public String list() {
        nretaitems = new ArrayList<>();
        nretaitems = swifttransactionservice.findByStatusNreta();
        System.out.println(nretaitems + "Items");
        return "forward:/Transaction/index.xhtml";
    }

    @RequestMapping("/Message/MakerView")
    public String makerlist() {
        nretaitems = new ArrayList<>();

       

        

        nretaitems = swifttransactionservice.findAll();
        if (nretaitems == null) {
            nretaitems = new ArrayList<>();
        }

        return "forward:/Transaction/MakerView.xhtml";
    }

    @RequestMapping("/Transaction/PView")
    public String pendinglist() {
        nretaitems = new ArrayList<>();
        nretaitems = swifttransactionservice.findByStatusNretaPending();
        System.out.println(nretaitems + "Items");
        return "forward:/Transaction/index.xhtml";
    }

    @RequestMapping("/Transaction/reta")
    public String reta() {
        retaitems = new ArrayList<>();
        retaitems = swifttransactionservice.findByAccountType();

        System.out.println(retaitems + "Items");
        return "forward:/Transaction/retentionTransaction.xhtml";
    }

    @RequestMapping("/Transaction/Preta")
    public String pendingreta() {
        retaitems = new ArrayList<>();
        retaitems = swifttransactionservice.findByAccountTypePending();

        System.out.println(retaitems + "Items");
        return "forward:/Transaction/retentionTransaction.xhtml";
    }

    @RequestMapping("/Transaction/new")
    public String newProfile() {
        selected = new SwiftTransaction();

        return "forward:/Message/message.xhtml";
    }

    public void save() {
        if (selected != null) {

            selected.setCreatedBy("Lidiyam");

            swifttransactionservice.save(selected);

        } else
            System.out.println("error");

    }

    public String rejectTransaction() {
        for (int i = 0; i < mulselect.size(); i++) {
            mulselect.get(i).setStatus("Reject");
            swifttransactionservice.update(mulselect.get(i));

            items = swifttransactionservice.findAll();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Transaction Rejected", "Transaction Rejected"));
            System.out.println("updated");

        }

        return "forward:/Transaction/View";
    }

    public String autorizeFincTransaction() throws ParseException, DatatypeConfigurationException {

        String AC_DESC;
        String CUST_AC_NO;
        String BRANCH_CODE;
        String ACCOUNT_CLASS;
        String ACCY;
        String CUST_NO;
        String txCode;

        System.out.println(mulselect + "   multiple");
        for (int i = 0; i < mulselect.size(); i++) {

            if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                txCode = "934";
            } else {
                txCode = "075";
            }
            String correspondentAccount = null;
            List<TblCorrespondentBank> correspondentBank = corrService
                    .findBycodeandname(mulselect.get(i).getMessage().getCorrespondentBank());
            System.out.println(mulselect.get(i).getMessage().getCorrespondentBank() + "Code");
            System.out.println(correspondentBank + "data");
            FacesContext context = FacesContext.getCurrentInstance();

            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

            RefreshableKeycloakSecurityContext c = (RefreshableKeycloakSecurityContext) request

                    .getAttribute(KeycloakSecurityContext.class.getName());
            username = c.getToken().getPreferredUsername();
            if (correspondentBank.size() > 0) {
                if (mulselect.get(i).getStatus().equalsIgnoreCase("Liquidated")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Failed to Save", "Transaction Already Authorized..."));
                    break;
                }
                for (TblCorrespondentBank tblCorrespondentBank : correspondentBank) {
                    if (tblCorrespondentBank.getCurrency()
                            .equalsIgnoreCase(mulselect.get(i).getMessage().getCurrencyId())) {
                        correspondentAccount = correspondentBank.get(0).getBankAccount();
                    }
                }

                System.out.println(correspondentAccount + "  account");
                if (mulselect.get(i).getOtherAccountNumber() == null) {

                    if (mulselect.get(i).getMessage().getCurrencyId()
                            .equalsIgnoreCase(mulselect.get(i).getMessage().getAccountNumberCurrency())
                            && !mulselect.get(i).getMessage().getAccountNumberCurrency().equalsIgnoreCase("ETB")) {

                        System.out.println("   same currency");
                        Request transaction = new Request();

                        transaction.setBATCH_NO("752m");
                        transaction.setCURR_NO(new BigDecimal(1));

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateFormat.format(mulselect.get(i).getMessage().getValueDate());

                        transaction.setVALUE_DATE(strDate);
                        System.out.println("DATE=======" + transaction.getVALUE_DATE());
                        transaction.setBRANCH_CODE("006");
                        transaction.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        transaction.setAUTHSTAT("A");
                        transaction.setCHECHKERID("UAT9");

                        System.out.println("   test 123");
                        List<MultiJrnl> mj = new ArrayList();
                        // Multi Jornal Task

                        MultiJrnl mj1 = new MultiJrnl();

                        mj1.setSERIAL_NO(new BigDecimal(2));
                        mj1.setDR_CR("D");
                        AccountInfo accountDto4 = accountRestClient.getAccountInfo(correspondentAccount);
                        mj1.setBRANCH_CODE(accountDto4.getBRANCH_CODE());
                        mj1.setACCOUNT(correspondentAccount);
                        mj1.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));

                        /// mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));

                        mj1.setTXN_CODE(txCode);
                        mj1.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        mj1.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // mj1.setCUSTOMER(accountDto4.getCUST_NO());
                        mj1.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                        // System.out.println(transaction.getDetbsJrnlTxnDetail());
                        // transaction.getDetbsJrnlTxnDetail().add(mj1);
                        mj.add(mj1);
                        System.out.println("   test 1234");

                        System.out.println(mulselect.get(i) + " mul data " + i + "Round");

                        MultiJrnl fmj2 = new MultiJrnl();
                        fmj2.setSERIAL_NO(new BigDecimal(3));
                        fmj2.setDR_CR("C");

                        fmj2.setACCORGL("A");

                        System.out.println("No Finc Cross currency other account");
                        fmj2.setACCOUNT(mulselect.get(i).getMessage().getAccountNumber());
                        fmj2.setCCY(mulselect.get(i).getMessage().getAccountNumberCurrency());

                        if (mulselect.get(i).getMessage().getAccountNumberCurrency().equalsIgnoreCase("ETB")) {
                            AccountInfo accountDto = accountRestClient
                                    .getAccountInfo(mulselect.get(i).getMessage().getAccountNumber());
                            fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                            fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                            fmj2.setTXN_CODE(txCode);
                        } else {
                            AccountInfo accountDto = accountRestClient
                                    .getAccountInfo(mulselect.get(i).getMessage().getAccountNumber());
                            fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                            fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                            fmj2.setTXN_CODE(txCode);
                        }

                        fmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        fmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // fmj2.setCUSTOMER(accountDto.getCUST_NO());
                        fmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getFcyRate()));
                        mj.add(fmj2);

                        System.out.println(mj.get(i).getACCOUNT() + "Account ");
                        System.out.println("   test 1235");

                        // transaction.getDetbsJrnlTxnDetail().add(mj2);

                        transaction.setDetbsJrnlTxnDetail(mj);

                        double rounddebitBirrAmount;
                        double debitBirrAmount;
                        if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                            debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount();
                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        } else {
                            if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("CHF")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("JPY")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("AED")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SEK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("NOK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("DKK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SAR")) {
                                debitBirrAmount = (mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate()) / 100;
                            } else {
                                debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate();
                            }

                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        }
                        System.out.println(rounddebitBirrAmount + "finc debitBirrAmount");

                        double creditBirrAmount = (mulselect.get(i).getFcyAmount() * mulselect.get(i).getFcyRate());

                        double roundcreditBirrAmount = Math.round(creditBirrAmount * Math.pow(10, 2))
                                / Math.pow(10, 2);
                        System.out.println(roundcreditBirrAmount + "finc creditBirrAmount");
                        double difference = rounddebitBirrAmount - roundcreditBirrAmount;

                        double rounddifference = Math.round(difference * Math.pow(10, 2)) / Math.pow(10, 2);

                        System.out.println(rounddifference + "difference");
                        if (rounddifference == 0) {
                            System.out.println("Batch Balanced");
                        }

                        else {
                            MultiJrnl bbmj = new MultiJrnl();
                            bbmj.setSERIAL_NO(new BigDecimal(4));

                            if (rounddifference < 0) {

                                bbmj.setDR_CR("D");

                                bbmj.setACCOUNT("350330008");

                            } else if (rounddifference > 0) {

                                bbmj.setDR_CR("C");

                                bbmj.setACCOUNT("435120005");

                            }

                            AccountInfo accountDto3 = accountRestClient.getAccountInfo("350430005");
                            bbmj.setBRANCH_CODE("237");
                            bbmj.setACCORGL("G");
                            // bbmj.setACCOUNT("350430005");
                            bbmj.setCCY("ETB");
                            bbmj.setAMOUNT(BigDecimal.valueOf(Math.abs(rounddifference)));
                            System.out.println(bbmj.getAMOUNT());

                            bbmj.setTXN_CODE(txCode);

                            bbmj.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                    + mulselect.get(i).getMessage().getCountryOfOrigin());
                            // bbmj.setCUSTOMER(accountDto3.getCUST_NO());
                            bbmj.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                            mj.add(bbmj);
                            System.out.println("   test 1235");

                        }

                        System.out.println(transaction.getDetbsJrnlTxnDetail() + "   test DetbsJrnlTxnDetail");

                        BatchMaster bm1 = new BatchMaster();
                        bm1.setBATCH_NUMBER("752m");
                        bm1.setDESCRIPTION("752m");
                        Double testamount = Double.parseDouble(df.format(mulselect.get(i).getMessage().getFcyAmount()
                                * mulselect.get(i).getMessage().getRate()));

                        bm1.setDEBIT(BigDecimal.valueOf(testamount));

                        System.out.println("debit " + BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount())
                                .multiply(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate())));
                        bm1.setCREDIT(BigDecimal.valueOf(testamount));
                        bm1.setBALANCING("Y");

                        transaction.setDevwsBatchMaster(bm1);

                        Response response = paymentClientrt.autorizeTransaction(transaction);
                        System.out.println(response.getREFERENCE_NO() + "   reference");

                        if (response.getMSGSTAT().equalsIgnoreCase("SUCCESS")) {
                            System.out.println("reference " + response.getREFERENCE_NO());
                            System.out.println("batch number  " + response.getBATCH_NO());
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Transaction Authorized",
                                            "Successfully Saved and Authorized..." + response.getREFERENCE_NO()));
                            mulselect.get(i).setStatus("Liquidated");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setUpdatedDate(new Date());
                            mulselect.get(i).setTransactionNumber(response.getREFERENCE_NO());
                            mulselect.get(i).setDeletedBy("");
                            mulselect.get(i).setMessageid(response.getMSGID());
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Failed to Save", "Failed to Save the transaction..."));
                            mulselect.get(i).setStatus("Cancelled");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setDeletedBy(response.getERROR());
                        }

                        swifttransactionservice.update(mulselect.get(i));

                        System.out.println(response + "   test 123");
                        System.out.println("   same currency");

                    }

                    else if (mulselect.get(i).getMessage().getAccountNumberCurrency().equalsIgnoreCase("ETB")) {
                        System.out.println("   ETB Currency");
                        Request transaction = new Request();

                        transaction.setBATCH_NO("752m");
                        transaction.setCURR_NO(new BigDecimal(1));

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateFormat.format(mulselect.get(i).getMessage().getValueDate());

                        transaction.setVALUE_DATE(strDate);
                        System.out.println("DATE=======" + transaction.getVALUE_DATE());
                        transaction.setBRANCH_CODE("006");
                        transaction.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        transaction.setAUTHSTAT("A");
                        transaction.setCHECHKERID("UAT9");

                        System.out.println("   test 123");
                        List<MultiJrnl> mj = new ArrayList();
                        // Multi Jornal Task

                        MultiJrnl mj1 = new MultiJrnl();

                        mj1.setSERIAL_NO(new BigDecimal(2));
                        mj1.setDR_CR("D");
                        AccountInfo accountDto4 = accountRestClient.getAccountInfo(correspondentAccount);
                        mj1.setBRANCH_CODE(accountDto4.getBRANCH_CODE());
                        mj1.setACCOUNT(correspondentAccount);
                        mj1.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));

                        /// mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));
                        mj1.setTXN_CODE(txCode);
                        mj1.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        mj1.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // mj1.setCUSTOMER(accountDto4.getCUST_NO());
                        mj1.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                        // System.out.println(transaction.getDetbsJrnlTxnDetail());
                        // transaction.getDetbsJrnlTxnDetail().add(mj1);
                        mj.add(mj1);
                        System.out.println("   test 1234");

                        System.out.println(mulselect.get(i) + " mul data " + i + "Round");

                        MultiJrnl fmj2 = new MultiJrnl();
                        fmj2.setSERIAL_NO(new BigDecimal(3));
                        fmj2.setDR_CR("C");

                        fmj2.setACCORGL("A");

                        System.out.println("No Finc Cross currency other account");
                        fmj2.setACCOUNT(mulselect.get(i).getMessage().getAccountNumber());
                        fmj2.setCCY(mulselect.get(i).getMessage().getAccountNumberCurrency());
                        fmj2.setBRANCH_CODE(mulselect.get(i).getMessage().getDashenBranch());
                        fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));

                        fmj2.setTXN_CODE(txCode);
                        fmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        fmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());
                        AccountInfo accountDto = accountRestClient
                                .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                        // fmj2.setCUSTOMER(accountDto.getCUST_NO());
                        fmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getFcyRate()));
                        mj.add(fmj2);

                        System.out.println(mj.get(i).getACCOUNT() + "Account ");
                        System.out.println("   test 1235");

                        // transaction.getDetbsJrnlTxnDetail().add(mj2);

                        transaction.setDetbsJrnlTxnDetail(mj);

                        double rounddebitBirrAmount;
                        double debitBirrAmount;
                        if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                            debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount();
                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        } else {
                            if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("CHF")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("JPY")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("AED")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SEK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("NOK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("DKK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SAR")) {
                                debitBirrAmount = (mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate()) / 100;
                            } else {
                                debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate();
                            }

                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        }
                        System.out.println(rounddebitBirrAmount + "finc debitBirrAmount");
                        double creditBirrAmount = (mulselect.get(i).getBirrAmount());
                        double roundcreditBirrAmount = Math.round(creditBirrAmount * Math.pow(10, 2))
                                / Math.pow(10, 2);

                        System.out.println(roundcreditBirrAmount + "finc creditBirrAmount");
                        double difference = rounddebitBirrAmount - roundcreditBirrAmount;
                        double rounddifference = Math.round(difference * Math.pow(10, 2)) / Math.pow(10, 2);

                        System.out.println(rounddifference + "difference");
                        if (rounddifference == 0) {
                            System.out.println("Batch Balanced");
                        }

                        else {
                            MultiJrnl bbmj = new MultiJrnl();
                            bbmj.setSERIAL_NO(new BigDecimal(4));

                            if (rounddifference < 0) {

                                bbmj.setDR_CR("D");

                                bbmj.setACCOUNT("350330008");

                            } else if (rounddifference > 0) {

                                bbmj.setDR_CR("C");

                                bbmj.setACCOUNT("435120005");

                            }

                            AccountInfo accountDto3 = accountRestClient.getAccountInfo("350430005");
                            bbmj.setBRANCH_CODE("237");
                            bbmj.setACCORGL("G");
                            // bbmj.setACCOUNT("350430005");
                            bbmj.setCCY("ETB");
                            bbmj.setAMOUNT(BigDecimal.valueOf(Math.abs(rounddifference)));
                            System.out.println(bbmj.getAMOUNT());

                            bbmj.setTXN_CODE(txCode);

                            bbmj.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                    + mulselect.get(i).getMessage().getCountryOfOrigin());
                            // bbmj.setCUSTOMER(accountDto3.getCUST_NO());
                            bbmj.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                            mj.add(bbmj);
                            System.out.println("   test 1235");

                        }

                        System.out.println(transaction.getDetbsJrnlTxnDetail() + "   test DetbsJrnlTxnDetail");

                        BatchMaster bm1 = new BatchMaster();
                        bm1.setBATCH_NUMBER("752m");
                        bm1.setDESCRIPTION("752m");
                        Double testamount = Double.parseDouble(df.format(mulselect.get(i).getMessage().getFcyAmount()
                                * mulselect.get(i).getMessage().getRate()));

                        bm1.setDEBIT(BigDecimal.valueOf(testamount));

                        System.out.println("debit " + BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount())
                                .multiply(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate())));
                        bm1.setCREDIT(BigDecimal.valueOf(testamount));
                        bm1.setBALANCING("Y");

                        transaction.setDevwsBatchMaster(bm1);

                        Response response = paymentClientrt.autorizeTransaction(transaction);
                        System.out.println(response.getREFERENCE_NO() + "   reference");

                        if (response.getMSGSTAT().equalsIgnoreCase("SUCCESS")) {
                            System.out.println("reference " + response.getREFERENCE_NO());
                            System.out.println("batch number  " + response.getBATCH_NO());
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Transaction Authorized",
                                            "Successfully Saved and Authorized..." + response.getREFERENCE_NO()));
                            mulselect.get(i).setStatus("Liquidated");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setUpdatedDate(new Date());
                            mulselect.get(i).setTransactionNumber(response.getREFERENCE_NO());
                            mulselect.get(i).setDeletedBy("");
                            mulselect.get(i).setMessageid(response.getMSGID());
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Failed to Save", "Failed to Save the transaction..."));
                            mulselect.get(i).setStatus("Cancelled");
                            mulselect.get(i).setUpdatedBy(username);

                            mulselect.get(i).setDeletedBy(response.getERROR());
                        }

                        swifttransactionservice.update(mulselect.get(i));

                        System.out.println(response + "   test 123");
                        System.out.println("   same currency");

                    } else {
                        Request transaction = new Request();

                        transaction.setBATCH_NO("752m");
                        transaction.setCURR_NO(new BigDecimal(1));

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateFormat.format(mulselect.get(i).getMessage().getValueDate());

                        transaction.setVALUE_DATE(strDate);
                        transaction.setBRANCH_CODE("006");
                        // transaction.setCCY("USD");

                        transaction.setAUTHSTAT("A");
                        // transaction.setCHECHKERID("UAT9");
                        System.out.println("   test 123");
                        List<MultiJrnl> mj = new ArrayList();
                        // Multi Jornal Task

                        MultiJrnl mj1 = new MultiJrnl();

                        mj1.setSERIAL_NO(new BigDecimal(2));
                        mj1.setDR_CR("D");
                        AccountInfo accountDto4 = accountRestClient.getAccountInfo(correspondentAccount);
                        mj1.setBRANCH_CODE(accountDto4.getBRANCH_CODE());
                        mj1.setACCOUNT(correspondentAccount);
                        mj1.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));

                        /// mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));
                        mj1.setTXN_CODE(txCode);
                        mj1.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        mj1.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // mj1.setCUSTOMER(accountDto4.getCUST_NO());
                        mj1.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                        // System.out.println(transaction.getDetbsJrnlTxnDetail());
                        // transaction.getDetbsJrnlTxnDetail().add(mj1);
                        mj.add(mj1);
                        System.out.println("   test 1234");

                        for (int k = 0; k < mulselect.size(); k++) {
                            System.out.println(mulselect.get(i) + " mul data " + i + "Round");

                            MultiJrnl fmj2 = new MultiJrnl();
                            fmj2.setSERIAL_NO(new BigDecimal(3));
                            fmj2.setDR_CR("C");

                            fmj2.setACCORGL("A");
                            if (mulselect.get(i).getOtherAccountNumber() == null) {
                                fmj2.setACCOUNT(mulselect.get(i).getMessage().getAccountNumber());
                                fmj2.setCCY(mulselect.get(i).getMessage().getAccountNumberCurrency());
                                // if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                                // fmj2.setBRANCH_CODE(mulselect.get(i).getMessage().getDashenBranch());
                                // fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                                // } else {
                                AccountInfo accountDto = accountRestClient
                                        .getAccountInfo(mulselect.get(i).getMessage().getAccountNumber());
                                fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                                fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                                // }
                            } else if (mulselect.get(i).getOtherAccountNumber() != null) {
                                System.out.println(mulselect.get(i).getOtherAccountNumber() + "Other Account");
                                fmj2.setACCOUNT(mulselect.get(i).getOtherAccountNumber());
                                System.out.println(mulselect.get(i).getOtherAccountCurrency() + "Other currency");
                                fmj2.setCCY(mulselect.get(i).getOtherAccountCurrency());
                                if (fmj2.getCCY().equalsIgnoreCase("ETB")) {
                                    System.out.println("Birr");
                                    System.out.println(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                                    AccountInfo accountDto = accountRestClient
                                            .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                                    fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                                    fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                                } else if (!fmj2.getCCY().equalsIgnoreCase("ETB")) {
                                    System.out.println(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                                    AccountInfo accountDto = accountRestClient
                                            .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                                    fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                                    fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                                }

                            }

                            fmj2.setTXN_CODE(txCode);
                            fmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                            fmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                    + mulselect.get(i).getMessage().getCountryOfOrigin());
                            // AccountInfo accountDto = accountRestClient
                            // .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                            // fmj2.setCUSTOMER(accountDto.getCUST_NO());
                            fmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getFcyRate()));
                            mj.add(fmj2);

                            System.out.println(mj.get(i).getACCOUNT() + "Account ");
                            System.out.println("   test 1235");
                        }

                        // transaction.getDetbsJrnlTxnDetail().add(mj2);

                        double rounddebitBirrAmount;
                        double debitBirrAmount;
                        if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                            debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount();
                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        } else {
                            if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("CHF")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("JPY")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("AED")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SEK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("NOK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("DKK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SAR")) {
                                debitBirrAmount = (mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate()) / 100;
                            } else {
                                debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate();
                            }

                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        }
                        System.out.println(rounddebitBirrAmount + "debitBirrAmount");
                        double creditBirrAmount = (mulselect.get(i).getFcyAmount() * mulselect.get(i).getFcyRate());
                        double roundcreditBirrAmount = Math.round(creditBirrAmount * Math.pow(10, 2))
                                / Math.pow(10, 2);

                        System.out.println(roundcreditBirrAmount + "creditBirrAmount");
                        double difference = rounddebitBirrAmount - roundcreditBirrAmount;
                        double rounddifference = Math.round(difference * Math.pow(10, 2)) / Math.pow(10, 2);

                        System.out.println(rounddifference + "differencehgjhgjhjhg");
                        if (rounddifference == 0) {
                            System.out.println("Batch Balanced");
                        }

                        else {
                            MultiJrnl bbmj = new MultiJrnl();
                            bbmj.setSERIAL_NO(new BigDecimal(4));

                            if (rounddifference < 0) {

                                bbmj.setDR_CR("D");

                                bbmj.setACCOUNT("350330008");

                            } else if (rounddifference > 0) {

                                bbmj.setDR_CR("C");

                                bbmj.setACCOUNT("435120005");

                            }

                            AccountInfo accountDto3 = accountRestClient.getAccountInfo("350430005");
                            bbmj.setBRANCH_CODE("237");
                            bbmj.setACCORGL("G");
                            // bbmj.setACCOUNT("350430005");
                            bbmj.setCCY("ETB");
                            bbmj.setAMOUNT(BigDecimal.valueOf(Math.abs(rounddifference)));
                            System.out.println(bbmj.getAMOUNT());

                            bbmj.setTXN_CODE(txCode);

                            bbmj.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                    + mulselect.get(i).getMessage().getCountryOfOrigin());
                            // bbmj.setCUSTOMER(accountDto3.getCUST_NO());
                            bbmj.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                            mj.add(bbmj);
                            System.out.println("   test 1235");

                        }

                        transaction.setDetbsJrnlTxnDetail(mj);

                        System.out.println(transaction.getDetbsJrnlTxnDetail() + "   test DetbsJrnlTxnDetail");

                        BatchMaster bm1 = new BatchMaster();
                        bm1.setBATCH_NUMBER("752m");
                        bm1.setDESCRIPTION("752m");
                        Double testamount = Double.parseDouble(df.format(mulselect.get(i).getMessage().getFcyAmount()
                                * mulselect.get(i).getMessage().getRate()));

                        bm1.setDEBIT(BigDecimal.valueOf(testamount));

                        System.out.println("debit " + BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount())
                                .multiply(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate())));
                        bm1.setCREDIT(BigDecimal.valueOf(testamount));
                        bm1.setBALANCING("Y");

                        transaction.setDevwsBatchMaster(bm1);

                        Response response = paymentClientrt.autorizeTransaction(transaction);
                        System.out.println(response.getREFERENCE_NO() + "   reference");

                        if (response.getMSGSTAT().equalsIgnoreCase("SUCCESS")) {
                            System.out.println("reference " + response.getREFERENCE_NO());
                            System.out.println("batch number  " + response.getBATCH_NO());
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Transaction Authorized", "Successfully Saved and Authorized..."));
                            mulselect.get(i).setStatus("Liquidated");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setUpdatedDate(new Date());
                            mulselect.get(i).setTransactionNumber(response.getREFERENCE_NO());
                            mulselect.get(i).setDeletedBy("");
                            mulselect.get(i).setBatchnumber(response.getBATCH_NO());
                            mulselect.get(i).setMessageid(response.getMSGID());
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Failed to Save", "Failed to Save the transaction..."));
                            mulselect.get(i).setStatus("Cancelled");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setDeletedBy(response.getERROR());
                            // mulselect.get(i).setDeletedBy(rtresponse.getERROR());
                        }

                        swifttransactionservice.update(mulselect.get(i));

                        System.out.println(response + "   test 123");

                    }
                }

                else if (mulselect.get(i).getOtherAccountNumber() != null) {

                    System.out.println(mulselect.get(i).getOtherAccountNumber() + "Other Account");
                    if (mulselect.get(i).getMessage().getCurrencyId()
                            .equalsIgnoreCase(mulselect.get(i).getOtherAccountCurrency())) {
                        Request transaction = new Request();

                        transaction.setBATCH_NO("752m");
                        transaction.setCURR_NO(new BigDecimal(1));

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateFormat.format(mulselect.get(i).getMessage().getValueDate());

                        transaction.setVALUE_DATE(strDate);
                        System.out.println("DATE=======" + transaction.getVALUE_DATE());
                        transaction.setBRANCH_CODE("006");
                        transaction.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        transaction.setAUTHSTAT("A");
                        transaction.setCHECHKERID("UAT9");

                        System.out.println("   test 123");
                        List<MultiJrnl> mj = new ArrayList();
                        // Multi Jornal Task

                        MultiJrnl mj1 = new MultiJrnl();

                        mj1.setSERIAL_NO(new BigDecimal(2));
                        mj1.setDR_CR("D");
                        AccountInfo accountDto4 = accountRestClient.getAccountInfo(correspondentAccount);
                        mj1.setBRANCH_CODE(accountDto4.getBRANCH_CODE());
                        mj1.setACCOUNT(correspondentAccount);
                        mj1.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));

                        /// mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));
                        mj1.setTXN_CODE(txCode);
                        mj1.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        mj1.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // mj1.setCUSTOMER(accountDto4.getCUST_NO());
                        mj1.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                        // System.out.println(transaction.getDetbsJrnlTxnDetail());
                        // transaction.getDetbsJrnlTxnDetail().add(mj1);
                        mj.add(mj1);
                        System.out.println("   test 1234");

                        System.out.println(mulselect.get(i) + " mul data " + i + "Round");

                        MultiJrnl fmj2 = new MultiJrnl();
                        fmj2.setSERIAL_NO(new BigDecimal(3));
                        fmj2.setDR_CR("C");

                        fmj2.setACCORGL("A");

                        System.out.println("No Finc Cross currency other account");
                        fmj2.setACCOUNT(mulselect.get(i).getOtherAccountNumber());
                        fmj2.setCCY(mulselect.get(i).getOtherAccountCurrency());

                        if (mulselect.get(i).getOtherAccountCurrency().equalsIgnoreCase("ETB")) {
                            AccountInfo accountDto = accountRestClient
                                    .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                            fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                            fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                        } else {
                            AccountInfo accountDto = accountRestClient
                                    .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                            fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                            fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                        }
                        AccountInfo accountDto = accountRestClient
                                .getAccountInfo(mulselect.get(i).getOtherAccountNumber());

                        fmj2.setTXN_CODE(txCode);
                        fmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        fmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        fmj2.setCUSTOMER(accountDto.getCUST_NO());
                        fmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getFcyRate()));
                        mj.add(fmj2);

                        System.out.println(mj.get(i).getACCOUNT() + "Account ");
                        System.out.println("   test 1235");

                        // transaction.getDetbsJrnlTxnDetail().add(mj2);

                        transaction.setDetbsJrnlTxnDetail(mj);

                        double rounddebitBirrAmount;
                        double debitBirrAmount;
                        if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                            debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount();
                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        } else {
                            if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("CHF")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("JPY")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("AED")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SEK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("NOK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("DKK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SAR")) {
                                debitBirrAmount = (mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate()) / 100;
                            } else {
                                debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate();
                            }

                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        }
                        System.out.println(rounddebitBirrAmount + "finc debitBirrAmount");
                        double creditBirrAmount = (mulselect.get(i).getFcyAmount() * mulselect.get(i).getFcyRate());
                        double roundcreditBirrAmount = Math.round(creditBirrAmount * Math.pow(10, 2))
                                / Math.pow(10, 2);

                        System.out.println(roundcreditBirrAmount + "finc creditBirrAmount");
                        double difference = rounddebitBirrAmount - roundcreditBirrAmount;
                        double rounddifference = Math.round(difference * Math.pow(10, 2)) / Math.pow(10, 2);

                        System.out.println(rounddifference + "difference");
                        if (rounddifference == 0) {
                            System.out.println("Batch Balanced");
                        }

                        else {
                            MultiJrnl bbmj = new MultiJrnl();
                            bbmj.setSERIAL_NO(new BigDecimal(4));

                            if (rounddifference < 0) {

                                bbmj.setDR_CR("D");

                                bbmj.setACCOUNT("350330008");

                            } else if (rounddifference > 0) {

                                bbmj.setDR_CR("C");

                                bbmj.setACCOUNT("435120005");

                            }

                            AccountInfo accountDto3 = accountRestClient.getAccountInfo("350430005");
                            bbmj.setBRANCH_CODE("237");
                            bbmj.setACCORGL("G");
                            // bbmj.setACCOUNT("350430005");
                            bbmj.setCCY("ETB");
                            bbmj.setAMOUNT(BigDecimal.valueOf(Math.abs(rounddifference)));
                            System.out.println(bbmj.getAMOUNT());

                            bbmj.setTXN_CODE(txCode);

                            bbmj.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                    + mulselect.get(i).getMessage().getCountryOfOrigin());
                            // bbmj.setCUSTOMER(accountDto3.getCUST_NO());
                            bbmj.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                            mj.add(bbmj);
                            System.out.println("   test 1235");

                        }

                        System.out.println(transaction.getDetbsJrnlTxnDetail() + "   test DetbsJrnlTxnDetail");

                        BatchMaster bm1 = new BatchMaster();
                        bm1.setBATCH_NUMBER("752m");
                        bm1.setDESCRIPTION("752m");
                        Double testamount = Double.parseDouble(df.format(mulselect.get(i).getMessage().getFcyAmount()
                                * mulselect.get(i).getMessage().getRate()));

                        bm1.setDEBIT(BigDecimal.valueOf(testamount));

                        System.out.println("debit " + BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount())
                                .multiply(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate())));
                        bm1.setCREDIT(BigDecimal.valueOf(testamount));
                        bm1.setBALANCING("Y");

                        transaction.setDevwsBatchMaster(bm1);

                        Response response = paymentClientrt.autorizeTransaction(transaction);
                        System.out.println(response.getREFERENCE_NO() + "   reference");

                        if (response.getMSGSTAT().equalsIgnoreCase("SUCCESS")) {
                            System.out.println("reference " + response.getREFERENCE_NO());
                            System.out.println("batch number  " + response.getBATCH_NO());
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Transaction Authorized",
                                            "Successfully Saved and Authorized..." + response.getREFERENCE_NO()));
                            mulselect.get(i).setStatus("Liquidated");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setUpdatedDate(new Date());
                            mulselect.get(i).setTransactionNumber(response.getREFERENCE_NO());
                            mulselect.get(i).setDeletedBy("");
                            mulselect.get(i).setMessageid(response.getMSGID());
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Failed to Save", "Failed to Save the transaction..."));
                            mulselect.get(i).setStatus("Cancelled");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setDeletedBy(response.getERROR());
                        }

                        swifttransactionservice.update(mulselect.get(i));

                        System.out.println(response + "   test 123");
                        System.out.println("   same currency");

                    } else if (mulselect.get(i).getOtherAccountCurrency().equalsIgnoreCase("ETB")) {
                        System.out.println("   ETB Currency");
                        Request transaction = new Request();

                        transaction.setBATCH_NO("752m");
                        transaction.setCURR_NO(new BigDecimal(1));

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateFormat.format(mulselect.get(i).getMessage().getValueDate());

                        transaction.setVALUE_DATE(strDate);
                        System.out.println("DATE=======" + transaction.getVALUE_DATE());
                        transaction.setBRANCH_CODE("006");
                        transaction.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        transaction.setAUTHSTAT("A");
                        transaction.setCHECHKERID("UAT9");

                        System.out.println("   test 123");
                        List<MultiJrnl> mj = new ArrayList();
                        // Multi Jornal Task

                        MultiJrnl mj1 = new MultiJrnl();

                        mj1.setSERIAL_NO(new BigDecimal(2));
                        mj1.setDR_CR("D");
                        AccountInfo accountDto4 = accountRestClient.getAccountInfo(correspondentAccount);
                        mj1.setBRANCH_CODE(accountDto4.getBRANCH_CODE());
                        mj1.setACCOUNT(correspondentAccount);
                        mj1.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));

                        /// mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));
                        mj1.setTXN_CODE(txCode);
                        mj1.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        mj1.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // mj1.setCUSTOMER(accountDto4.getCUST_NO());
                        mj1.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                        // System.out.println(transaction.getDetbsJrnlTxnDetail());
                        // transaction.getDetbsJrnlTxnDetail().add(mj1);
                        mj.add(mj1);
                        System.out.println("   test 1234");

                        System.out.println(mulselect.get(i) + " mul data " + i + "Round");

                        MultiJrnl fmj2 = new MultiJrnl();
                        fmj2.setSERIAL_NO(new BigDecimal(3));
                        fmj2.setDR_CR("C");

                        fmj2.setACCORGL("A");

                        AccountInfo accountDto = accountRestClient
                                .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                        System.out.println("No Finc Cross currency other account");
                        fmj2.setACCOUNT(mulselect.get(i).getOtherAccountNumber());
                        fmj2.setCCY(mulselect.get(i).getOtherAccountCurrency());
                        fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                        fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));

                        fmj2.setTXN_CODE(txCode);
                        fmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        fmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // fmj2.setCUSTOMER(accountDto.getCUST_NO());
                        fmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getFcyRate()));
                        mj.add(fmj2);

                        System.out.println(mj.get(i).getACCOUNT() + "Account ");
                        System.out.println("   test 1235");

                        // transaction.getDetbsJrnlTxnDetail().add(mj2);

                        transaction.setDetbsJrnlTxnDetail(mj);

                        double rounddebitBirrAmount;
                        double debitBirrAmount;
                        if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                            debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount();
                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        } else {
                            if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("CHF")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("JPY")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("AED")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SEK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("NOK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("DKK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SAR")) {
                                debitBirrAmount = (mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate()) / 100;
                            } else {
                                debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate();
                            }

                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        }
                        System.out.println(rounddebitBirrAmount + "finc debitBirrAmount");
                        double creditBirrAmount = (mulselect.get(i).getBirrAmount());
                        double roundcreditBirrAmount = Math.round(creditBirrAmount * Math.pow(10, 2))
                                / Math.pow(10, 2);

                        System.out.println(roundcreditBirrAmount + "finc creditBirrAmount");
                        double difference = rounddebitBirrAmount - roundcreditBirrAmount;
                        double rounddifference = Math.round(difference * Math.pow(10, 2)) / Math.pow(10, 2);

                        System.out.println(rounddifference + "difference");
                        if (rounddifference == 0) {
                            System.out.println("Batch Balanced");
                        }

                        else {
                            MultiJrnl bbmj = new MultiJrnl();
                            bbmj.setSERIAL_NO(new BigDecimal(4));

                            if (rounddifference < 0) {

                                bbmj.setDR_CR("D");

                                bbmj.setACCOUNT("350330008");

                            } else if (rounddifference > 0) {

                                bbmj.setDR_CR("C");

                                bbmj.setACCOUNT("435120005");

                            }

                            AccountInfo accountDto3 = accountRestClient.getAccountInfo("350430005");
                            bbmj.setBRANCH_CODE("237");
                            bbmj.setACCORGL("G");
                            // bbmj.setACCOUNT("350430005");
                            bbmj.setCCY("ETB");
                            bbmj.setAMOUNT(BigDecimal.valueOf(Math.abs(rounddifference)));
                            System.out.println(bbmj.getAMOUNT());

                            bbmj.setTXN_CODE(txCode);

                            bbmj.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                    + mulselect.get(i).getMessage().getCountryOfOrigin());
                            // bbmj.setCUSTOMER(accountDto3.getCUST_NO());
                            bbmj.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                            mj.add(bbmj);
                            System.out.println("   test 1235");

                        }

                        System.out.println(transaction.getDetbsJrnlTxnDetail() + "   test DetbsJrnlTxnDetail");

                        BatchMaster bm1 = new BatchMaster();
                        bm1.setBATCH_NUMBER("752m");
                        bm1.setDESCRIPTION("752m");
                        Double testamount = Double.parseDouble(df.format(mulselect.get(i).getMessage().getFcyAmount()
                                * mulselect.get(i).getMessage().getRate()));

                        bm1.setDEBIT(BigDecimal.valueOf(testamount));

                        System.out.println("debit " + BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount())
                                .multiply(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate())));
                        bm1.setCREDIT(BigDecimal.valueOf(testamount));
                        bm1.setBALANCING("Y");

                        transaction.setDevwsBatchMaster(bm1);

                        Response response = paymentClientrt.autorizeTransaction(transaction);
                        System.out.println(response.getREFERENCE_NO() + "   reference");

                        if (response.getMSGSTAT().equalsIgnoreCase("SUCCESS")) {
                            System.out.println("reference " + response.getREFERENCE_NO());
                            System.out.println("batch number  " + response.getBATCH_NO());
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Transaction Authorized",
                                            "Successfully Saved and Authorized..." + response.getREFERENCE_NO()));
                            mulselect.get(i).setStatus("Liquidated");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setUpdatedDate(new Date());
                            mulselect.get(i).setTransactionNumber(response.getREFERENCE_NO());
                            mulselect.get(i).setDeletedBy("");
                            mulselect.get(i).setMessageid(response.getMSGID());
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Failed to Save", "Failed to Save the transaction..."));
                            mulselect.get(i).setStatus("Cancelled");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setDeletedBy(response.getERROR());
                        }

                        swifttransactionservice.update(mulselect.get(i));

                        System.out.println(response + "   test 123");
                        System.out.println("   same currency");
                    } else {
                        Request transaction = new Request();

                        transaction.setBATCH_NO("752m");
                        transaction.setCURR_NO(new BigDecimal(1));

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateFormat.format(mulselect.get(i).getMessage().getValueDate());

                        transaction.setVALUE_DATE((strDate));
                        transaction.setBRANCH_CODE("006");
                        transaction.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        transaction.setAUTHSTAT("A");
                        transaction.setCHECHKERID("UAT9");
                        System.out.println("   test 123");
                        List<MultiJrnl> mj = new ArrayList();
                        // Multi Jornal Task

                        MultiJrnl mj1 = new MultiJrnl();

                        mj1.setSERIAL_NO(new BigDecimal(2));
                        mj1.setDR_CR("D");
                        AccountInfo accountDto4 = accountRestClient.getAccountInfo(correspondentAccount);
                        mj1.setBRANCH_CODE(accountDto4.getBRANCH_CODE());
                        mj1.setACCOUNT(correspondentAccount);
                        mj1.setCCY(mulselect.get(i).getMessage().getCurrencyId());

                        mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));

                        /// mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));
                        mj1.setTXN_CODE(txCode);
                        mj1.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                        mj1.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                + mulselect.get(i).getMessage().getCountryOfOrigin());

                        // mj1.setCUSTOMER(accountDto4.getCUST_NO());
                        mj1.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                        // System.out.println(transaction.getDetbsJrnlTxnDetail());
                        // transaction.getDetbsJrnlTxnDetail().add(mj1);
                        mj.add(mj1);
                        System.out.println("   test 1234");

                        for (int k = 0; k < mulselect.size(); k++) {
                            System.out.println(mulselect.get(i) + " mul data " + i + "Round");

                            MultiJrnl fmj2 = new MultiJrnl();
                            fmj2.setSERIAL_NO(new BigDecimal(3));
                            fmj2.setDR_CR("C");

                            fmj2.setACCORGL("A");
                            if (mulselect.get(i).getOtherAccountNumber() == null) {
                                System.out.println("No Finc Cross currency other account");
                                fmj2.setACCOUNT(mulselect.get(i).getMessage().getAccountNumber());
                                fmj2.setCCY(mulselect.get(i).getMessage().getAccountNumberCurrency());
                                if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                                    fmj2.setBRANCH_CODE(mulselect.get(i).getMessage().getDashenBranch());
                                    fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                                } else {
                                    AccountInfo accountDto = accountRestClient
                                            .getAccountInfo(mulselect.get(i).getMessage().getAccountNumber());
                                    fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                                    fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                                }
                            } else if (mulselect.get(i).getOtherAccountNumber() != null) {
                                System.out.println(mulselect.get(i).getOtherAccountNumber() + "Other Account");
                                fmj2.setACCOUNT(mulselect.get(i).getOtherAccountNumber());
                                System.out.println(mulselect.get(i).getOtherAccountCurrency() + "Other currency");
                                fmj2.setCCY(mulselect.get(i).getOtherAccountCurrency());
                                if (fmj2.getCCY().equalsIgnoreCase("ETB")) {
                                    System.out.println("Birr");
                                    System.out.println(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                                    AccountInfo accountDto = accountRestClient
                                            .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                                    fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                                    fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));
                                } else if (!fmj2.getCCY().equalsIgnoreCase("ETB")) {
                                    System.out.println(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                                    AccountInfo accountDto = accountRestClient
                                            .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                                    fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                                    fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));
                                }

                            }

                            fmj2.setTXN_CODE(txCode);
                            fmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                            fmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer() + "/"
                                    + mulselect.get(i).getMessage().getCountryOfOrigin());
                            AccountInfo accountDto = accountRestClient
                                    .getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                            // fmj2.setCUSTOMER(accountDto.getCUST_NO());
                            fmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getFcyRate()));
                            mj.add(fmj2);

                            System.out.println(mj.get(i).getACCOUNT() + "Account ");
                            System.out.println("   test 1235");
                        }

                        // transaction.getDetbsJrnlTxnDetail().add(mj2);

                        transaction.setDetbsJrnlTxnDetail(mj);

                        double rounddebitBirrAmount;
                        double debitBirrAmount;
                        if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB")) {
                            debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount();
                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        } else {
                            if (mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("CHF")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("JPY")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("AED")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SEK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("NOK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("DKK")
                                    || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SAR")) {
                                debitBirrAmount = (mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate()) / 100;
                            } else {
                                debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount()
                                        * mulselect.get(i).getMessage().getRate();
                            }

                            rounddebitBirrAmount = Math.round(debitBirrAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                        }
                        System.out.println(rounddebitBirrAmount + "finc debitBirrAmount");
                        System.out.println("mulselect.get(i).getFcyAmount()" + mulselect.get(i).getFcyAmount());
                        double creditBirrAmount = (mulselect.get(i).getFcyAmount() * mulselect.get(i).getFcyRate());

                        double roundcreditBirrAmount = Math.round(creditBirrAmount * Math.pow(10, 2))
                                / Math.pow(10, 2);
                        System.out.println(roundcreditBirrAmount + "finc creditBirrAmount");
                        double difference = rounddebitBirrAmount - roundcreditBirrAmount;

                        double rounddifference = Math.round(difference * Math.pow(10, 2)) / Math.pow(10, 2);

                        System.out.println(rounddifference + "difference");
                        if (rounddifference == 0) {
                            System.out.println("Batch Balanced");
                        }

                        else {
                            MultiJrnl bbmj = new MultiJrnl();
                            bbmj.setSERIAL_NO(new BigDecimal(4));

                            if (rounddifference < 0) {

                                bbmj.setDR_CR("D");

                                bbmj.setACCOUNT("350330008");

                            } else if (rounddifference > 0) {

                                bbmj.setDR_CR("C");

                                bbmj.setACCOUNT("435120005");

                            }

                            AccountInfo accountDto3 = accountRestClient.getAccountInfo("350430005");
                            bbmj.setBRANCH_CODE("237");
                            bbmj.setACCORGL("G");
                            // bbmj.setACCOUNT("350430005");
                            bbmj.setCCY("ETB");
                            bbmj.setAMOUNT(BigDecimal.valueOf(Math.abs(rounddifference)));
                            System.out.println(bbmj.getAMOUNT());

                            bbmj.setTXN_CODE(txCode);

                            bbmj.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer());
                            // bbmj.setCUSTOMER(accountDto3.getCUST_NO());
                            bbmj.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                            mj.add(bbmj);
                            System.out.println("   test 1235");

                        }

                        System.out.println(transaction.getDetbsJrnlTxnDetail() + "   test DetbsJrnlTxnDetail");

                        BatchMaster bm1 = new BatchMaster();
                        bm1.setBATCH_NUMBER("752m");
                        bm1.setDESCRIPTION("752m");
                        Double testamount = Double.parseDouble(df.format(mulselect.get(i).getMessage().getFcyAmount()
                                * mulselect.get(i).getMessage().getRate()));

                        bm1.setDEBIT(BigDecimal.valueOf(testamount));

                        System.out.println("debit " + BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount())
                                .multiply(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate())));
                        bm1.setCREDIT(BigDecimal.valueOf(testamount));
                        bm1.setBALANCING("Y");

                        transaction.setDevwsBatchMaster(bm1);

                        Response response = paymentClientrt.autorizeTransaction(transaction);
                        System.out.println(response.getREFERENCE_NO() + "   reference");

                        if (response.getMSGSTAT().equalsIgnoreCase("SUCCESS")) {
                            System.out.println("reference " + response.getREFERENCE_NO());
                            System.out.println("batch number  " + response.getBATCH_NO());
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Transaction Authorized",
                                            "Successfully Saved and Authorized..." + response.getREFERENCE_NO()));
                            mulselect.get(i).setStatus("Liquidated");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setUpdatedDate(new Date());
                            mulselect.get(i).setTransactionNumber(response.getREFERENCE_NO());
                            mulselect.get(i).setDeletedBy("");
                            mulselect.get(i).setMessageid(response.getMSGID());
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                            "Failed to Save", "Failed to Save the transaction..."));
                            mulselect.get(i).setStatus("Cancelled");
                            mulselect.get(i).setUpdatedBy(username);
                            mulselect.get(i).setDeletedBy(response.getERROR());
                        }

                        swifttransactionservice.update(mulselect.get(i));

                        System.out.println(response + "   test 123");

                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Failed to Save", "No Account Number regestered under this correspondent bank"));
            }

        }
        list();

        return "forward:/Transaction/retentionTransaction.xhtml";
    }

    public static XMLGregorianCalendar stringToXMLGregorianCalendar(String dateString)
            throws ParseException, DatatypeConfigurationException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString); // Parse string to Date object
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc); // Convert GregorianCalendar to
                                                                          // XMLGregorianCalendar
    }

    public static XMLGregorianCalendar parseAsXmlDateWithoutTimezone(
            String dateStr, String format) throws ParseException {
        if (dateStr == null || dateStr.length() < 1) {
            throw new IllegalArgumentException(
                    "Argument 'String dateStr' is null or empty string!");
        }

        if (format == null || format.length() < 1) {
            throw new IllegalArgumentException(
                    "Argument 'String format' is null or empty string!");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setLenient(false);
        Date date = simpleDateFormat.parse(dateStr);
        return convert2XmlDateWithoutTimezone(date);
    }

    public static XMLGregorianCalendar convert2XmlDateWithoutTimezone(
            Date date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Argument 'Date date' is null!");
        }

        GregorianCalendar utilGregorianCalendar = new GregorianCalendar();
        utilGregorianCalendar.setTimeInMillis(date.getTime());
        try {
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendarDate(
                            utilGregorianCalendar.get(Calendar.YEAR),
                            utilGregorianCalendar.get(Calendar.MONTH) + 1,
                            utilGregorianCalendar
                                    .get(Calendar.DAY_OF_MONTH),
                            DatatypeConstants.FIELD_UNDEFINED);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}
