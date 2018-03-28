/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author apu
 */
@Entity
@Table(name = "advert")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Advert.findAll", query = "SELECT a FROM Advert a")
    , @NamedQuery(name = "Advert.findByAdvertId", query = "SELECT a FROM Advert a WHERE a.advertId = :advertId")
    , @NamedQuery(name = "Advert.findByHeader", query = "SELECT a FROM Advert a WHERE a.header = :header")
    , @NamedQuery(name = "Advert.findById", query = "SELECT a FROM Advert a WHERE a.id = :id")
    , @NamedQuery(name = "Advert.findByLink", query = "SELECT a FROM Advert a WHERE a.link = :link")
    , @NamedQuery(name = "Advert.findByPrice", query = "SELECT a FROM Advert a WHERE a.price = :price")
    , @NamedQuery(name = "Advert.findByPublicationDate", query = "SELECT a FROM Advert a WHERE a.publicationDate = :publicationDate")
    , @NamedQuery(name = "Advert.findByRegion", query = "SELECT a FROM Advert a WHERE a.region = :region")
    , @NamedQuery(name = "Advert.findByUserSince", query = "SELECT a FROM Advert a WHERE a.userSince = :userSince")})
public class Advert implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "advert_id")
    private Long advertId;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "header")
    private String header;
    @Column(name = "id")
    private BigInteger id;
    @Column(name = "link")
    private String link;
    @Column(name = "price")
    private String price;
    @Column(name = "publication_date")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;
    @Column(name = "publication_time")
    @Temporal(TemporalType.TIME)
    private Date publicationTime;
    @Column(name = "region")
    private String region;
    @Column(name = "user_since")
    @Temporal(TemporalType.DATE)
    private Date userSince;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="PHONENAME_ADVERT",
            joinColumns = @JoinColumn(name = "advert_id", referencedColumnName = "advert_id"),
            inverseJoinColumns = @JoinColumn(name = "phonename_id", referencedColumnName = "phonename_id") 
    )  
    private Collection<PhoneName> phoneNameCollection = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Advert() {
    }

    public Advert(Long advertId) {
        this.advertId = advertId;
    }

    public Long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Long advertId) {
        this.advertId = advertId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(Date publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getUserSince() {
        return userSince;
    }

    public void setUserSince(Date userSince) {
        this.userSince = userSince;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public Collection<PhoneName> getPhoneNameCollection() {
        return phoneNameCollection;
    }

    public void setPhoneNameCollection(Collection<PhoneName> phoneNameCollection) {
        this.phoneNameCollection = phoneNameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (advertId != null ? advertId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Advert)) {
            return false;
        }
        Advert other = (Advert) object;
        if ((this.advertId == null && other.advertId != null) || (this.advertId != null && !this.advertId.equals(other.advertId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.olxcrawler.repository.entity.Advert[ advertId=" + advertId + " ]";
    }
    
}
