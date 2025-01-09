package com.swiftmessage.swiftmessage.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "TBL_INCOMING_RECORD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblIncomingRecord.findAll", query = "SELECT t FROM TblIncomingRecord t")
    , @NamedQuery(name = "TblIncomingRecord.findById", query = "SELECT t FROM TblIncomingRecord t WHERE t.id = :id")
    , @NamedQuery(name = "TblIncomingRecord.findByAccountNumber", query = "SELECT t FROM TblIncomingRecord t WHERE t.accountNumber = :accountNumber")
    , @NamedQuery(name = "TblIncomingRecord.findByBankReference", query = "SELECT t FROM TblIncomingRecord t WHERE t.bankReference = :bankReference")
    , @NamedQuery(name = "TblIncomingRecord.findByBirrAmount", query = "SELECT t FROM TblIncomingRecord t WHERE t.birrAmount = :birrAmount")
    , @NamedQuery(name = "TblIncomingRecord.findByIsDashenBranch", query = "SELECT t FROM TblIncomingRecord t WHERE t.isDashenBranch = :isDashenBranch")
    , @NamedQuery(name = "TblIncomingRecord.findByFcyAmount", query = "SELECT t FROM TblIncomingRecord t WHERE t.fcyAmount = :fcyAmount")
    , @NamedQuery(name = "TblIncomingRecord.findByOrderingCustomer", query = "SELECT t FROM TblIncomingRecord t WHERE t.orderingCustomer = :orderingCustomer")
    , @NamedQuery(name = "TblIncomingRecord.findByOtherBankBranch", query = "SELECT t FROM TblIncomingRecord t WHERE t.otherBankBranch = :otherBankBranch")
    , @NamedQuery(name = "TblIncomingRecord.findByRate", query = "SELECT t FROM TblIncomingRecord t WHERE t.rate = :rate")
    , @NamedQuery(name = "TblIncomingRecord.findByRecordState", query = "SELECT t FROM TblIncomingRecord t WHERE t.recordState = :recordState")
    , @NamedQuery(name = "TblIncomingRecord.findByReference", query = "SELECT t FROM TblIncomingRecord t WHERE t.reference = :reference")
    , @NamedQuery(name = "TblIncomingRecord.findByRegistrationDate", query = "SELECT t FROM TblIncomingRecord t WHERE t.registrationDate = :registrationDate")
    , @NamedQuery(name = "TblIncomingRecord.findBySenderBank", query = "SELECT t FROM TblIncomingRecord t WHERE t.senderBank = :senderBank")
    , @NamedQuery(name = "TblIncomingRecord.findByLcyrate", query = "SELECT t FROM TblIncomingRecord t WHERE t.lcyrate = :lcyrate")
    , @NamedQuery(name = "TblIncomingRecord.findByFcyrate", query = "SELECT t FROM TblIncomingRecord t WHERE t.fcyrate = :fcyrate")
    , @NamedQuery(name = "TblIncomingRecord.findByValueDate", query = "SELECT t FROM TblIncomingRecord t WHERE t.valueDate = :valueDate")
    , @NamedQuery(name = "TblIncomingRecord.findByAccountType", query = "SELECT t FROM TblIncomingRecord t WHERE t.accountType = :accountType")
    , @NamedQuery(name = "TblIncomingRecord.findByBeneficiary", query = "SELECT t FROM TblIncomingRecord t WHERE t.beneficiary = :beneficiary")
    , @NamedQuery(name = "TblIncomingRecord.findByDashenBranch", query = "SELECT t FROM TblIncomingRecord t WHERE t.dashenBranch = :dashenBranch")
    , @NamedQuery(name = "TblIncomingRecord.findByCorrespondentBank", query = "SELECT t FROM TblIncomingRecord t WHERE t.correspondentBank = :correspondentBank")
    , @NamedQuery(name = "TblIncomingRecord.findByCountryOfOrigin", query = "SELECT t FROM TblIncomingRecord t WHERE t.countryOfOrigin = :countryOfOrigin")
    , @NamedQuery(name = "TblIncomingRecord.findByCreatedBy", query = "SELECT t FROM TblIncomingRecord t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TblIncomingRecord.findByCurrencyId", query = "SELECT t FROM TblIncomingRecord t WHERE t.currencyId = :currencyId")
    , @NamedQuery(name = "TblIncomingRecord.findByPaymentPurpose", query = "SELECT t FROM TblIncomingRecord t WHERE t.paymentPurpose = :paymentPurpose")
    , @NamedQuery(name = "TblIncomingRecord.findByRegBy", query = "SELECT t FROM TblIncomingRecord t WHERE t.regBy = :regBy")
    , @NamedQuery(name = "TblIncomingRecord.findByRegDate", query = "SELECT t FROM TblIncomingRecord t WHERE t.regDate = :regDate")
    , @NamedQuery(name = "TblIncomingRecord.findByModifiedBy", query = "SELECT t FROM TblIncomingRecord t WHERE t.modifiedBy = :modifiedBy")
    , @NamedQuery(name = "TblIncomingRecord.findByModifiedDate", query = "SELECT t FROM TblIncomingRecord t WHERE t.modifiedDate = :modifiedDate")
    , @NamedQuery(name = "TblIncomingRecord.findByDeletedBy", query = "SELECT t FROM TblIncomingRecord t WHERE t.deletedBy = :deletedBy")
    , @NamedQuery(name = "TblIncomingRecord.findByDeletedDate", query = "SELECT t FROM TblIncomingRecord t WHERE t.deletedDate = :deletedDate")
    ,@NamedQuery(name = "TblIncomingRecord.findLastRecord", query = "SELECT t FROM TblIncomingRecord t ORDER BY id DESC")
 
     ,@NamedQuery(name = "TblIncomingRecord.findOtherBankTotal", query = "SELECT t FROM TblIncomingRecord t where t.dashenBranch IS NULL")

    // ,@NamedQuery(name = "TblIncomingRecord.findTopBranch", query = "SELECT SUM(t.fcyAmount) , t.dashenBranch FROM TblIncomingRecord t GROUP BY t.dashenBranch ORDER BY SUM(t.fcyAmount) DESC FETCH FIRST 4 ROWS ONLY")

    , @NamedQuery(name = "TblIncomingRecord.findByAccountNumberCurrency", query = "SELECT t FROM TblIncomingRecord t WHERE t.accountNumberCurrency = :accountNumberCurrency")})
