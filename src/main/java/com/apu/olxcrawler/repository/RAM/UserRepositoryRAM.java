/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.UserRepository;
import com.apu.olxcrawler.repository.entity.UserEntity;

/**
 *
 * @author apu
 */
/*
 * the id is phoneNumber and there can be a lot of names linked with one phone number
 * the previous logic is incorrect. It will be better to use from Olx
*/
public class UserRepositoryRAM implements UserRepository {

    @Override
    public UserEntity getAdvertById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long add(UserEntity user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
