/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.entity.Advert;
import com.apu.olxcrawler.repository.entity.User;
import com.apu.olxcrawler.repository.UserAdvertRepository;

/**
 *
 * @author apu
 */
public class UserAdvertRepositoryRAM implements UserAdvertRepository {

    @Override
    public Advert getAdvertByUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUserByAdvert(Advert advert) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(User user, Advert advert) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
