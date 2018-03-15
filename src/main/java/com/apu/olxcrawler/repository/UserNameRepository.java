/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.UserName;

/**
 *
 * @author apu
 */
public interface UserNameRepository {
    
    UserName get(String name);
    
    UserName getNameById(Integer id);
    
    Integer getIdByNameValue(String name);
    
    Integer add(String name);
    
    Integer save(UserName userName);
    
}
