/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.PhoneName;
import com.apu.olxcrawler.repository.entity.PhoneNumber;
import com.apu.olxcrawler.repository.entity.UserName;

/**
 *
 * @author apu
 */
public interface PhoneNameRepository {
    
    PhoneName get(String nameStr, String phoneStr);
    
    PhoneName getPhoneNameById(Integer id);
    
    PhoneName getByPhoneNumber(PhoneNumber phoneNumber);
    
    PhoneName getByUserName(UserName userName);
    
    Integer add(String phoneStr);
    
    Integer save(PhoneName phoneName);
    
}
