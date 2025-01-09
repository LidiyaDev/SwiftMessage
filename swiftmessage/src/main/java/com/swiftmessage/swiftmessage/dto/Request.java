package com.swiftmessage.swiftmessage.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class Request {

    // HEADER

    String SOURCE;

    String UBSCOMP;

    String MSGID;

    String CORRELID;

    String USERID;

    String BRANCH;

    String MODULEID;

    String SERVICE;

    String OPERATION;

    String SOURCE_OPERATION;


    //BODY

    String BATCH_NO;

    BigDecimal CURR_NO;

    String VALUE_DATE;

    String BRANCH_CODE;

    String CCY;

    String AUTHSTAT;

    String CHECHKERID;

    List<MultiJrnl> detbsJrnlTxnDetail;
    BatchMaster devwsBatchMaster;

    public Request() {
    }
 
    public String getSOURCE() {
        return SOURCE;
    }
    public void setSOURCE(String sOURCE) {
        SOURCE = sOURCE;
    }
    public String getUBSCOMP() {
        return UBSCOMP;
    }
    public void setUBSCOMP(String uBSCOMP) {
        UBSCOMP = uBSCOMP;
    }
    public String getMSGID() {
        return MSGID;
    }
    public void setMSGID(String mSGID) {
        MSGID = mSGID;
    }
    public String getCORRELID() {
        return CORRELID;
    }
    public void setCORRELID(String cORRELID) {
        CORRELID = cORRELID;
    }
    public String getUSERID() {
        return USERID;
    }
    public void setUSERID(String uSERID) {
        USERID = uSERID;
    }
    public String getBRANCH() {
        return BRANCH;
    }
    public void setBRANCH(String bRANCH) {
        BRANCH = bRANCH;
    }
    public String getMODULEID() {
        return MODULEID;
    }
    public void setMODULEID(String mODULEID) {
        MODULEID = mODULEID;
    }
    public String getSERVICE() {
        return SERVICE;
    }
    public void setSERVICE(String sERVICE) {
        SERVICE = sERVICE;
    }
    public String getOPERATION() {
        return OPERATION;
    }
    public void setOPERATION(String oPERATION) {
        OPERATION = oPERATION;
    }
    public String getSOURCE_OPERATION() {
        return SOURCE_OPERATION;
    }
    public void setSOURCE_OPERATION(String sOURCE_OPERATION) {
        SOURCE_OPERATION = sOURCE_OPERATION;
    }
    public String getBATCH_NO() {
        return BATCH_NO;
    }
    public void setBATCH_NO(String bATCH_NO) {
        BATCH_NO = bATCH_NO;
    }
    public BigDecimal getCURR_NO() {
        return CURR_NO;
    }
    public void setCURR_NO(BigDecimal cURR_NO) {
        CURR_NO = cURR_NO;
    }
    public String getVALUE_DATE() {
        return VALUE_DATE;
    }
    public void setVALUE_DATE(String vALUE_DATE) {
        VALUE_DATE = vALUE_DATE;
    }
    public String getBRANCH_CODE() {
        return BRANCH_CODE;
    }
    public void setBRANCH_CODE(String bRANCH_CODE) {
        BRANCH_CODE = bRANCH_CODE;
    }
    public String getCCY() {
        return CCY;
    }
    public void setCCY(String cCY) {
        CCY = cCY;
    }
    public String getAUTHSTAT() {
        return AUTHSTAT;
    }
    public void setAUTHSTAT(String aUTHSTAT) {
        AUTHSTAT = aUTHSTAT;
    }
    public String getCHECHKERID() {
        return CHECHKERID;
    }
    public void setCHECHKERID(String cHECHKERID) {
        CHECHKERID = cHECHKERID;
    }
    public List<MultiJrnl> getDetbsJrnlTxnDetail() {
        return detbsJrnlTxnDetail;
    }
    public void setDetbsJrnlTxnDetail(List<MultiJrnl> detbsJrnlTxnDetail) {
        this.detbsJrnlTxnDetail = detbsJrnlTxnDetail;
    }
    public BatchMaster getDevwsBatchMaster() {
        return devwsBatchMaster;
    }
    public void setDevwsBatchMaster(BatchMaster devwsBatchMaster) {
        this.devwsBatchMaster = devwsBatchMaster;
    }


    
}