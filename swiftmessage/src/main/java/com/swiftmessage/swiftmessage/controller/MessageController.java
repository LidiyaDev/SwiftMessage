package com.swiftmessage.swiftmessage.controller;

import java.math.BigDecimal;
import java.net.http.HttpClient.Redirect;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.SourceType;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.reactive.function.client.WebClient;

import com.prowidesoftware.swift.model.field.Field50F;
import com.prowidesoftware.swift.model.field.Field50K;
import com.prowidesoftware.swift.model.field.Field53A;
import com.prowidesoftware.swift.model.field.Field54A;
import com.prowidesoftware.swift.model.field.Field57A;
import com.prowidesoftware.swift.model.field.Field59;
import com.prowidesoftware.swift.model.field.Field59F;
import com.prowidesoftware.swift.model.field.Field70;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;
import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.Rates;
import com.swiftmessage.swiftmessage.dto.SwiftMesage;
import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.entity.TblTmp;
import com.swiftmessage.swiftmessage.repository.SwiftTransactionRepository;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.entity.TblCorrespondentBank;

import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.MessageIncomingService;
import com.swiftmessage.swiftmessage.service.MessageRestClient;
import com.swiftmessage.swiftmessage.service.RateRestClient;
import com.swiftmessage.swiftmessage.service.SwiftMessageService;
import com.swiftmessage.swiftmessage.service.TblCorrespondentBankService;
import com.swiftmessage.swiftmessage.service.TblTmpService;

@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
// @ViewScoped
public class MessageController {

    // private static final String baseUrl1 = "http://192.168.12.47:9999/getByRefference/";
    // private WebClient webclient1 = WebClient.create(baseUrl1);

    // MessageRestClient messageRestClient = new MessageRestClient(webclient1);

     private static final String baseUrl12 = "http://192.168.12.47:6070/getByDateAndCy/";
     private WebClient webclient12 = WebClient.create(baseUrl12);
     RateRestClient rateRestClient = new RateRestClient(webclient12);

    @Autowired
    MessageRestClient messageRestClient;

    @Autowired
    SwiftTransactionRepository swiftTransactionRepository;

    // @Autowired
    // RateRestClient rateRestClient;

    // @Autowired
    // AccountRestClient accountRestClient;

    @Autowired
    MessageIncomingService incomingService;

    private List<TblIncomingRecord> mesitem;

    private TblIncomingRecord selected;
    private List<TblIncomingRecord> items;
    private TblIncomingRecord tempitems;
    private TblIncomingRecord nextSaveItem = new TblIncomingRecord();

    @Autowired
    TblCorrespondentBankService correspondentService;

    private String refferencenumber;
    private Date registrationDate;
    private String REFERENCE_NO;
    private String BRANCH;
    private String SENDER;
    private String VALUE_DATE;
    private String AMOUNT;
    private String MESSAGE;
    private String CCY;
    private String accountNumber;
    private String corspondantBank;
    private String orderingCustomer;
    private String bankName;
    private boolean bankren = false;
    private boolean otherbranchrend = false;
    private String paymentPurpose;
    private String beneficiaryName;
    private String countryOrigin;
    private String othercountryOrigin;
    private String otherBank;
    private double valueRate;
    private double exchangedMoney;
    private int otherbanktrue;
    private String ourReference = "";

    private boolean nextrnd = false;

    private boolean viewrnd = false;
    private double finalAmount;
    private String noMessage;
    private boolean noMessagernd = false;



    
    public boolean isNextrnd() {
        return nextrnd;
    }

    public void setNextrnd(boolean nextrnd) {
        this.nextrnd = nextrnd;
    }

    public boolean isNoMessagernd() {
        return noMessagernd;
    }

    public void setNoMessagernd(boolean noMessagernd) {
        this.noMessagernd = noMessagernd;
    }

    public String getNoMessage() {
        return noMessage;
    }

    public void setNoMessage(String noMessage) {
        this.noMessage = noMessage;
    }

    public List<TblIncomingRecord> getMesitem() {
        return mesitem;
    }

    public String getOurReference() {
        return ourReference;
    }

