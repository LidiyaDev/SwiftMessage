package com.swiftmessage.swiftmessage.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.Rates;
import com.swiftmessage.swiftmessage.entity.ExRateLocal;
import com.swiftmessage.swiftmessage.entity.FetchedData;
import com.swiftmessage.swiftmessage.entity.TblCorrespondentBank;
import com.swiftmessage.swiftmessage.entity.TblIncomingRecord;
import com.swiftmessage.swiftmessage.entity.TblTmp;
import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.ExRateLocalService;
import com.swiftmessage.swiftmessage.service.FetchedDataService;
import com.swiftmessage.swiftmessage.service.MessageIncomingService;
import com.swiftmessage.swiftmessage.service.RateRestClient;
import com.swiftmessage.swiftmessage.service.TblCorrespondentBankService;
import com.swiftmessage.swiftmessage.service.TblTmpService;


import lombok.Data;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.RequestMethod;


@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Controller


public class FetchedDataController {

    @Autowired
    FetchedDataService fetchedDataService;

    @Autowired

    MessageIncomingService incomingService;

    @Autowired

    TblCorrespondentBankService correspondentService;
    private static final String baseUrl2 = "http://localhost:6070/getByAccountNo/";

    private WebClient webclient2 = WebClient.create(baseUrl2);

    AccountRestClient accountRestClient = new AccountRestClient(webclient2);

    private static final String baseUrl12 = "http://192.168.12.47:6070/getByDateAndCy/";

    private WebClient webclient12 = WebClient.create(baseUrl12);

    RateRestClient rateRestClient = new RateRestClient(webclient12);
    private AccountInfo accountInfo;
    private List<Rates> rates = new ArrayList<>();

    private TblTmp tmpdata;
    private TblTmp tmpSavedData;

    @Autowired
    TblTmpService tblTmpService;

    @Autowired
    ExRateLocalService exRateLocalService;

    private List<TblIncomingRecord> mesitem;

    FetchedData fetchedData = null;

    private String inreference;
    private Double rateValue;
    private String selectedCountry;
    private boolean noMessagernd = false;
    private boolean bankren = false;
    private String correBank;
    private String senderBank;
    private String valueDateString;

    private boolean otherbranchrend = false;
    private String noMessage;

    private boolean savernd = false;
    private String saveMessage = new String();

    @PostConstruct
    public void init() {

        accountInfo = new AccountInfo();
        rateValue = 1.0;
        selectedCountry = new String();
        fetchedData = new FetchedData();
        noMessage = new String();
        noMessagernd = false;
        correBank = new String();
        senderBank = new String();
        saveMessage = new String();
        savernd = false;

    }

    @RequestMapping("/newSwiftMessage")

    public String newProfile1() {

        tmpSavedData = new TblTmp();

        // selected.setTinno("asdfasdf");

        System.out.println("lidu123");

        clearSearch();

        return "forward:/Message/NewMessage.xhtml";

    }

    @RequestMapping("/Message/SwiftMessage")
    public String index() {

        return "/Message/NewMessage.xhtml";
    }

    @RequestMapping("/Message/index1")
    public String listView() throws ParseException {
        saveandExit();
        mesitem = incomingService.findAll();
        System.out.println(mesitem + "Items");
        return "forward:/Message/index.xhtml";
    }

    public void clearSearch() {
        accountInfo = new AccountInfo();
        rateValue = 1.0;
        selectedCountry = new String();
        fetchedData = new FetchedData();
        noMessage = new String();
        correBank = new String();
        senderBank = new String();
        noMessagernd = false;
        otherbranchrend = false;
        inreference = new String();
        saveMessage = new String();
        savernd = false;

    }

