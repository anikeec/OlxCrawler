/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.PhoneEntity;
import com.apu.olxcrawler.repository.entity.UserEntity;

/**
 *
 * @author apu
 */
public interface PhoneUserRepository {
    
    PhoneEntity getPhoneByUser(UserEntity user);
    
    UserEntity getUserByPhone(PhoneEntity phone);
    
    Integer add(PhoneEntity phone, UserEntity user);
    
}
