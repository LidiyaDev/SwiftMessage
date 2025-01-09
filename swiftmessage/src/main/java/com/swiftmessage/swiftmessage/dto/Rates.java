package com.swiftmessage.swiftmessage.dto;

import java.util.Date;

public class Rates {
    private Double BUY_RATE;
    private Double SALE_RATE;
    private Date RATE_DATE;
    private String CCY1;
    private String CCY2;
    private String RATE_TYPE;
    private Double MID_RATE;
    public Double getBUY_RATE() {
        return BUY_RATE;
    }
    public void setBUY_RATE(Double bUY_RATE) {
        BUY_RATE = bUY_RATE;
    }
    public Double getSALE_RATE() {
        return SALE_RATE;
    }
    public void setSALE_RATE(Double sALE_RATE) {
        SALE_RATE = sALE_RATE;
    }
    public Date getRATE_DATE() {
        return RATE_DATE;
    }
    public void setRATE_DATE(Date rATE_DATE) {
        RATE_DATE = rATE_DATE;
    }
    public String getCCY1() {
        return CCY1;
    }
    public void setCCY1(String cCY1) {
        CCY1 = cCY1;
    }
    public String getCCY2() {
        return CCY2;
    }
    public void setCCY2(String cCY2) {
        CCY2 = cCY2;
    }
    public String getRATE_TYPE() {
        return RATE_TYPE;
    }
    public void setRATE_TYPE(String rATE_TYPE) {
        RATE_TYPE = rATE_TYPE;
    }
    public Double getMID_RATE() {
        return MID_RATE;
    }
    public void setMID_RATE(Double mID_RATE) {
        MID_RATE = mID_RATE;
    }
    

    
}