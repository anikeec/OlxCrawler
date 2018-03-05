/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apu
 */
@Entity
@Table(name = "phone_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhoneUser.findAll", query = "SELECT p FROM PhoneUser p")
    , @NamedQuery(name = "PhoneUser.findById", query = "SELECT p FROM PhoneUser p WHERE p.id = :id")
    , @NamedQuery(name = "PhoneUser.findByDate", query = "SELECT p FROM PhoneUser p WHERE p.date = :date")})
public class PhoneUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "phonenumber_id", referencedColumnName = "phonenumber_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PhoneNumber phonenumberId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userId;

    public PhoneUser() {
    }

    public PhoneUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PhoneNumber getPhonenumberId() {
        return phonenumberId;
    }

    public void setPhonenumberId(PhoneNumber phonenumberId) {
        this.phonenumberId = phonenumberId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof PhoneUser)) {
            return false;
        }
        PhoneUser other = (PhoneUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.olxcrawler.repository.ORM.Entity.PhoneUser[ id=" + id + " ]";
    }
    
}
