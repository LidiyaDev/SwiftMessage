package com.swiftmessage.swiftmessage.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author user
 */
@Entity
@Table(name = "SWIFT_TRANSACTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SwiftTransaction.findAll", query = "SELECT s FROM SwiftTransaction s")
    
    , @NamedQuery(name = "SwiftTransaction.findById", query = "SELECT s FROM SwiftTransaction s WHERE s.id = :id")
    , @NamedQuery(name = "SwiftTransaction.findByLcyPercentage", query = "SELECT s FROM SwiftTransaction s WHERE s.lcyPercentage = :lcyPercentage")
    , @NamedQuery(name = "SwiftTransaction.findByFcyPercentage", query = "SELECT s FROM SwiftTransaction s WHERE s.fcyPercentage = :fcyPercentage")
    , @NamedQuery(name = "SwiftTransaction.findByBirrAmount", query = "SELECT s FROM SwiftTransaction s WHERE s.birrAmount = :birrAmount")
    , @NamedQuery(name = "SwiftTransaction.findByOtherAccountNumber", query = "SELECT s FROM SwiftTransaction s WHERE s.otherAccountNumber = :otherAccountNumber")
    , @NamedQuery(name = "SwiftTransaction.findByOtherAccountName", query = "SELECT s FROM SwiftTransaction s WHERE s.otherAccountName = :otherAccountName")
    , @NamedQuery(name = "SwiftTransaction.findByOtherAccountCurrency", query = "SELECT s FROM SwiftTransaction s WHERE s.otherAccountCurrency = :otherAccountCurrency")
    , @NamedQuery(name = "SwiftTransaction.findByRetaBirrAccount", query = "SELECT s FROM SwiftTransaction s WHERE s.retaBirrAccount = :retaBirrAccount")
    , @NamedQuery(name = "SwiftTransaction.findByRetaBirrAccountName", query = "SELECT s FROM SwiftTransaction s WHERE s.retaBirrAccountName = :retaBirrAccountName")
    , @NamedQuery(name = "SwiftTransaction.findByCreatedBy", query = "SELECT s FROM SwiftTransaction s WHERE s.createdBy = :createdBy")
    , @NamedQuery(name = "SwiftTransaction.findByCreatedDate", query = "SELECT s FROM SwiftTransaction s WHERE s.createdDate = :createdDate")
    , @NamedQuery(name = "SwiftTransaction.findByUpdatedBy", query = "SELECT s FROM SwiftTransaction s WHERE s.updatedBy = :updatedBy")
    , @NamedQuery(name = "SwiftTransaction.findByUpdatedDate", query = "SELECT s FROM SwiftTransaction s WHERE s.updatedDate = :updatedDate")
    , @NamedQuery(name = "SwiftTransaction.findByDeletedBy", query = "SELECT s FROM SwiftTransaction s WHERE s.deletedBy = :deletedBy")
    , @NamedQuery(name = "SwiftTransaction.findByDeletedDate", query = "SELECT s FROM SwiftTransaction s WHERE s.deletedDate = :deletedDate")
    , @NamedQuery(name = "SwiftTransaction.findByStatus", query = "SELECT s FROM SwiftTransaction s WHERE s.status = :status")
    , @NamedQuery(name = "SwiftTransaction.findByMessageStatus", query = "SELECT s FROM SwiftTransaction s WHERE (s.message.accountType = 'RETA' OR s.retaBirrAccount != NULL) AND s.status = 'Liquidated' ORDER BY s.message.registrationDate DESC ")

    , @NamedQuery(name = "SwiftTransaction.findByMessageStatusPending", query = "SELECT s FROM SwiftTransaction s WHERE (s.message.accountType = 'RETA' OR s.retaBirrAccount != NULL) AND s.status = 'Pending'  ORDER BY s.message.registrationDate DESC ")

    , @NamedQuery(name = "SwiftTransaction.findByMessageStatusNreta", query = "SELECT s FROM SwiftTransaction s WHERE s.message.accountType != 'RETA' AND s.retaBirrAccount = NULL AND s.status = 'Liquidated' ORDER BY s.message.registrationDate DESC ")

    , @NamedQuery(name = "SwiftTransaction.findByMessageStatusNretaPending", query = "SELECT s FROM SwiftTransaction s WHERE s.message.accountType != 'RETA' AND s.retaBirrAccount = NULL AND s.status IN ('Pending', 'Cancelled')  ORDER BY s.message.registrationDate DESC ")

    , @NamedQuery(name = "SwiftTransaction.findByMessageReference", query = "SELECT s FROM SwiftTransaction s WHERE s.message.reference = :reference AND s.status != 'Reject'")

    , @NamedQuery(name = "SwiftTransaction.findByMessageOther", query = "SELECT s FROM SwiftTransaction s WHERE s.message.accountType != 'RETA'")

    , @NamedQuery(name = "SwiftTransaction.findByBnDate", query = "SELECT s FROM SwiftTransaction s WHERE s.status = 'Liquidated' AND (s.message.regDate BETWEEN :start AND :end) AND s.message.accountType != 'RETA' ")

    , @NamedQuery(name = "SwiftTransaction.findByBnDateReta", query = "SELECT s FROM SwiftTransaction s WHERE s.status = 'Liquidated' AND (s.message.regDate BETWEEN :start AND :end) AND s.message.currencyId != 'ETB' ")

    , @NamedQuery(name = "SwiftTransaction.findByBnDateAll", query = "SELECT s FROM SwiftTransaction s WHERE s.status = 'Liquidated' AND (s.message.regDate BETWEEN :start AND :end) ")


    ,@NamedQuery(name = "SwiftTransaction.findByBnDateSingle", query = "SELECT s FROM SwiftTransaction s WHERE s.status = 'Liquidated' AND TRUNC(s.message.regDate) = TRUNC(:end)"
)


    , @NamedQuery(name = "SwiftTransaction.findByAccountClass", query = "SELECT s FROM SwiftTransaction s WHERE s.status = 'Liquidated' AND (s.message.accountType = :accountType) ")

    , @NamedQuery(name = "SwiftTransaction.findByCategory", query = "SELECT s FROM SwiftTransaction s WHERE s.status = 'Liquidated' AND (s.customerType = :customerType) ")

    , @NamedQuery(name = "SwiftTransaction.findByBranch", query = "SELECT s FROM SwiftTransaction s WHERE s.status = 'Liquidated' AND (s.message.dashenBranch = :dashenBranch) ")

    

    , @NamedQuery(name = "SwiftTransaction.findByTransactionNumber", query = "SELECT s FROM SwiftTransaction s WHERE s.transactionNumber = :transactionNumber")})
