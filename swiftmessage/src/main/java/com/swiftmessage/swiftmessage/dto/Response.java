package com.swiftmessage.swiftmessage.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class Response {

    //HEADER
    String SOURCE;

    String UBSCOMP;

    String MSGID;

    String CORRELID;

    String USERID;

    String ENTITY;

    String BRANCH;

    String MODULEID;

    String SERVICE;

    String OPERATION;

    String SOURCE_OPERATION;

    String DESTINATION;

    String FUNCTIONID;

    String ACTION;

    String MSGSTAT;

    //BODY

    String REFERENCE_NO;

    String BATCH_NO;

    BigDecimal CURR_NO;

    XMLGregorianCalendar VALUE_DATE;

    String BRANCH_CODE;

    String CCY;

    BigDecimal TOTAL_DR;

    BigDecimal TOTAL_CR;

    String MAKER;

    String MAKDTTIME;

    String CHECHKERID;

    String AUTHSTAT;

    String TXNSTAT;

    BigDecimal REC_NO;

    BigDecimal TOTAL_NO;

    String ERROR;

    List<MultiJrnl> detbsJrnlTxnDetail;
    BatchMaster devwsBatchMaster;
    public Response() {
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
    public String getENTITY() {
        return ENTITY;
    }
    public void setENTITY(String eNTITY) {
        ENTITY = eNTITY;
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
    public String getDESTINATION() {
        return DESTINATION;
    }
    public void setDESTINATION(String dESTINATION) {
        DESTINATION = dESTINATION;
    }
    public String getFUNCTIONID() {
        return FUNCTIONID;
    }
    public void setFUNCTIONID(String fUNCTIONID) {
        FUNCTIONID = fUNCTIONID;
    }
    public String getACTION() {
        return ACTION;
    }
    public void setACTION(String aCTION) {
        ACTION = aCTION;
    }
    public String getMSGSTAT() {
        return MSGSTAT;
    }
    public void setMSGSTAT(String mSGSTAT) {
        MSGSTAT = mSGSTAT;
    }
    public String getREFERENCE_NO() {
        return REFERENCE_NO;
    }
    public void setREFERENCE_NO(String rEFERENCE_NO) {
        REFERENCE_NO = rEFERENCE_NO;
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
    public XMLGregorianCalendar getVALUE_DATE() {
        return VALUE_DATE;
    }
    public void setVALUE_DATE(XMLGregorianCalendar vALUE_DATE) {
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
    public BigDecimal getTOTAL_DR() {
        return TOTAL_DR;
    }
    public void setTOTAL_DR(BigDecimal tOTAL_DR) {
        TOTAL_DR = tOTAL_DR;
    }
    public BigDecimal getTOTAL_CR() {
        return TOTAL_CR;
    }
    public void setTOTAL_CR(BigDecimal tOTAL_CR) {
        TOTAL_CR = tOTAL_CR;
    }
    public String getMAKER() {
        return MAKER;
    }
    public void setMAKER(String mAKER) {
        MAKER = mAKER;
    }
    public String getMAKDTTIME() {
        return MAKDTTIME;
    }
    public void setMAKDTTIME(String mAKDTTIME) {
        MAKDTTIME = mAKDTTIME;
    }
    public String getCHECHKERID() {
        return CHECHKERID;
    }
    public void setCHECHKERID(String cHECHKERID) {
        CHECHKERID = cHECHKERID;
    }
    public String getAUTHSTAT() {
        return AUTHSTAT;
    }
    public void setAUTHSTAT(String aUTHSTAT) {
        AUTHSTAT = aUTHSTAT;
    }
    public String getTXNSTAT() {
        return TXNSTAT;
    }
    public void setTXNSTAT(String tXNSTAT) {
        TXNSTAT = tXNSTAT;
    }
    public BigDecimal getREC_NO() {
        return REC_NO;
    }
    public void setREC_NO(BigDecimal rEC_NO) {
        REC_NO = rEC_NO;
    }
    public BigDecimal getTOTAL_NO() {
        return TOTAL_NO;
    }
    public void setTOTAL_NO(BigDecimal tOTAL_NO) {
        TOTAL_NO = tOTAL_NO;
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


    public String getERROR() {
        return ERROR;
    }


    public void setERROR(String eRROR) {
        ERROR = eRROR;
    }


    
    


}