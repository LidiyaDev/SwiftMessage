package com.swiftmessage.swiftmessage.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HISTORY_RATE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExRateLocal {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FETCHED_SEQ")
    @SequenceGenerator(name = "FETCHED_SEQ", sequenceName = "FETCHED_SEQ", allocationSize = 1)

    @Column(name = "ID")
    private Long id;

    private String BRANCH_CODE;
    private String CCY1;
	private String CCY2;
	private String RATE_TYPE;
	private Date RATE_DT_STAMP;
	private String MID_RATE;
	private String BUY_RATE;
	private String SALE_RATE;
	private String RATE_SERIAL;
	private String RATE_DATE;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBRANCH_CODE() {
        return BRANCH_CODE;
    }
    public void setBRANCH_CODE(String bRANCH_CODE) {
        BRANCH_CODE = bRANCH_CODE;
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
    public Date getRATE_DT_STAMP() {
        return RATE_DT_STAMP;
    }
    public void setRATE_DT_STAMP(Date rATE_DT_STAMP) {
        RATE_DT_STAMP = rATE_DT_STAMP;
    }
    public String getMID_RATE() {
        return MID_RATE;
    }
    public void setMID_RATE(String mID_RATE) {
        MID_RATE = mID_RATE;
    }
    public String getBUY_RATE() {
        return BUY_RATE;
    }
    public void setBUY_RATE(String bUY_RATE) {
        BUY_RATE = bUY_RATE;
    }
    public String getSALE_RATE() {
        return SALE_RATE;
    }
    public void setSALE_RATE(String sALE_RATE) {
        SALE_RATE = sALE_RATE;
    }
    public String getRATE_SERIAL() {
        return RATE_SERIAL;
    }
    public void setRATE_SERIAL(String rATE_SERIAL) {
        RATE_SERIAL = rATE_SERIAL;
    }
    public String getRATE_DATE() {
        return RATE_DATE;
    }
    public void setRATE_DATE(String rATE_DATE) {
        RATE_DATE = rATE_DATE;
    }


    
}

