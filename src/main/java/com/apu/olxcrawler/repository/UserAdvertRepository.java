/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.Advert;
import com.apu.olxcrawler.repository.entity.User;

/**
 *
 * @author apu
 */
public interface UserAdvertRepository {
    
    Advert getAdvertByUser(User user);
    
    User getUserByAdvert(Advert advert);
    
    Integer add(User user, Advert advert);
    
}
