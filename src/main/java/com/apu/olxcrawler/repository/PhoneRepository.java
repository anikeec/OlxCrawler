/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.PhoneNumber;

/**
 *
 * @author apu
 */
public interface PhoneRepository {
    
    PhoneNumber getPhoneById(Integer id);
    
    Integer getIdByPhoneNumber(String phoneNumber);
    
    Integer add(String phoneNumber);
    
}