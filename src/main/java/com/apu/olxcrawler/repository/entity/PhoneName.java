/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "phone_name")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhoneName.findAll", query = "SELECT p FROM PhoneName p")
    , @NamedQuery(name = "PhoneName.findByPhonenumberId", query = "SELECT p FROM PhoneName p WHERE p.phonenameId = :phonenameId")
//    , @NamedQuery(name = "PhoneName.findByPhoneIdAndNameId", query = "SELECT p FROM PhoneName p "
//                                                                        + "INNER JOIN FETCH UserName un ON un.name = :name "
//                                                                        + "INNER JOIN FETCH PhoneNumber phn ON phn.number = :number")
//    , @NamedQuery(name = "PhoneName.findByPhoneIdAndNameId", query = "SELECT p FROM UserName un, "
//                                                                        + "IN(un.phoneNameCollection) p "
//                                                                        + "WHERE un.name = :name ")
//  select phonename_id FROM phone_name AS phn INNER JOIN user_name AS un ON un.name = 'Test' INNER JOIN phone_number AS pn ON pn.number = 'Phone Test'      
    , @NamedQuery(name = "PhoneName.findByPhoneAndName", query = "SELECT p FROM PhoneName p WHERE p.userName = "
                                                                        + "( SELECT un FROM UserName un WHERE un.name = :name ) "
                                                                        + "AND p.phoneNumber = "
                                                                        + "( SELECT phn FROM PhoneNumber phn WHERE phn.number = :number)")    
    })
public class PhoneName implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "phonename_id")
    private Integer phonenameId;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "phoneNameCollection", fetch = FetchType.LAZY)
    private Collection<Advert> advertCollection = new ArrayList<>();
    
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "phoneNameCollection", fetch = FetchType.LAZY)
    private Collection<User> userCollection = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phonenumber_id")
    private PhoneNumber phoneNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username_id")
    private UserName userName;
    

    public PhoneName() {
    }

    public PhoneName(Integer phonenameId) {
        this.phonenameId = phonenameId;
    }

    public Integer getPhonenameId() {
        return phonenameId;
    }

    public void setPhonenameId(Integer phonenameId) {
        this.phonenameId = phonenameId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserName getUserName() {
        return userName;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    @XmlTransient
    public Collection<Advert> getAdvertCollection() {
        return advertCollection;
    }

    public void setAdvertCollection(Collection<Advert> advertCollection) {
        this.advertCollection = advertCollection;
    }

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    @XmlTransient
    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (phonenameId != null ? phonenameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhoneName)) {
            return false;
        }
        PhoneName other = (PhoneName) object;
        if ((this.phonenameId == null && other.phonenameId != null) || (this.phonenameId != null && !this.phonenameId.equals(other.phonenameId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.olxcrawler.repository.entity.PhoneName[ phonenameId=" + phonenameId + " ]";
    }
    
}
