/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.entity.User;
import com.apu.olxcrawler.repository.UserRepository;

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
    public User get(String idStr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public User getAdvertById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer save(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