    public void setOurReference(String ourReference) {
        this.ourReference = ourReference;
    }

    public void setMesitem(List<TblIncomingRecord> mesitem) {
        this.mesitem = mesitem;
    }

    public String getOthercountryOrigin() {
        return othercountryOrigin;
    }

    public void setOthercountryOrigin(String othercountryOrigin) {
        this.othercountryOrigin = othercountryOrigin;
    }

    public int getOtherbanktrue() {
        return otherbanktrue;
    }

    public void setOtherbanktrue(int otherbanktrue) {
        this.otherbanktrue = otherbanktrue;
    }

    public TblIncomingRecord getTempitems() {
        return tempitems;
    }

    public void setTempitems(TblIncomingRecord tempitems) throws ParseException {
        this.tempitems = tempitems;

    }

    public double getFinalAmount() {
        double d = Double.parseDouble(AMOUNT);
        if (CCY.equalsIgnoreCase(ACCY)) {

            finalAmount = d;
        } else if (!CCY.equalsIgnoreCase(ACCY)) {
            if (ACCY.equalsIgnoreCase("ETB")) {
                finalAmount = valueRate * d;
            }

            else {

            }
        }
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public TblIncomingRecord getSelected() {
        return selected;
    }

    public double getExchangedMoney() {

        double d = Double.parseDouble(AMOUNT);

        if (valueRate == 0.0) {
            exchangedMoney = d;
        } else {
            exchangedMoney = valueRate * d;
        }

        return exchangedMoney;
    }

    public void setExchangedMoney(double exchangedMoney) {
        this.exchangedMoney = exchangedMoney;
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

    public boolean isBankren() {
        return bankren;
    }

    public double getValueRate() {
        return valueRate;
    }

    public void setValueRate(double valueRate) {
        this.valueRate = valueRate;
    }

    public String getOtherBank() {
        return otherBank;
    }

    public void setOtherBank(String otherBank) {
        this.otherBank = otherBank;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public String getCountryOrigin() {
        return countryOrigin;
    }

    public void setCountryOrigin(String countryOrigin) {
        this.countryOrigin = countryOrigin;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }

    public Date getRegistrationDate() {
        registrationDate = new Date();
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setBankren(boolean bankren) {
        this.bankren = bankren;
    }

    public boolean isOtherbranchrend() {
        return otherbranchrend;
    }

    public void setOtherbranchrend(boolean otherbranchrend) {
        this.otherbranchrend = otherbranchrend;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isViewrnd() {
        return viewrnd;
    }

    public void setViewrnd(boolean viewrnd) {
        this.viewrnd = viewrnd;
    }

    public String getRefferencenumber() {
        return refferencenumber;
    }

    public void setRefferencenumber(String refferencenumber) {
        this.refferencenumber = refferencenumber;
    }

    public String getREFERENCE_NO() {
        return REFERENCE_NO;
    }

    public void setREFERENCE_NO(String rEFERENCE_NO) {
        REFERENCE_NO = rEFERENCE_NO;
    }

    public String getBRANCH() {
        return BRANCH;
    }

    public void setBRANCH(String bRANCH) {
        BRANCH = bRANCH;
    }

    public String getSENDER() {
        return SENDER;
    }

    public void setSENDER(String sENDER) {
        SENDER = sENDER;
    }

    public String getVALUE_DATE() {
        return VALUE_DATE;
    }

    public void setVALUE_DATE(String vALUE_DATE) {
        VALUE_DATE = vALUE_DATE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String aMOUNT) {
        AMOUNT = aMOUNT;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String mESSAGE) {
        MESSAGE = mESSAGE;
    }

    public String getCCY() {
        return CCY;
    }

    public void setCCY(String cCY) {
        CCY = cCY;
    }

    public void retriveMeesage33() {
        System.out.println("refferencenumber  :" + refferencenumber);
    }

    public void prepareSelect(TblIncomingRecord item) {
        if (item != null) {

            System.out.println("test item" + item);
            selected = item;
        }

    }

    @RequestMapping("/Message/View")
    public String list() {
        mesitem = incomingService.findAll();
        System.out.println(mesitem + "Items");
        return "forward:/Message/index.xhtml";
    }

    @RequestMapping("/Message/index")
    public String listView() throws ParseException {
        mesitem = incomingService.findAll();
        System.out.println(mesitem + "Items");
        return "forward:/Message/index.xhtml";
    }

    @RequestMapping("/Message/new")
    public String newProfile() {
        selected = new TblIncomingRecord();
        // selected.setTinno("asdfasdf");
        System.out.println("lidu123 ");

        return "forward:/Message/message.xhtml";
    }

    @RequestMapping("/Message/new1")
    public String newProfile1() {
        selected = new TblIncomingRecord();
        // selected.setTinno("asdfasdf");
        System.out.println("lidu123 ");
        cancelEntry();
        return "forward:/Message/message.xhtml";
    }

    @RequestMapping("/Message/account")
    public String newAccount() {
        selected = new TblIncomingRecord();
        // selected.setTinno("asdfasdf");
        System.out.println("lidu12345 ");

        return "forward:/Message/account.xhtml";
    }

    
    public String edit(@RequestParam String id) throws ParseException {

        System.out.println(id);

        

        // System.out.println(tempitems.getAccountNumber() + "tempItem");
        if (ACCOUNT_CLASS.contains("RET")) {
            System.out.println("RETAs");

            return "forward:/Message/retention.xhtml";
        }

        else {

            System.out.println(ACCOUNT_CLASS);

            
            return "forward:/Message/account.xhtml";
        }

    }
    
TblTmp tmpdata;
TblTmp tmpSavedData;



public TblTmp getTmpSavedData() {
    return tmpSavedData;
}

public void setTmpSavedData(TblTmp tmpSavedData) {
    this.tmpSavedData = tmpSavedData;
}

public TblTmp getTmpdata() {
    return tmpdata;
}
@Autowired
TblTmpService tblTmpService;


    public void updatelast() throws ParseException
    {
        
tempitems = new TblIncomingRecord();
tmpdata = new TblTmp();
tmpdata.setAccountNumber(accountNumber);
System.out.println(ourReference + "  reference");
tmpdata.setBankReference("");
// tempitems.setIsDashenBranch("0");
tmpdata.setOrderingCustomer(orderingCustomer);
tmpdata.setOtherBankBranch(otherBank);
tmpdata.setRate(valueRate);
tmpdata.setRecordState(null);
tmpdata.setReference(REFERENCE_NO);
tmpdata.setRegistrationDate(registrationDate);
tmpdata.setRegDate(new Date());
tmpdata.setSenderBank(SENDER);
tmpdata.setAccountNumberCurrency(ACCY);
String result = VALUE_DATE.split(" ")[0];
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
Date date1 = formatter.parse(result);
tmpdata.setValueDate(date1);
tmpdata.setAccountType(ACCOUNT_CLASS);
tmpdata.setBeneficiary(beneficiaryName);
tmpdata.setDashenBranch(BRANCH_CODE);
tmpdata.setCorrespondentBank(corspondantBank);
tmpdata.setFcyAmount(Double.parseDouble(AMOUNT));
tmpdata.setCurrencyId(CCY);
tmpdata.setCreatedBy("testUser");
tmpdata.setPaymentPurpose(paymentPurpose);

// tempitems.setIsDashenBranch((Integer.toString(otherbanktrue)));
tmpdata.setCountryOfOrigin(countryOrigin);

//TblTmp tmpSavedData = new TblTmp();
tmpSavedData =  tblTmpService.save(tmpdata);
BigDecimal savedId=new BigDecimal("0");
//savedId = new BigDecimal(null);
savedId = nextSaveItem.getId();
nextrnd=true;
System.out.println(tmpSavedData.getReference() + "-------------------correspondet");
System.out.println(nextrnd+"---------------true");
//saveButtonRnd = true;


        // System.out.println(tempitems.getAccountNumber() + "tempItem");
        
    }

    static String removeSubstring(String text, int startIndex, int endIndex) {
        if (endIndex < startIndex) {
            startIndex = endIndex;
        }

        String a = text.substring(0, startIndex);
        String b = text.substring(endIndex);

        return a + b;
    }

    public void retriveMeesage() throws ParseException {
        System.out.println("refferencenumber  :" + refferencenumber);

        List<SwiftTransaction> testDate = swiftTransactionRepository.findByMessageReference(refferencenumber);
        System.out.println("========data======"+testDate.toString());
        if (testDate.size() > 0) {
            noMessagernd = true;
            noMessage = "Already registered Swift Message ";
        } else {
            SwiftMesage messageDto = messageRestClient.getMessageInfo(refferencenumber);
            if (messageDto != null) {
                noMessagernd = false;
                REFERENCE_NO = messageDto.getREFERENCE_NO();
                BRANCH = messageDto.getBRANCH();

                VALUE_DATE = messageDto.getVALUE_DATE();
                CCY = messageDto.getCCY();
                AMOUNT = messageDto.getAMOUNT();
                MESSAGE = messageDto.getMESSAGE();

                String subMessage = removeSubstring(MESSAGE, 0, 56);

                MT103 m = MT103.parse(subMessage);
            

                String subcoress1 = m.getSender().substring(0,4);
                List<TblCorrespondentBank> bankTable = correspondentService.findByBankLike("%" + subcoress1 + "%",CCY);
                if (bankTable.size() > 0) {
                    SENDER = bankTable.get(0).getName();
                } else {
                    SENDER = m.getSender();
                }

                paymentPurpose = purpose(subMessage);
                Field70 purpose = m.getField70();
                if (purpose != null) {
                    System.out.println(purpose.getComponent1() + "purpose payment");
                    paymentPurpose = purpose.getComponent1();
                }

                Field54A coree = m.getField54A();
                if (coree != null) {
                    corspondantBank = coree.getComponent3();
                    System.out.println(coree);
                } 
                else if (coree == null) {
                    Field53A corre1 = m.getField53A();
                    if (corre1 != null) {
                        corspondantBank = corre1.getComponent3();
                    }

                    else if(m.getSender() != null) {
                        corspondantBank = m.getSender();
                    }
                    else
                    {
                        corspondantBank = new String();
                    }

                }
                
                
                if (corspondantBank != null) {

                    
                    String subcoress = corspondantBank.substring(0,4);

                    System.out.println("subcoress========="+subcoress);
                    List<TblCorrespondentBank> bankTable1 = correspondentService
                            .findByBankLike("%" + subcoress+"%",CCY);
                            System.out.println(bankTable1+"  bank table");
                    if (bankTable1.size() > 0) {
                        for (TblCorrespondentBank bk : bankTable1) {
                            if(bk.getCurrency().equalsIgnoreCase(CCY))
                            {
                                corspondantBank = bk.getName();
                            }
                        }
                        
                    } else
                        corspondantBank = corspondantBank;

                }

                countryOrigin = countryOriginRetrive(subMessage);
                orderingCustomer = getOrderingCustomer(subMessage);
                Field50K ordering = m.getField50K();

                if (ordering != null) {
                    orderingCustomer = ordering.getComponent2();
                    countryOrigin = ordering.getComponent3();
                    System.out.println(countryOrigin + " test country origin");
                } else {
                    Field50F orderingF = m.getField50F();
                    if (orderingF != null) {
                        orderingCustomer = orderingF.getComponent3();
                        countryOrigin = orderingF.getComponent5();
                    }
                    else{
                        orderingCustomer = "";
                    countryOrigin = "No country";
                    }
                    
                }

                beneficiaryName = beneficName(subMessage);
                Field59 account = m.getField59();
                if (account != null) {

                    if(account.getComponent1().length()>=13)
                    {
                        accountNumber = account.getComponent1().substring(0, 13);
                        retriveAccount();
                    }
                    else
                        {
                            accountNumber = account.getComponent1();
                        }
                    

                    beneficiaryName = account.getComponent2();
                } else {
                    Field59F accountF = m.getField59F();
                    if (accountF != null) {
                        if(accountF.getComponent1().length() >=13)
                        {
                            accountNumber = accountF.getComponent1().substring(0, 13);
                            retriveAccount();
                        }
                        else
                        {
                            accountNumber = accountF.getComponent1();
                        }
                        beneficiaryName = accountF.getComponent3();
                    }

                }

                Field57A otheraccount = m.getField57A();

                if(otheraccount != null)
                {
                    System.out.println("otheraccount======"+otheraccount);
                    String otherbb = otheraccount.getComponent3();
                    System.out.println("otherbb======"+otherbb);

                    String bankCut = otherbb.substring(0, 4);
                    // System.out.println(bankCut + "bankCut");
                    List<TblCorrespondentBank> bankTable1 = correspondentService.findByBankLike("%" + bankCut + "%",CCY);
                    /// System.out.println(bankTable1 + "bankTable1");
                    // System.out.println(bankTable1.get(0).getName() + "bankname");
                    if (bankTable1.size() > 0) {
                        otherBank = bankTable1.get(0).getName();
                        System.out.println(bankTable1.get(0).getName() + "bankname");
                    } else
                        otherBank = otherBankRetrive(subMessage);

                }

                
                if(otherBank != null && !otherBank.contains("DASHETAA"))
                {
                    valueRate = 0.0;
                }
                

                if(otherBank == null || otherBank.contains("DASHETAA"))
                {
                    try {
                        
                    valueRate = valueBuyRate();
                } catch (ParseException e) {

                    e.printStackTrace();
                }
                }

                try {
                        
                    valueRate = valueBuyRate();
                } catch (ParseException e) {

                    e.printStackTrace();
                }

SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = formatter.parse(messageDto.getVALUE_DATE());

        Date currntDate = new Date();

                if(date1.compareTo(currntDate)>0)

                {

                   System.out.println("Future Valu DAte");

                   noMessagernd = true;

                noMessage = "Value Date is Greater than Current Date! ";

                }
                


                

                
            } else {
                System.out.println("test");
                noMessagernd = true;
                noMessage = "No Swift Message With this Reference Number! ";
            }
        }

        // System.out.println( " Account Info List");

    }

    public String countryOriginRetrive(String message) {
        try {
            String separator = ":50K:";
            int sepPos = message.indexOf(separator);
            if (sepPos == -1) {

                System.out.println("");
                return null;
            }

            String separatedText = message.substring(sepPos + separator.length());
            // System.out.println(separatedText + "Country");
            String separater2 = "\n";

            int sepPos1 = separatedText.indexOf(separater2);
            String lastseparated = separatedText.substring(sepPos1 + separater2.length());

            Object[] lines1 = lastseparated.lines().toArray();
            // System.out.println(lines1[2].toString() + "Country");
            return lines1[3].toString() + ", " + lines1[2].toString();
        } catch (Exception ex) {
            // System.out.println("error " + ex);
            return "";
        }

    }

    public Double valueBuyRate() throws ParseException {

        Double value = 0.0;
        String result = VALUE_DATE.split(" ")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = formatter.parse(result);
        DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
        String dateToString = df.format(date1);

        System.out.println("ACCY++++"+ACCY);

        System.out.println("otherBank++++"+otherBank);
        if(otherBank !=null && !otherBank.equalsIgnoreCase("") && !otherBank.contains("DASH"))
        {
            System.out.println("other======"+CCY);
            value = 0.0;
        }
        else{
            if (CCY.equalsIgnoreCase("ETB") ) {

                System.out.println("Currrent ETB======"+CCY);
                List<Rates> rate = rateRestClient.retriveRatesByDateAndCy(dateToString, ACCY);
                for (int i = 0; i < rate.size(); i++) {
                    if (rate.get(i).getRATE_TYPE().equals("BANK_BUY")) {
    
                        value = rate.get(i).getMID_RATE();
                    }
    
                }
            } else {
                System.out.println("Currrent ETB======"+CCY);
                List<Rates> rate = rateRestClient.retriveRatesByDateAndCy(dateToString, CCY);
                for (int i = 0; i < rate.size(); i++) {
                    if (rate.get(i).getRATE_TYPE().equals("BANK_BUY")) {
                        value = rate.get(i).getMID_RATE();
                    }
    
                }
            }
        }

        

        return value;

    }

    public String otherBankRetrive(String message) {
        try {
            String separator = ":57A:";
            int sepPos = message.indexOf(separator);
            if (sepPos == -1) {
                System.out.println(sepPos + "Position");
                otherbanktrue = 0;
                otherbranchrend = false;
                return null;
            }
            otherbanktrue = 1;

            String separatedText = message.substring(sepPos + separator.length());

            String separater2 = "\n";
            // System.out.println(separatedText + " Other Bank nmnmnmn");
            int sepPos1 = separatedText.indexOf(separater2);
            String lastseparated = separatedText.substring(sepPos1 + separater2.length());
            // System.out.println(lastseparated + " Other Bank15566666s");
            Object[] lines1 = separatedText.lines().toArray();
            // System.out.println(lines1[0].toString() + " Other Bank1");
            otherbranchrend = true;
            return lines1[0].toString();
        } catch (Exception ex) {
            System.out.println("error  " + ex);
            return "";
        }

    }

    public String purpose(String message) {
        try {
            String separator = ":70:";
            int sepPos = message.indexOf(separator);
            if (sepPos == -1) {
                System.out.println("");
                return "";
            }

            String separatedText = message.substring(sepPos + separator.length());

            String separater2 = "\n";

            int sepPos1 = separatedText.indexOf(separater2);
            String lastseparated = separatedText.substring(sepPos1 + separater2.length());

            Object[] lines1 = separatedText.lines().toArray();

            return lines1[0].toString();
        } catch (Exception ex) {
            // System.out.println("error " + ex);
            return "";
        }

    }

    public String getOrderingCustomer(String message) {

        try {
            String separator = ":50K:/";
            int sepPos = message.indexOf(separator);
            if (sepPos == -1) {
                // System.out.println("No Ordering Customer");

                String separator1 = ":50F:/";
                int sepPos1 = message.indexOf(separator1);
                if (sepPos1 == -1) {
                    System.out.println("No Ordering Customer");
                    return "";
                }

                String separatedText1 = message.substring(sepPos + separator1.length());

                String separater3 = "\n";

                int sepPos3 = separatedText1.indexOf(separater3);
                String lastseparated1 = separatedText1.substring(sepPos3 + separater3.length());

                Object[] lines2 = lastseparated1.lines().toArray();
                String OrderingCustomer4 = lines2[0].toString() + " " + lines2[1].toString() + " "
                        + lines2[3].toString() + " " + lines2[4].toString();

                return OrderingCustomer4;

            }

            String separatedText = message.substring(sepPos + separator.length());

            String separater2 = "\n";

            int sepPos1 = separatedText.indexOf(separater2);
            String lastseparated = separatedText.substring(sepPos1 + separater2.length());

            Object[] lines1 = lastseparated.lines().toArray();
            String OrderingCustomer = lines1[0].toString();

            return OrderingCustomer;
        } catch (Exception ex) {
            return "";
        }
    }

    public String accounts(String message) {
        try {
            String separator = ":59:/";
            int sepPos = message.indexOf(separator);
            if (sepPos == -1) {
                System.out.println("");
            }

            String separatedText = message.substring(sepPos + separator.length());

            accountNumber = separatedText.substring(0, 13);

            retriveAccount();

            return accountNumber;
        } catch (Exception ex) {
            // System.out.println("error " +ex);
            return "";
        }
    }

    public String beneficName(String message) {
        try {
            String separator = ":59:/";
            int sepPos = message.indexOf(separator);
            if (sepPos == -1) {
                System.out.println("");
                return null;
            }

            String separatedText = message.substring(sepPos + separator.length());

            String separater2 = "\n";

            int sepPos1 = separatedText.indexOf(separater2);
            String lastseparated = separatedText.substring(sepPos1 + separater2.length());

            Object[] lines1 = lastseparated.lines().toArray();
            lines1[0].toString();

            return lines1[0].toString();
        } catch (Exception ex) {
            // System.out.println("error " +ex);
            return "";
        }
    }

   
 private static final String baseUrl2 = "http://localhost:6070/getByAccountNo/";
    private WebClient webclient2 = WebClient.create(baseUrl2);
    AccountRestClient accountRestClient = new AccountRestClient(webclient2);
    private String AC_DESC;
    private String CUST_AC_NO;
    private String BRANCH_CODE;
    private String ACCOUNT_CLASS;
    private String ACCY;
    private String CUST_NO;

    // private boolean viewrnd = false;

    public String getAC_DESC() {
        return AC_DESC;
    }

    public String getACCY() {
        return ACCY;
    }

    public void setACCY(String aCCY) {
        ACCY = aCCY;
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

    public String getCUST_NO() {
        return CUST_NO;
    }

    public void setCUST_NO(String cUST_NO) {
        CUST_NO = cUST_NO;
    }

    public void retriveAccount() {

        // System.out.println(accountNumber + "Account 123");
        AccountInfo accountDto = accountRestClient.getAccountInfo(accountNumber);

        System.out.println("accountNumber========="+accountNumber);
       // System.out.println("=====account Detai++++++++"+accountDto.getACCOUNT_CLASS());
        viewrnd = true;

        if (accountDto != null) {
            // System.out.println(accountDto.getAC_DESC() + " Account Info List");

            AC_DESC = accountDto.getAC_DESC();
            CUST_AC_NO = accountDto.getCUST_AC_NO();
            BRANCH_CODE = accountDto.getBRANCH_CODE();
            ACCOUNT_CLASS = accountDto.getACCOUNT_CLASS();
            ACCY = accountDto.getCCY();
            CUST_NO = accountDto.getCUST_NO();
            bankName = "Dashen Bank";
            bankren = true;
            otherbranchrend = true;
        } else {
            bankren = false;
            otherbranchrend = false;
            bankName = otherBank;
            AC_DESC = "";
            CUST_AC_NO = "";
            BRANCH_CODE = "";
            ACCOUNT_CLASS = "";
            ACCY = "";
            CUST_NO = "";
        }

    }

    public String getCorrespondentBank(String message) {
        // System.out.println("message " + message);
        try {

            if (message.contains(":53A:")) {
                String separator = ":53A:";
                int sepPos = message.indexOf(separator);
                if (sepPos == -1) {
                    System.out.println("");
                }

                String separatedText = message.substring(sepPos + separator.length());

                String separater2 = "\n";

                int sepPos1 = separatedText.indexOf(separater2);
                String lastseparated = separatedText.substring(sepPos1 + separater2.length());

                Object[] lines1 = separatedText.lines().toArray();
                String Correspondent = lines1[0].toString();

                return Correspondent;
            } else {
                return null;

            }

        } catch (Exception ex) {
            System.out.println("error  " + ex);
            return "";
        }

    }

  
    public void cancelEntry() {
        System.out.println("cancel");
        AC_DESC = null;
        CUST_AC_NO = null;
        BRANCH_CODE = null;
        ACCOUNT_CLASS = null;
        CCY = null;
        CUST_NO = null;
        REFERENCE_NO = null;
        BRANCH = null;
        SENDER = null;
        VALUE_DATE = null;
        CCY = null;
        AMOUNT = null;
        MESSAGE = null;
        refferencenumber = null;
        accountNumber = null;
        accountNumber = null;
        corspondantBank = null;
        orderingCustomer = null;
        bankName = null;
        noMessage = null;
        noMessagernd = false;
        paymentPurpose = null;
        beneficiaryName = null;
        countryOrigin = null;
        otherBank = new String();;
        valueRate = 0;

    }

    public String getCorspondantBank() {
        return corspondantBank;
    }

    public void setCorspondantBank(String corspondantBank) {
        this.corspondantBank = corspondantBank;
    }

    public String getOrderingCustomer() {
        return orderingCustomer;
    }

    public void setOrderingCustomer(String orderingCustomer) {
        this.orderingCustomer = orderingCustomer;
    }

    

}