    public void saveandExit() {
        tmpdata = new TblTmp();
        tmpdata.setAccountNumber(fetchedData.getAccountNumber());
        System.out.println(inreference + "  reference");
        tmpdata.setBankReference("");
        // tempitems.setIsDashenBranch("0");
        tmpdata.setOrderingCustomer(fetchedData.getOrderingCustomer());
        tmpdata.setOtherBankBranch(fetchedData.getOtherBankBranch());
        tmpdata.setRate(rateValue);
        tmpdata.setRecordState(null);
        tmpdata.setReference(inreference);
        tmpdata.setRegistrationDate(new Date());
        tmpdata.setRegDate(new Date());
        tmpdata.setSenderBank(fetchedData.getSenderBank());
        // tmpdata.setAccountNumberCurrency(accountInfo.getCCY());

        tmpdata.setValueDate(fetchedData.getValueDate());
        // tmpdata.setAccountType(accountInfo.getACCOUNT_CLASS());
        tmpdata.setBeneficiary(fetchedData.getBeneficiary());
        // tmpdata.setDashenBranch(accountInfo.getBRANCH_CODE());
        tmpdata.setCorrespondentBank(fetchedData.getCorrespondentBank());

        tmpdata.setFcyAmount(fetchedData.getFcyAmount());
        tmpdata.setCurrencyId(fetchedData.getCurrencyId());
        tmpdata.setCreatedBy("testUser");
        tmpdata.setPaymentPurpose(fetchedData.getPaymentPurpose());
        tmpdata.setTotalServiceCharge(fetchedData.getTotalServiceCharge());

        // tempitems.setIsDashenBranch((Integer.toString(otherbanktrue)));
        tmpdata.setCountryOfOrigin(selectedCountry);

        // TblTmp tmpSavedData = new TblTmp();
        tmpSavedData = tblTmpService.save(tmpdata);

        TblIncomingRecord realData = new TblIncomingRecord();

        realData.setAccountNumber(tmpdata.getAccountNumber());

        // tempitems.setIsDashenBranch("0");
        realData.setOrderingCustomer(tmpdata.getOrderingCustomer());
        realData.setOtherBankBranch(tmpdata.getOtherBankBranch());
        realData.setRate(tmpdata.getRate());
        realData.setRecordState(null);
        realData.setReference(tmpdata.getReference());
        realData.setRegistrationDate(tmpdata.getRegistrationDate());
        realData.setRegDate(new Date());
        realData.setSenderBank(tmpdata.getSenderBank());
        realData.setAccountNumberCurrency(tmpdata.getAccountNumberCurrency());
        realData.setValueDate(tmpdata.getValueDate());
        realData.setAccountType(tmpdata.getAccountType());
        realData.setBeneficiary(tmpdata.getBeneficiary());
        realData.setDashenBranch(tmpdata.getDashenBranch());
        realData.setCorrespondentBank(tmpdata.getCorrespondentBank());
        realData.setFcyAmount(tmpdata.getFcyAmount());
        realData.setCurrencyId(tmpdata.getCurrencyId());
        realData.setCreatedBy("Other");
        realData.setPaymentPurpose(tmpdata.getPaymentPurpose());
        realData.setTotalServiceCharge(tmpdata.getTotalServiceCharge());

        // tempitems.setIsDashenBranch((Integer.toString(otherbanktrue)));
        realData.setCountryOfOrigin(tmpdata.getCountryOfOrigin());

        System.out.println(tmpdata.getAccountType() + "-------------from last page reference ");
        // fromLastRecord = swiftmessagerService.findByReference(temp.getReference());
        // System.out.println(fromLastRecord.get(0).toString()+"----------------last
        // Record");
        // fromLastRecord.get(0).setCreatedBy(usern);
        // fromLastRecord.get(0).setRegistrationDate(new Date());
        // temp.setRate(rateExcha);
        DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String formattedDate = df.format(Calendar.getInstance().getTime());
        List<TblIncomingRecord> lastrecord = incomingService.findLast();
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

        String banksReferenceString = String.format("%s%s/%s", "DB/Other/", String.format(FORMAT, current), "24");
        String ourReference = "DB/FINC/" + lastref + 1 + "/" + formattedDate;
        System.out.println(banksReferenceString + "  Reference");
        realData.setBankReference(banksReferenceString);
        realData.setRecordState(current);
        // temp.setCountryOfOrigin(othercountryOrigin);
        incomingService.save(realData);

    }

