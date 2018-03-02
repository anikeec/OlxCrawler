/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.AdvertEntity;
import com.apu.olxcrawler.repository.entity.UserEntity;

/**
 *
 * @author apu
 */
public interface UserAdvertRepository {
    
    AdvertEntity getAdvertByUser(UserEntity user);
    
    UserEntity getUserByAdvert(AdvertEntity advert);
    
    Integer add(UserEntity user, AdvertEntity advert);
    
}
