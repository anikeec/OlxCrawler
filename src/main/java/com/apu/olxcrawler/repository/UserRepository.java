/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.User;

/**
 *
 * @author apu
 */
public interface UserRepository {
    
    User get(String idStr);
    
    User getAdvertById(Long id);
    
    Integer save(User user);
    
}
