/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.UserAdvertRepository;
import com.apu.olxcrawler.repository.entity.AdvertEntity;
import com.apu.olxcrawler.repository.entity.UserEntity;

/**
 *
 * @author apu
 */
public class UserAdvertRepositoryRAM implements UserAdvertRepository {

    @Override
    public AdvertEntity getAdvertByUser(UserEntity user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserEntity getUserByAdvert(AdvertEntity advert) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(UserEntity user, AdvertEntity advert) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
