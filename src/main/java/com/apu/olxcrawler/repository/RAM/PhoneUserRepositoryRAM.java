/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.entity.PhoneUser;
import com.apu.olxcrawler.repository.entity.User;
import com.apu.olxcrawler.repository.PhoneUserRepository;

/**
 *
 * @author apu
 */
public class PhoneUserRepositoryRAM implements PhoneUserRepository {

    @Override
    public PhoneUser getPhoneByUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUserByPhone(PhoneUser phone) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(PhoneUser phone, User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
