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

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import com.swiftmessage.swiftmessage.dto.AccountInfo;
import com.swiftmessage.swiftmessage.dto.BatchMaster;
import com.swiftmessage.swiftmessage.dto.MultiJrnl;
import com.swiftmessage.swiftmessage.dto.Request;
import com.swiftmessage.swiftmessage.dto.Response;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.entity.TblCorrespondentBank;
import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.PaymentRestClient;
import com.swiftmessage.swiftmessage.service.SwiftTransactionService;
import com.swiftmessage.swiftmessage.service.TblCorrespondentBankService;



import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RetaTransactionController {
    @Autowired
    SwiftTransactionService swifttransactionservice;
    @Autowired
    TblCorrespondentBankService corrService;

    // private static final String baseUrpy =
    // "http://192.168.12.47:7088/cbsservice/transaction/";//
    // "http://localhost:8085/cbsservice/transaction/";
    // private WebClient webclientpy = WebClient.create(baseUrpy);
    @Autowired
    PaymentRestClient paymentClient;

    private SwiftTransaction selected;
    private List<SwiftTransaction> items;
    private List<SwiftTransaction> retaitems;
    private List<SwiftTransaction> nretaitems;
    private List<SwiftTransaction> mulselect;

    String baseUrl2 = "http://192.168.12.47:6070/getByAccountNo/";
    WebClient webclient2 = WebClient.create(baseUrl2);
    AccountRestClient accountRestClient = new AccountRestClient(webclient2);

    // @Autowired
    // AccountRestClient accountRestClient;

    private String username;

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SwiftTransaction> getItems() {
        return items;
    }

    public void setItems(List<SwiftTransaction> items) {
        this.items = items;
    }

    public List<SwiftTransaction> getRetaitems() {
        return retaitems;
    }

    public void setRetaitems(List<SwiftTransaction> retaitems) {
        this.retaitems = retaitems;
    }

    public List<SwiftTransaction> getNretaitems() {
        return nretaitems;
    }

    public void setNretaitems(List<SwiftTransaction> nretaitems) {
        this.nretaitems = nretaitems;
    }

    public List<SwiftTransaction> getMulselect() {
        return mulselect;
    }

    public void setMulselect(List<SwiftTransaction> mulselect) {
        this.mulselect = mulselect;
    }

    public void prepareSelect(SwiftTransaction item) {
        if (item != null) {

            System.out.println("test item" + item);
            selected = item;
        }

    }

    public String rejectTransaction() {
        for (int i = 0; i < mulselect.size(); i++) {
            mulselect.get(i).setStatus("Reject");
            swifttransactionservice.update(mulselect.get(i));
            items = swifttransactionservice.findAll();
            System.out.println("updated");

        }

        return "forward:/Transaction/View";
    }

    public String autorizeTransaction() throws ParseException {

        System.out.println(mulselect + "   multiple");
        for (int i = 0; i < mulselect.size(); i++) {
            if (mulselect.get(i).getStatus().equalsIgnoreCase("Liquidated")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Failed to Save", "Transaction Already Authorized..."));
                break;
            }

            FacesContext context = FacesContext.getCurrentInstance();

            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

            RefreshableKeycloakSecurityContext c = (RefreshableKeycloakSecurityContext) request

                    .getAttribute(KeycloakSecurityContext.class.getName());
            username = c.getToken().getPreferredUsername();

            String correspondentAccount = null;
            List<TblCorrespondentBank> correspondentBank = corrService
                    .findBycodeandname(mulselect.get(i).getMessage().getCorrespondentBank());
            System.out.println(mulselect.get(i).getMessage().getCorrespondentBank() + "Code");
            System.out.println(correspondentBank + "data");
            if (correspondentBank.size() > 0) {
                for (TblCorrespondentBank tblCorrespondentBank : correspondentBank) {
                    if (tblCorrespondentBank.getCurrency()
                            .equalsIgnoreCase(mulselect.get(i).getMessage().getCurrencyId())) {
                        correspondentAccount = correspondentBank.get(0).getBankAccount();
                    }
                }
                Request transaction = new Request();

                transaction.setBATCH_NO("752m");
                transaction.setCURR_NO(new BigDecimal(1));

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(mulselect.get(i).getMessage().getValueDate());
                System.out.println(strDate + "===========strDate");

                transaction.setVALUE_DATE((strDate));
                transaction.setBRANCH_CODE("006");
                // transaction.setCCY("USD");
                transaction.setAUTHSTAT("A");
                // transaction.setCHECHKERID("UAT9");
                System.out.println("   test 123");
                List<MultiJrnl> mj = new ArrayList();
                // Multi Jornal Task

                MultiJrnl mj1 = new MultiJrnl();

                mj1.setSERIAL_NO(new BigDecimal(1));
                mj1.setDR_CR("D");
                System.out.println("correspondentAccount"+correspondentAccount);
                AccountInfo accountDto5 = accountRestClient.getAccountInfo(correspondentAccount);
                System.out.println(accountDto5.toString());

                mj1.setBRANCH_CODE(accountDto5.getBRANCH_CODE());

                mj1.setACCOUNT(correspondentAccount);

                mj1.setCCY(accountDto5.getCCY());
                mj1.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getMessage().getFcyAmount()));
                mj1.setTXN_CODE("075");
                mj1.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                mj1.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer()+"/"+mulselect.get(i).getMessage().getCountryOfOrigin());
                // mj1.setCUSTOMER(accountDto5.getCUST_NO());
                mj1.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                // System.out.println(transaction.getDetbsJrnlTxnDetail());
                // transaction.getDetbsJrnlTxnDetail().add(mj1);
                mj.add(mj1);
                System.out.println("   test 1234");

                MultiJrnl bmj2 = new MultiJrnl();
                MultiJrnl fmj2 = new MultiJrnl();
                fmj2.setSERIAL_NO(new BigDecimal(2));
                fmj2.setDR_CR("C");
                fmj2.setACCORGL("A");
                fmj2.setTXN_CODE("075");
                fmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());
                fmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer()+"/"+mulselect.get(i).getMessage().getCountryOfOrigin());
                fmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getFcyAmount()));

                if (mulselect.get(i).getOtherAccountNumber() == null) {
                    AccountInfo accountDto = new AccountInfo();
                    accountDto = accountRestClient
                            .getAccountInfo(mulselect.get(i).getMessage().getAccountNumber());
                    fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                    fmj2.setACCOUNT(mulselect.get(i).getMessage().getAccountNumber());
                    fmj2.setCCY(accountDto.getCCY());
                    fmj2.setCUSTOMER(accountDto.getCUST_NO());
                } else {
                    System.out.println("here mommmmm"+mulselect.get(i).getOtherAccountNumber());
                    AccountInfo accountDto = new AccountInfo();
                     accountDto = accountRestClient.getAccountInfo(mulselect.get(i).getOtherAccountNumber());
                    fmj2.setBRANCH_CODE(accountDto.getBRANCH_CODE());
                    fmj2.setACCOUNT(mulselect.get(i).getOtherAccountNumber());
                    fmj2.setCCY(accountDto.getCCY());
                    fmj2.setCUSTOMER(accountDto.getCUST_NO());
                }

                fmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getFcyRate()));
                mj.add(fmj2);

                bmj2.setSERIAL_NO(new BigDecimal(3));
                bmj2.setDR_CR("C");
                AccountInfo accountDto2 = new AccountInfo();
               accountDto2 =  accountRestClient.getAccountInfo(mulselect.get(i).getRetaBirrAccount());
                bmj2.setBRANCH_CODE(accountDto2.getBRANCH_CODE());
                bmj2.setACCORGL("A");
                bmj2.setACCOUNT(mulselect.get(i).getRetaBirrAccount());
                bmj2.setCCY("ETB");
                bmj2.setAMOUNT(BigDecimal.valueOf(mulselect.get(i).getBirrAmount()));

                bmj2.setTXN_CODE("075");
                bmj2.setINSTRUMENT_NO(mulselect.get(i).getMessage().getReference());

                bmj2.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer()+"/"+mulselect.get(i).getMessage().getCountryOfOrigin());
                bmj2.setCUSTOMER(accountDto2.getCUST_NO());
                bmj2.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                mj.add(bmj2);
                System.out.println("   test 1235");
                double rounddebitBirrAmount;
                double debitBirrAmount;
                if(mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("ETB"))
                {
                     debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount();
                      rounddebitBirrAmount = Math.round(debitBirrAmount* Math.pow(10, 2))/Math.pow(10, 2);
                }
                else{
                    if(mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("CHF") || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("JPY") || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("AED")
                        || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SEK") || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("NOK") || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("DKK")
                         || mulselect.get(i).getMessage().getCurrencyId().equalsIgnoreCase("SAR"))
                         {
                            debitBirrAmount = (mulselect.get(i).getMessage().getFcyAmount()* mulselect.get(i).getMessage().getRate())/100;
                         }
                         else
                         {
                            debitBirrAmount = mulselect.get(i).getMessage().getFcyAmount()
                            * mulselect.get(i).getMessage().getRate();
                         }
                     
                 rounddebitBirrAmount = Math.round(debitBirrAmount* Math.pow(10, 2))/Math.pow(10, 2);
                }

                
               
                System.out.println(rounddebitBirrAmount + "debitBirrAmount");
                double creditBirrAmount = (mulselect.get(i).getFcyAmount() * mulselect.get(i).getFcyRate())
                        + (mulselect.get(i).getBirrAmount());
                double roundcreditBirrAmount = Math.round(creditBirrAmount* Math.pow(10, 2))/Math.pow(10, 2);
                //double roundcreditBirrAmount = Double.parseDouble(df.format(creditBirrAmount));
                System.out.println(roundcreditBirrAmount + "creditBirrAmount");
                double difference = rounddebitBirrAmount - roundcreditBirrAmount;
                double rounddifference = Math.round(difference* Math.pow(10, 2))/Math.pow(10, 2);
               
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
                    bbmj.setBRANCH_CODE(accountDto2.getBRANCH_CODE());
                    bbmj.setACCORGL("G");
                    //bbmj.setACCOUNT("350430005");
                    bbmj.setCCY("ETB");
                    bbmj.setAMOUNT(BigDecimal.valueOf(Math.abs(rounddifference)));

                    bbmj.setTXN_CODE("075");

                    bbmj.setADDL_TEXT(mulselect.get(i).getMessage().getOrderingCustomer());
                    bbmj.setCUSTOMER(accountDto2.getCUST_NO());
                    bbmj.setEXCH_RATE(BigDecimal.valueOf(mulselect.get(i).getMessage().getRate()));
                    mj.add(bbmj);
                    System.out.println("   test 1235");

                }

                // transaction.getDetbsJrnlTxnDetail().add(mj2);

                transaction.setDetbsJrnlTxnDetail(mj);

                System.out.println(transaction.getDetbsJrnlTxnDetail() + "   test DetbsJrnlTxnDetail");

                BatchMaster bm1 = new BatchMaster();
                bm1.setBATCH_NUMBER("752m");
                bm1.setDESCRIPTION("752m");
                bm1.setDEBIT(BigDecimal.valueOf(debitBirrAmount));

                bm1.setCREDIT(BigDecimal.valueOf(debitBirrAmount));
                bm1.setBALANCING("Y");

                transaction.setDevwsBatchMaster(bm1);

                Response response = paymentClient.autorizeTransaction(transaction);

                // System.out.println(response.getREFERENCE_NO() + " reference");
                if (response.getMSGSTAT().equalsIgnoreCase("SUCCESS")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Transaction Authorized",
                            "Successfully Saved and Authorized..." + response.getREFERENCE_NO()));
                    mulselect.get(i).setStatus("Liquidated");
                    mulselect.get(i).setTransactionNumber(response.getREFERENCE_NO());
                    mulselect.get(i).setUpdatedBy(username);
                    mulselect.get(i).setUpdatedDate(new Date());
                    mulselect.get(i).setBatchnumber(response.getBATCH_NO());
                    mulselect.get(i).setMessageid(response.getMSGID());
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Failed to Save", "Failed to Save the transaction..."));
                    mulselect.get(i).setStatus("Cancelled");
                    mulselect.get(i).setUpdatedBy(username);
                    mulselect.get(i).setDeletedBy(response.getERROR());

                }
                swifttransactionservice.update(mulselect.get(i));

                System.out.println(response + "   test 123");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Failed to Save", "No Account Number regestered under this correspondent bank"));
            }

        }
        retaitems = swifttransactionservice.findByAccountType();

        return "forward:/Transaction/retentionTransaction.xhtml";

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