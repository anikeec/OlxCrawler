/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.entity;

import java.util.Date;

/**
 *
 * @author apu
 */
public class UserAdvertEntity {
    private int id;
    private UserEntity user;
    private AdvertEntity advert;
    private Date date;

    public UserAdvertEntity(int id, UserEntity user, AdvertEntity advert, Date date) {
        this.id = id;
        this.user = user;
        this.advert = advert;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AdvertEntity getAdvert() {
        return advert;
    }

    public void setAdvert(AdvertEntity advert) {
        this.advert = advert;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
}
