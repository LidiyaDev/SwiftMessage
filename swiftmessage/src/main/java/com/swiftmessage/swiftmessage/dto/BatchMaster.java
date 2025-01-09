package com.swiftmessage.swiftmessage.dto;

import java.math.BigDecimal;

public class BatchMaster {

    String BATCH_NUMBER;

    String DESCRIPTION;

    BigDecimal DEBIT;

    BigDecimal CREDIT;

    String BALANCING;

    public BatchMaster() {
    }

    public String getBATCH_NUMBER() {
        return BATCH_NUMBER;
    }

    public void setBATCH_NUMBER(String bATCH_NUMBER) {
        BATCH_NUMBER = bATCH_NUMBER;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String dESCRIPTION) {
        DESCRIPTION = dESCRIPTION;
    }

    public BigDecimal getDEBIT() {
        return DEBIT;
    }

    public void setDEBIT(BigDecimal dEBIT) {
        DEBIT = dEBIT;
    }

    public BigDecimal getCREDIT() {
        return CREDIT;
    }

    public void setCREDIT(BigDecimal cREDIT) {
        CREDIT = cREDIT;
    }

    public String getBALANCING() {
        return BALANCING;
    }

    public void setBALANCING(String bALANCING) {
        BALANCING = bALANCING;
    }

    

}