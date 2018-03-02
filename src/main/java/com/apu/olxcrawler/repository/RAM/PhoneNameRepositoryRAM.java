/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.PhoneNameRepository;
import com.apu.olxcrawler.repository.entity.NameEntity;
import com.apu.olxcrawler.repository.entity.PhoneEntity;

/**
 *
 * @author apu
 */
public class PhoneNameRepositoryRAM implements PhoneNameRepository {

    @Override
    public PhoneEntity getPhoneByName(NameEntity name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NameEntity getNameByPhone(PhoneEntity phone) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(PhoneEntity phone, NameEntity name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
