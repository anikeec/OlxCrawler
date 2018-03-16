/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.ORM;

import com.apu.olxcrawler.repository.PhoneNameRepository;
import com.apu.olxcrawler.repository.entity.PhoneName;
import com.apu.olxcrawler.repository.entity.PhoneNumber;
import com.apu.olxcrawler.repository.entity.UserName;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author apu
 */
public class PhoneNameRepositoryHB implements PhoneNameRepository {
    
    private final Session session;

    public PhoneNameRepositoryHB(Session session) {
        this.session = session;
    }
    
    @Override
    public PhoneName get(String nameStr, String phoneStr) {        
        Query query = session.getNamedQuery("PhoneName.findByPhoneAndName")
                                .setString("name", nameStr)
                                .setString("number", phoneStr);
        List<PhoneName> list = query.list();
        if(list.isEmpty())    return null;
        return list.get(0);
    }
    
    public PhoneName get(UserName userName, PhoneNumber phoneNumber) {
        return this.get(userName.getName(), phoneNumber.getNumber());
    }

    @Override
    public PhoneName getPhoneNameById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PhoneName getByPhoneNumber(PhoneNumber phoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PhoneName getByUserName(UserName userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(String phoneStr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer save(PhoneName phoneName) {
        PhoneName phoneNameTmp = 
                this.get(phoneName.getUserName(), phoneName.getPhoneNumber());
        if(phoneNameTmp == null) {
            session.persist(phoneName);
            return phoneName.getPhonenameId();
        } 
        return phoneNameTmp.getPhonenameId();
    }
    
}
