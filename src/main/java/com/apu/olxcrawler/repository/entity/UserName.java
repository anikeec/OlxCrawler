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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "user_name")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserName.findAll", query = "SELECT u FROM UserName u")
    , @NamedQuery(name = "UserName.findByUsernameId", query = "SELECT u FROM UserName u WHERE u.usernameId = :usernameId")
    , @NamedQuery(name = "UserName.findByName", query = "SELECT u FROM UserName u WHERE u.name = :name")
    , @NamedQuery(name = "UserName.findByNameNull", query = "SELECT u FROM UserName u WHERE u.name IS NULL")})
public class UserName implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "username_id")
    private Integer usernameId;
    @Column(name = "name")
    private String name;
    
    @OneToMany(mappedBy = "userName", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<PhoneName> phoneNameCollection;

    public UserName() {
    }

    public UserName(Integer usernameId) {
        this.usernameId = usernameId;
    }

    public Integer getUsernameId() {
        return usernameId;
    }

    public void setUsernameId(Integer usernameId) {
        this.usernameId = usernameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        hash += (usernameId != null ? usernameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserName)) {
            return false;
        }
        UserName other = (UserName) object;
        if ((this.usernameId == null && other.usernameId != null) || (this.usernameId != null && !this.usernameId.equals(other.usernameId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.olxcrawler.repository.entity.UserName[ usernameId=" + usernameId + " ]";
    }
    
}
