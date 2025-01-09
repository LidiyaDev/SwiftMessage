package com.swiftmessage.swiftmessage.dto;

import java.math.BigDecimal;

public class MultiJrnl {
    BigDecimal SERIAL_NO;

    String DR_CR;

    String BRANCH_CODE;

    String ACCORGL;

    String ACCOUNT;

    String CCY;

    BigDecimal AMOUNT;

    String TXN_CODE;

    String INSTRUMENT_NO;

    BigDecimal LCY_AMOUNT;;

    String ADDL_TEXT;

    String CUSTOMER;

    BigDecimal EXCH_RATE;

    public MultiJrnl() {
    }

    public BigDecimal getSERIAL_NO() {
        return SERIAL_NO;
    }

    public void setSERIAL_NO(BigDecimal sERIAL_NO) {
        SERIAL_NO = sERIAL_NO;
    }

    public String getDR_CR() {
        return DR_CR;
    }

    

    public void setDR_CR(String dR_CR) {
        DR_CR = dR_CR;
    }

    public String getBRANCH_CODE() {
        return BRANCH_CODE;
    }

    public void setBRANCH_CODE(String bRANCH_CODE) {
        BRANCH_CODE = bRANCH_CODE;
    }

    public String getACCORGL() {
        return ACCORGL;
    }

    public void setACCORGL(String aCCORGL) {
        ACCORGL = aCCORGL;
    }

    public String getACCOUNT() {
        return ACCOUNT;
    }

    public void setACCOUNT(String aCCOUNT) {
        ACCOUNT = aCCOUNT;
    }

    public String getCCY() {
        return CCY;
    }

    public void setCCY(String cCY) {
        CCY = cCY;
    }

    public BigDecimal getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(BigDecimal aMOUNT) {
        AMOUNT = aMOUNT;
    }

    public String getTXN_CODE() {
        return TXN_CODE;
    }

    public void setTXN_CODE(String tXN_CODE) {
        TXN_CODE = tXN_CODE;
    }

    public String getADDL_TEXT() {
        return ADDL_TEXT;
    }

    public void setADDL_TEXT(String aDDL_TEXT) {
        ADDL_TEXT = aDDL_TEXT;
    }

    public String getCUSTOMER() {
        return CUSTOMER;
    }

    public void setCUSTOMER(String cUSTOMER) {
        CUSTOMER = cUSTOMER;
    }

    public BigDecimal getEXCH_RATE() {
        return EXCH_RATE;
    }

    public void setEXCH_RATE(BigDecimal eXCH_RATE) {
        EXCH_RATE = eXCH_RATE;
    }

    public BigDecimal getLCY_AMOUNT() {
        return LCY_AMOUNT;
    }

    public void setLCY_AMOUNT(BigDecimal lCY_AMOUNT) {
        LCY_AMOUNT = lCY_AMOUNT;
    }

    public String getINSTRUMENT_NO() {
        return INSTRUMENT_NO;
    }

    public void setINSTRUMENT_NO(String iNSTRUMENT_NO) {
        INSTRUMENT_NO = iNSTRUMENT_NO;
    }

    

    
    

}