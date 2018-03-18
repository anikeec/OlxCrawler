/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.ORM;

import com.apu.olxcrawler.repository.entity.Advert;
import com.apu.olxcrawler.repository.entity.PhoneName;
import com.apu.olxcrawler.repository.entity.PhoneNumber;
import com.apu.olxcrawler.repository.entity.User;
import com.apu.olxcrawler.repository.entity.UserName;
import org.hibernate.Session;
import com.apu.olxcrawler.repository.UserNameRepository;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author apu
 */
public class UserNameRepositoryHB implements UserNameRepository {
    
    private final Session session;

    public UserNameRepositoryHB(Session session) {
        this.session = session;
    }
    
    @Override
    public UserName get(String name) {
        Query query;
        if(name == null)
            query = session.getNamedQuery("UserName.findByNameNull");
        else
            query = session.getNamedQuery("UserName.findByName")
                                        .setString("name", name);
        List<UserName> list = query.list();
        if(list.isEmpty())    return null;
        return list.get(0);
    }

    @Override
    public UserName getNameById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getIdByNameValue(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer save(UserName userName) {
        UserName userNameTmp = this.get(userName.getName());
        if(userNameTmp == null) {
            session.persist(userName);
            return userName.getUsernameId();
        } 
        return userNameTmp.getUsernameId();
    }
 
}
