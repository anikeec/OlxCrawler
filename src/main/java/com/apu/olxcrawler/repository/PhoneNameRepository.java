/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.NameEntity;
import com.apu.olxcrawler.repository.entity.PhoneEntity;

/**
 *
 * @author apu
 */
public interface PhoneNameRepository {
    
    PhoneEntity getPhoneByName(NameEntity name);
    
    NameEntity getNameByPhone(PhoneEntity phone);
    
    Integer add(PhoneEntity phone, NameEntity name);
    
}
