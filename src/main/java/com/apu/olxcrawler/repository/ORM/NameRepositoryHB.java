/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.ORM;

import com.apu.olxcrawler.repository.NameRepository;
import com.apu.olxcrawler.repository.entity.UserName;
import org.hibernate.Session;

/**
 *
 * @author apu
 */
public class NameRepositoryHB implements NameRepository {

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
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        UserName un = new UserName();
        un.setName(name);
        session.persist(un);
        session.flush();
        session.getTransaction().commit();
        return un.getUsernameId();
    }
    
    public static void main(String[] args) {
        NameRepository rep = new NameRepositoryHB();
        System.out.println(rep.add("Test"));
        System.out.println(rep.add("Test2"));
    }
    
}