public class SwiftTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTION_SEQ")
    @SequenceGenerator(name = "TRANSACTION_SEQ", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "LCY_PERCENTAGE")
    private Double lcyPercentage;
    @Column(name = "FCY_PERCENTAGE")
    private Double fcyPercentage;
    @Column(name = "BIRR_AMOUNT")
    private Double birrAmount;
    @Column(name = "FCY_AMOUNT")
    private Double fcyAmount;
   // @Size(max = 200)
    @Column(name = "OTHER_ACCOUNT_NUMBER")
    private String otherAccountNumber;
   // @Size(max = 2000)
    @Column(name = "OTHER_ACCOUNT_NAME")
    private String otherAccountName;
   // @Size(max = 200)
    @Column(name = "OTHER_ACCOUNT_CURRENCY")
    private String otherAccountCurrency;
   // @Size(max = 200)
    @Column(name = "RETA_BIRR_ACCOUNT")
    private String retaBirrAccount;
    //@Size(max = 2000)
    @Column(name = "RETA_BIRR_ACCOUNT_NAME")
    private String retaBirrAccountName;
    //@Size(max = 200)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
   // @Size(max = 200)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
   // @Size(max = 200)
    @Column(name = "DELETED_BY")
    private String deletedBy;
    @Column(name = "DELETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;

    @Column(name = "FCY_RATE")
    private Double fcyRate;
    //@Size(max = 200)
    @Column(name = "STATUS")
    private String status;

    @Column(name = "RETA_ACCOUNT_CURRENCY")
    private String retaAccountCurrency;

    @Column(name = "CUSTOMER_TYPE")
    private String customerType;

    @Column(name = "BATCHNUMBER")
    private String batchnumber;

    @Column(name = "MESSAGEID")
    private String messageid;

   // @Size(max = 2000)
    @Column(name = "TRANSACTION_NUMBER")
    private String transactionNumber;
    @JoinColumn(name = "MESSAGE", referencedColumnName = "ID")
    @ManyToOne
    private TblIncomingRecord message;

    public SwiftTransaction() {
    }

    public SwiftTransaction(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Double getLcyPercentage() {
        return lcyPercentage;
    }

    public void setLcyPercentage(Double lcyPercentage) {
        this.lcyPercentage = lcyPercentage;
    }

    public Double getFcyPercentage() {
        return fcyPercentage;
    }

    public void setFcyPercentage(Double fcyPercentage) {
        this.fcyPercentage = fcyPercentage;
    }

    public Double getBirrAmount() {
        return birrAmount;
    }

    public void setBirrAmount(Double birrAmount) {
        this.birrAmount = birrAmount;
    }

    

    public Double getFcyAmount() {
        return fcyAmount;
    }

    public void setFcyAmount(Double fcyAmount) {
        this.fcyAmount = fcyAmount;
    }

    public String getOtherAccountNumber() {
        return otherAccountNumber;
    }

    public void setOtherAccountNumber(String otherAccountNumber) {
        this.otherAccountNumber = otherAccountNumber;
    }

    public String getOtherAccountName() {
        return otherAccountName;
    }

    public void setOtherAccountName(String otherAccountName) {
        this.otherAccountName = otherAccountName;
    }

    public String getOtherAccountCurrency() {
        return otherAccountCurrency;
    }

    public void setOtherAccountCurrency(String otherAccountCurrency) {
        this.otherAccountCurrency = otherAccountCurrency;
    }

    public String getRetaBirrAccount() {
        return retaBirrAccount;
    }

    public void setRetaBirrAccount(String retaBirrAccount) {
        this.retaBirrAccount = retaBirrAccount;
    }

    public String getRetaBirrAccountName() {
        return retaBirrAccountName;
    }

    public void setRetaBirrAccountName(String retaBirrAccountName) {
        this.retaBirrAccountName = retaBirrAccountName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
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

    

    public Double getFcyRate() {
        return fcyRate;
    }

    public void setFcyRate(Double fcyRate) {
        this.fcyRate = fcyRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public TblIncomingRecord getMessage() {
        return message;
    }

    public void setMessage(TblIncomingRecord message) {
        this.message = message;
    }


    


    public String getRetaAccountCurrency() {
        return retaAccountCurrency;
    }

    public void setRetaAccountCurrency(String retaAccountCurrency) {
        this.retaAccountCurrency = retaAccountCurrency;
    }

    

    public String getBatchnumber() {
        return batchnumber;
    }

    public void setBatchnumber(String batchnumber) {
        this.batchnumber = batchnumber;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
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
        if (!(object instanceof SwiftTransaction)) {
            return false;
        }
        SwiftTransaction other = (SwiftTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SwiftTransaction[ id=" + id + " ]";
    }
    
}