    public void searchMessage() {
        // inreference = new String();

        System.out.println("inreference======" + inreference);

        List<FetchedData> fetchedList = new ArrayList<>();
        fetchedList = fetchedDataService.byReference(inreference);
        if (fetchedList.size() == 0) {
            noMessagernd = true;
            noMessage = "No Swift Message With this Reference Number! ";
        } else {
            List<TblIncomingRecord> testDate = incomingService.findByReference(inreference);
            if (testDate.size() > 0) {
                noMessagernd = true;

                noMessage = "Already registered Swift Message! ";
            } else {
                fetchedData = fetchedList.get(0);

                noMessagernd = false;

                System.out.println("inreference++++++++++" + fetchedData.getAccountNumber());

                String subcoress1 = fetchedData.getCorrespondentBank().substring(0, 5);
                System.out.println("subcoress1++++++++++" + subcoress1);

                List<TblCorrespondentBank> bankTable = correspondentService.findByBankLike("%" + subcoress1 + "%",
                        fetchedData.getCurrencyId());
                System.out.println("bankTable++++++++++" + bankTable.get(0));
                if (bankTable.size() > 0) {
                    correBank = bankTable.get(0).getName();
                } else {
                    correBank = fetchedData.getCorrespondentBank();
                }

                String subsend = fetchedData.getSenderBank().substring(0, 4);
                List<TblCorrespondentBank> sendbankTable = correspondentService.findByBankLike("%" + subsend + "%",
                        fetchedData.getCurrencyId());
                if (sendbankTable.size() > 0) {
                    senderBank = sendbankTable.get(0).getName();
                } else {
                    senderBank = fetchedData.getSenderBank();
                }

                accountInfo = accountRestClient.getAccountInfo(fetchedData.getAccountNumber());
                DateFormat df = new SimpleDateFormat("dd-MMM-yy");
                valueDateString = df.format(fetchedData.getValueDate());

                String dateToString = df.format(fetchedData.getValueDate());

                if (fetchedData.getCurrencyId() == "ETB" || fetchedData.getCurrencyId().equalsIgnoreCase("ETB")) {

                    System.out.println("HERE ETB");

                    // System.out.println("accountInfo.getCCY()"+accountInfo.getCCY());
                    List<Rates> rate = new ArrayList<>();
                    List<ExRateLocal> localListRate = new ArrayList<>();
                    String currency = new String();
                    if (accountInfo != null) {
                        currency = accountInfo.getCCY();
                    } else {
                        currency = fetchedData.getCurrencyId();
                    }

                    rate = rateRestClient.retriveRatesByDateAndCy(dateToString, currency);

                    if (rate.size() == 0 || rate == null) {

                        localListRate = exRateLocalService.findbyDateCcy(currency, dateToString);

                        // if (localListRate.size() == 0 || localListRate == null) {
                        //     System.out.println("Here Entered");
                        //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
                        //     LocalDate messageDate = LocalDate.parse(dateToString, formatter);
                        //     LocalDate today = LocalDate.now();
                        //     String todate = today.format(formatter);
                        //     if (today.isEqual(messageDate.plusDays(1))) {
                        //         rate = rateRestClient.retriveRatesByDateAndCy(todate, currency);
                        //         rateValue = Double.valueOf(rate.get(0).getMID_RATE());
                        //     }

                        // } else {
                            for (int i = 0; i < localListRate.size(); i++) {

                                if (localListRate.get(i).getRATE_TYPE().equals("BANK_BUY")) {

                                    rateValue = Double.valueOf(localListRate.get(i).getMID_RATE());

                                }

                            }
                        //}

                    } else {
                        for (int i = 0; i < rate.size(); i++) {

                            if (rate.get(i).getRATE_TYPE().equals("BANK_BUY")) {

                                rateValue = rate.get(i).getMID_RATE();

                            }

                        }
                    }

                } else {
                    List<Rates> rate = new ArrayList<>();
                    List<ExRateLocal> localListRate = new ArrayList<>();

                    rate = rateRestClient.retriveRatesByDateAndCy(dateToString, fetchedData.getCurrencyId());

                    if (rate.size() == 0) {

                        localListRate = exRateLocalService.findbyDateCcy(fetchedData.getCurrencyId(), dateToString);
                        // if (localListRate == null) {
                        //     System.out.println("Here Entered");
                        //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
                        //     LocalDate messageDate = LocalDate.parse(dateToString, formatter);
                        //     LocalDate today = LocalDate.now();
                        //     String todate = today.format(formatter);
                        //     if (today.isEqual(messageDate.plusDays(1))) {
                        //         System.out.println("Here entered");
                        //         rate = rateRestClient.retriveRatesByDateAndCy(todate, fetchedData.getCurrencyId());
                        //         rateValue = Double.valueOf(rate.get(0).getMID_RATE());
                        //     }

                        // } else {

                            for (int i = 0; i < localListRate.size(); i++) {

                                if (localListRate.get(i).getRATE_TYPE().equals("BANK_BUY")) {

                                    rateValue = Double.valueOf(localListRate.get(i).getMID_RATE());

                                }

                            }
                       //}

                    } else {
                        for (int i = 0; i < rate.size(); i++) {

                            if (rate.get(i).getRATE_TYPE().equals("BANK_BUY")) {

                                rateValue = rate.get(i).getMID_RATE();

                            }

                        }
                    }

                }
                int compare = fetchedData.getValueDate().compareTo(new Date());
                if (compare == 1) {
                    noMessagernd = true;

                    noMessage = "Future Value Date! ";
                } else {
                    noMessagernd = false;
                }

                if (fetchedData.getOtherBankBranch() != null
                        && !fetchedData.getOtherBankBranch().contains("DASHETAA")) {
                    otherbranchrend = true;
                }
            }

        }

    }  
 
