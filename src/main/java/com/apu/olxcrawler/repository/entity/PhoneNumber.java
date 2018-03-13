/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author apu
 */
@Entity
@Table(name = "phone_number")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhoneNumber.findAll", query = "SELECT p FROM PhoneNumber p")
    , @NamedQuery(name = "PhoneNumber.findByPhonenumberId", query = "SELECT p FROM PhoneNumber p WHERE p.phonenumberId = :phonenumberId")
    , @NamedQuery(name = "PhoneNumber.findByNumber", query = "SELECT p FROM PhoneNumber p WHERE p.number = :number")})
public class PhoneNumber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "phonenumber_id")
    private Integer phonenumberId;
    @Column(name = "number")
    private String number; 
    
    @OneToMany(mappedBy = "phoneNumber", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<PhoneName> phoneNameCollection;

    public PhoneNumber() {
    }

    public PhoneNumber(Integer phonenumberId) {
        this.phonenumberId = phonenumberId;
    }

    public Integer getPhonenumberId() {
        return phonenumberId;
    }

    public void setPhonenumberId(Integer phonenumberId) {
        this.phonenumberId = phonenumberId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        hash += (phonenumberId != null ? phonenumberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhoneNumber)) {
            return false;
        }
        PhoneNumber other = (PhoneNumber) object;
        if ((this.phonenumberId == null && other.phonenumberId != null) || (this.phonenumberId != null && !this.phonenumberId.equals(other.phonenumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.olxcrawler.repository.entity.PhoneNumber[ phonenumberId=" + phonenumberId + " ]";
    }
    
}
