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
@Table(name = "TBL_COUNTRY_OF_ORIGIN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCountryOfOrigin.findAll", query = "SELECT t FROM TblCountryOfOrigin t")
    , @NamedQuery(name = "TblCountryOfOrigin.findById", query = "SELECT t FROM TblCountryOfOrigin t WHERE t.id = :id")
    , @NamedQuery(name = "TblCountryOfOrigin.findByCountry", query = "SELECT t FROM TblCountryOfOrigin t WHERE t.country = :country")
    , @NamedQuery(name = "TblCountryOfOrigin.findByCreatedDate", query = "SELECT t FROM TblCountryOfOrigin t WHERE t.createdDate = :createdDate")
    , @NamedQuery(name = "TblCountryOfOrigin.findByCreatedBy", query = "SELECT t FROM TblCountryOfOrigin t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TblCountryOfOrigin.findByCode", query = "SELECT t FROM TblCountryOfOrigin t WHERE t.code = :code")})
public class TblCountryOfOrigin implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SWIFT_MESSAGE_SEQ")
    @SequenceGenerator(name = "SWIFT_MESSAGE_SEQ", sequenceName = "SWIFT_MESSAGE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private BigDecimal id;
   // @Size(max = 255)
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "CREATED_BY") 
    private String createdBy;
    //@Size(max = 200)
    @Column(name = "CODE")
    private String code;

    public TblCountryOfOrigin() {
    }

    public TblCountryOfOrigin(BigDecimal id) {
        this.id = id;
    }

    

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(object instanceof TblCountryOfOrigin)) {
            return false;
        }
        TblCountryOfOrigin other = (TblCountryOfOrigin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TblCountryOfOrigin[ id=" + id + " ]";
    }
    
}