   @RequestMapping("/Message/edit1")
    public String edit(@RequestParam String id) throws ParseException {

       System.out.println(id);
       BigDecimal idd = new BigDecimal(id);

        incomingService.deletedata(idd);
        

        mesitem = incomingService.findAll();
       
        return "forward:/Message/index.xhtml";

    }

    public void searchAccDetail() {
        System.out.println(fetchedData.getAccountNumber());
        accountInfo = accountRestClient.getAccountInfo(fetchedData.getAccountNumber());
        System.out.println("accountInfo" + accountInfo.getAC_DESC());
    }

    public void changeRate()
    {
        System.out.println("Changed rate+++++"+rateValue);
        
    }

    public void saveTempData() {

        tmpdata = new TblTmp();
        tmpdata.setAccountNumber(fetchedData.getAccountNumber());
        System.out.println(inreference + "  reference");
        tmpdata.setBankReference("");
        // tempitems.setIsDashenBranch("0");
        tmpdata.setOrderingCustomer(fetchedData.getOrderingCustomer());
        tmpdata.setOtherBankBranch(fetchedData.getOtherBankBranch());
        tmpdata.setRate(rateValue);
        tmpdata.setRecordState(null);
        tmpdata.setReference(inreference);
        tmpdata.setRegistrationDate(new Date());
        tmpdata.setRegDate(new Date());
        tmpdata.setSenderBank(senderBank);
        tmpdata.setAccountNumberCurrency(accountInfo.getCCY());
        tmpdata.setValueDate(fetchedData.getValueDate());
        tmpdata.setAccountType(accountInfo.getACCOUNT_CLASS());
        tmpdata.setBeneficiary(fetchedData.getBeneficiary());
        tmpdata.setDashenBranch(accountInfo.getBRANCH_CODE());
        tmpdata.setCorrespondentBank(correBank);

        tmpdata.setFcyAmount(fetchedData.getFcyAmount());
        tmpdata.setCurrencyId(fetchedData.getCurrencyId());
        tmpdata.setCreatedBy("testUser");
        tmpdata.setPaymentPurpose(fetchedData.getPaymentPurpose());
        tmpdata.setTotalServiceCharge(fetchedData.getTotalServiceCharge());

        // tempitems.setIsDashenBranch((Integer.toString(otherbanktrue)));
        tmpdata.setCountryOfOrigin(selectedCountry);

        // TblTmp tmpSavedData = new TblTmp();
        tmpSavedData = tblTmpService.save(tmpdata);

        if (tmpSavedData.getId() != null || tmpSavedData != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Message Saved", "Message Saved"));
            System.out.println("-------------------yes");
            savernd = true;
            saveMessage = "Message Saved";
        }

        System.out.println(tmpSavedData.getReference() + "-------------------correspondet");
    }

    @RequestMapping("/Message/accountM")

    public String account(@RequestParam String id) throws ParseException {

        System.out.println(id);

        // System.out.println(tempitems.getAccountNumber() + "tempItem");

        if (accountInfo.getACCOUNT_CLASS().contains("RET")) {

            System.out.println("RETAs");

            return "forward:/Message/RETATransaction.xhtml";

        }

        else {

            System.out.println(accountInfo.getACCOUNT_CLASS());

            return "forward:/Message/FINCTransaction.xhtml";

        }

    }

