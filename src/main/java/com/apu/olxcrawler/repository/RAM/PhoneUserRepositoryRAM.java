/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.PhoneUserRepository;
import com.apu.olxcrawler.repository.entity.PhoneEntity;
import com.apu.olxcrawler.repository.entity.UserEntity;

/**
 *
 * @author apu
 */
public class PhoneUserRepositoryRAM implements PhoneUserRepository {

    @Override
    public PhoneEntity getPhoneByUser(UserEntity user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserEntity getUserByPhone(PhoneEntity phone) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(PhoneEntity phone, UserEntity user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