public class TblIncomingRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SWIFT_MESSAGE_SEQ")
    @SequenceGenerator(name = "SWIFT_MESSAGE_SEQ", sequenceName = "SWIFT_MESSAGE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private BigDecimal id;
    //@Size(max = 255)
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    //@Size(max = 255)
    @Column(name = "BANK_REFERENCE")
    private String bankReference;
    @Column(name = "BIRR_AMOUNT")
    private Double birrAmount;
    @Column(name = "IS_DASHEN_BRANCH")
    private Character isDashenBranch;
    @Column(name = "FCY_AMOUNT")
    private Double fcyAmount;
    //@Size(max = 255)
    @Column(name = "ORDERING_CUSTOMER")
    private String orderingCustomer;
    //@Size(max = 255)
    @Column(name = "OTHER_BANK_BRANCH")
    private String otherBankBranch;
    @Column(name = "RATE")
    private Double rate;
    @Column(name = "RECORD_STATE")
    private Long recordState;
   // @Size(max = 255)
    @Column(name = "REFERENCE")
    private String reference;
    @Basic(optional = false)
   // @NotNull
    @Column(name = "REGISTRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    //@Size(max = 255)
    @Column(name = "SENDER_BANK")
    private String senderBank;
    @Column(name = "LCYRATE")
    private Double lcyrate;
    @Column(name = "FCYRATE")
    private Double fcyrate;
    @Column(name = "VALUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date valueDate;
    //@Size(max = 2000)
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 2000)
    @Column(name = "BENEFICIARY")
    private String beneficiary;
   // @Size(max = 2000)
    @Column(name = "DASHEN_BRANCH")
    private String dashenBranch;
    @Basic(optional = false)
   // @NotNull
   // @Size(min = 1, max = 2000)
    @Column(name = "CORRESPONDENT_BANK")
    private String correspondentBank;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 2000)
    @Column(name = "COUNTRY_OF_ORIGIN")
    private String countryOfOrigin;
    @Basic(optional = false)
   // @NotNull
    //@Size(min = 1, max = 20)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Basic(optional = false)
   // @NotNull
   // @Size(min = 1, max = 20)
    @Column(name = "CURRENCY_ID")
    private String currencyId;
    //@Size(max = 255)
    @Column(name = "PAYMENT_PURPOSE")
    private String paymentPurpose;
   // @Size(max = 80)
    @Column(name = "REG_BY")
    private String regBy;
    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
   // @Size(max = 120)
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
    @Column(name = "MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
   // @Size(max = 120)
    @Column(name = "DELETED_BY")
    private String deletedBy;
    @Column(name = "TOTAL_SERVICE_CHARGE")
    private String totalServiceCharge;

    @Column(name = "DELETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    //@Size(max = 200)
    @Column(name = "ACCOUNT_NUMBER_CURRENCY")
    private String accountNumberCurrency;
    @OneToMany(mappedBy = "message")
    private List<SwiftTransaction> swiftTransactionList;

    public TblIncomingRecord() {
    }

    public TblIncomingRecord(BigDecimal id) {
        this.id = id;
    }

    public TblIncomingRecord(BigDecimal id, Date registrationDate, String beneficiary, String correspondentBank, String countryOfOrigin, String createdBy, String currencyId) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.beneficiary = beneficiary;
        this.correspondentBank = correspondentBank;
        this.countryOfOrigin = countryOfOrigin;
        this.createdBy = createdBy;
        this.currencyId = currencyId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankReference() {
        return bankReference;
    }

    public void setBankReference(String bankReference) {
        this.bankReference = bankReference;
    }

    public Double getBirrAmount() {
        return birrAmount;
    }

    public void setBirrAmount(Double birrAmount) {
        this.birrAmount = birrAmount;
    }

    public Character getIsDashenBranch() {
        return isDashenBranch;
    }

    public void setIsDashenBranch(Character isDashenBranch) {
        this.isDashenBranch = isDashenBranch;
    }

    public Double getFcyAmount() {
        return fcyAmount;
    }

    public void setFcyAmount(Double fcyAmount) {
        this.fcyAmount = fcyAmount;
    }

    public String getOrderingCustomer() {
        return orderingCustomer;
    }

    public void setOrderingCustomer(String orderingCustomer) {
        this.orderingCustomer = orderingCustomer;
    }

    public String getOtherBankBranch() {
        return otherBankBranch;
    }

    public void setOtherBankBranch(String otherBankBranch) {
        this.otherBankBranch = otherBankBranch;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getRecordState() {
        return recordState;
    }

    public void setRecordState(Long recordState) {
        this.recordState = recordState;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSenderBank() {
        return senderBank;
    }

    public void setSenderBank(String senderBank) {
        this.senderBank = senderBank;
    }

    public Double getLcyrate() {
        return lcyrate;
    }

    public void setLcyrate(Double lcyrate) {
        this.lcyrate = lcyrate;
    }

    public Double getFcyrate() {
        return fcyrate;
    }

    public void setFcyrate(Double fcyrate) {
        this.fcyrate = fcyrate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getDashenBranch() {
        return dashenBranch;
    }

    public void setDashenBranch(String dashenBranch) {
        this.dashenBranch = dashenBranch;
    }

    public String getCorrespondentBank() {
        return correspondentBank;
    }

    public void setCorrespondentBank(String correspondentBank) {
        this.correspondentBank = correspondentBank;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }

    public String getRegBy() {
        return regBy;
    }

    public void setRegBy(String regBy) {
        this.regBy = regBy;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public String getTotalServiceCharge() {
        return totalServiceCharge;
    }

    public void setTotalServiceCharge(String totalServiceCharge) {
        this.totalServiceCharge = totalServiceCharge;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getAccountNumberCurrency() {
        return accountNumberCurrency;
    }

    public void setAccountNumberCurrency(String accountNumberCurrency) {
        this.accountNumberCurrency = accountNumberCurrency;
    }

    @XmlTransient
    public List<SwiftTransaction> getSwiftTransactionList() {
        return swiftTransactionList;
    }

    public void setSwiftTransactionList(List<SwiftTransaction> swiftTransactionList) {
        this.swiftTransactionList = swiftTransactionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblIncomingRecord)) {
            return false;
        }
        TblIncomingRecord other = (TblIncomingRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TblIncomingRecord[ id=" + id + " ]";
    }
    
}
