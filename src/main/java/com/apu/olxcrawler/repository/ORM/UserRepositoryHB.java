/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.ORM;

import com.apu.olxcrawler.repository.UserRepository;
import com.apu.olxcrawler.repository.entity.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author apu
 */
public class UserRepositoryHB implements UserRepository {
    
    private final Session session;

    public UserRepositoryHB(Session session) {
        this.session = session;
    }
    
    @Override
    public User get(String idStr) {
        Query query;
        if(idStr == null)
            query = session.getNamedQuery("User.findByIdNull");
        else
            query = session.getNamedQuery("User.findById")
                                        .setString("id", idStr);
        List<User> list = query.list();
        if(list.isEmpty())    return null;
        return list.get(0);
    }

    @Override
    public User getAdvertById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer save(User user) {
        User userTmp = this.get(user.getId());
        if(userTmp == null) {
            session.persist(user);
            return user.getUserId();
        } 
        return userTmp.getUserId();
    }
    
}
