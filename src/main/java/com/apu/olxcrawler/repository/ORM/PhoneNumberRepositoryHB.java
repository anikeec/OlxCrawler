/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.ORM;

import com.apu.olxcrawler.repository.PhoneNumberRepository;
import com.apu.olxcrawler.repository.entity.PhoneNumber;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author apu
 */
public class PhoneNumberRepositoryHB implements PhoneNumberRepository {
    
    private final Session session;

    public PhoneNumberRepositoryHB(Session session) {
        this.session = session;
    }
    
    @Override
    public PhoneNumber get(String phoneNumber) {
        Query query;
        if(phoneNumber == null) 
            query = session.getNamedQuery("PhoneNumber.findByNumberNull");
        else
            query = session.getNamedQuery("PhoneNumber.findByNumber")
                                        .setString("number", phoneNumber);
        List<PhoneNumber> list = query.list();
        if(list.isEmpty())    return null;
        return list.get(0);
    }

    @Override
    public PhoneNumber getPhoneById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getIdByPhoneNumber(String phoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer add(String phoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    @Override
    public Integer save(PhoneNumber phoneNumber) {
        PhoneNumber phoneNumberTmp = this.get(phoneNumber.getNumber());
        if(phoneNumberTmp == null) {
            session.persist(phoneNumber);
            return phoneNumber.getPhonenumberId();
        } 
        return phoneNumberTmp.getPhonenumberId();
    }
    
}
