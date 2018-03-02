/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import java.util.Date;

/**
 *
 * @author apu
 */
public interface PhoneNameRepository {
    
    Integer getPhoneIdByNameId(Integer id);
    
    Integer getNameIdByPhoneId(Integer id);
    
    Integer add(Integer phoneId, Integer nameId, Date date);
    
}