    public FetchedData getFetchedData() {
        if (fetchedData == null) {
            fetchedData = new FetchedData();
        }
        return fetchedData;
    }

    public void setFetchedData(FetchedData fetchedData) {
        this.fetchedData = fetchedData;
    }

    public String getInreference() {
        return inreference;
    }

    public void setInreference(String inreference) {
        this.inreference = inreference;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Double getRateValue() {
        return rateValue;
    }

    public void setRateValue(Double rateValue) {
        this.rateValue = rateValue;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public List<Rates> getRates() {
        return rates;
    }

    public void setRates(List<Rates> rates) {
        this.rates = rates;
    }

    /**
     * @return WebClient return the webclient2
     */
    public WebClient getWebclient2() {
        return webclient2;
    }

    /**
     * @param webclient2 the webclient2 to set
     */
    public void setWebclient2(WebClient webclient2) {
        this.webclient2 = webclient2;
    }

    /**
     * @return WebClient return the webclient12
     */
    public WebClient getWebclient12() {
        return webclient12;
    }

    /**
     * @param webclient12 the webclient12 to set
     */
    public void setWebclient12(WebClient webclient12) {
        this.webclient12 = webclient12;
    }

    /**
     * @return TblTmp return the tmpdata
     */
    public TblTmp getTmpdata() {
        return tmpdata;
    }

    /**
     * @param tmpdata the tmpdata to set
     */
    public void setTmpdata(TblTmp tmpdata) {
        this.tmpdata = tmpdata;
    }

    /**
     * @return TblTmp return the tmpSavedData
     */
    public TblTmp getTmpSavedData() {
        return tmpSavedData;
    }

    /**
     * @param tmpSavedData the tmpSavedData to set
     */
    public void setTmpSavedData(TblTmp tmpSavedData) {
        this.tmpSavedData = tmpSavedData;
    }

    /**
     * @return boolean return the noMessagernd
     */
    public boolean isNoMessagernd() {
        return noMessagernd;
    }

    /**
     * @param noMessagernd the noMessagernd to set
     */
    public void setNoMessagernd(boolean noMessagernd) {
        this.noMessagernd = noMessagernd;
    }

    /**
     * @return boolean return the bankren
     */
    public boolean isBankren() {
        return bankren;
    }

    /**
     * @param bankren the bankren to set
     */
    public void setBankren(boolean bankren) {
        this.bankren = bankren;
    }

    /**
     * @return String return the correBank
     */
    public String getCorreBank() {
        return correBank;
    }

    /**
     * @param correBank the correBank to set
     */
    public void setCorreBank(String correBank) {
        this.correBank = correBank;
    }

    /**
     * @return String return the valueDateString
     */
    public String getValueDateString() {
        return valueDateString;
    }

    /**
     * @param valueDateString the valueDateString to set
     */
    public void setValueDateString(String valueDateString) {
        this.valueDateString = valueDateString;
    }

    /**
     * @return boolean return the otherbranchrend
     */
    public boolean isOtherbranchrend() {
        return otherbranchrend;
    }

    /**
     * @param otherbranchrend the otherbranchrend to set
     */
    public void setOtherbranchrend(boolean otherbranchrend) {
        this.otherbranchrend = otherbranchrend;
    }

    public String getSenderBank() {
        return senderBank;
    }

    public void setSenderBank(String senderBank) {
        this.senderBank = senderBank;
    }

    public String getNoMessage() {
        return noMessage;
    }

    public void setNoMessage(String noMessage) {
        this.noMessage = noMessage;
    }

    /**
     * @return List<TblIncomingRecord> return the mesitem
     */
    public List<TblIncomingRecord> getMesitem() {
        return mesitem;
    }

    /**
     * @param mesitem the mesitem to set
     */
    public void setMesitem(List<TblIncomingRecord> mesitem) {
        this.mesitem = mesitem;
    }

    public boolean isSavernd() {
        return savernd;
    }

    public void setSavernd(boolean savernd) {
        this.savernd = savernd;
    }

    public String getSaveMessage() {
        return saveMessage;
    }

    public void setSaveMessage(String saveMessage) {
        this.saveMessage = saveMessage;
    }

}
