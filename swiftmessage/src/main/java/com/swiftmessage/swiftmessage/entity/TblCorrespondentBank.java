package com.swiftmessage.swiftmessage.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "TBL_CORRESPONDENT_BANK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCorrespondentBank.findAll", query = "SELECT t FROM TblCorrespondentBank t")
    , @NamedQuery(name = "TblCorrespondentBank.findById", query = "SELECT t FROM TblCorrespondentBank t WHERE t.id = :id")
    , @NamedQuery(name = "TblCorrespondentBank.findByCreatedDate", query = "SELECT t FROM TblCorrespondentBank t WHERE t.createdDate = :createdDate")
    , @NamedQuery(name = "TblCorrespondentBank.findByCreatedBy", query = "SELECT t FROM TblCorrespondentBank t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TblCorrespondentBank.findByName", query = "SELECT t FROM TblCorrespondentBank t WHERE t.name = :name")
    , @NamedQuery(name = "TblCorrespondentBank.findByCode", query = "SELECT t FROM TblCorrespondentBank t WHERE t.code = :code")

    , @NamedQuery(name = "TblCorrespondentBank.findByCodeandname", query = "SELECT t FROM TblCorrespondentBank t WHERE t.code = :code or t.name = :code")

    , @NamedQuery(name = "TblCorrespondentBank.findByLikeCode", query = "SELECT t FROM TblCorrespondentBank t WHERE t.code LIKE ?1 AND currency =?2")
    , @NamedQuery(name = "TblCorrespondentBank.findByRemark", query = "SELECT t FROM TblCorrespondentBank t WHERE t.remark = :remark")})
public class TblCorrespondentBank implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SWIFT_MESSAGE_SEQ")
    @SequenceGenerator(name = "SWIFT_MESSAGE_SEQ", sequenceName = "SWIFT_MESSAGE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
   // @NotNull
    @Column(name = "CREATED_BY")
    private String createdBy;
   // @Size(max = 1000)
    @Column(name = "NAME")
    private String name;
   // @Size(max = 200)
    @Column(name = "CODE")
    private String code;
   // @Size(max = 200)
    @Column(name = "REMARK")
    private String remark;

    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;
    @Column(name = "CURRENCY")
    private String currency;

    public TblCorrespondentBank() {
    }

    public TblCorrespondentBank(BigDecimal id) {
        this.id = id;
    }

   
    

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof TblCorrespondentBank)) {
            return false;
        }
        TblCorrespondentBank other = (TblCorrespondentBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TblCorrespondentBank[ id=" + id + " ]";
    }
    
}
