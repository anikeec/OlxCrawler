/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.ORM;

import com.apu.olxcrawler.repository.AdvertRepository;
import com.apu.olxcrawler.repository.entity.Advert;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author apu
 */
public class AdvertRepositoryHB implements AdvertRepository {
    
    private final Session session;

    public AdvertRepositoryHB(Session session) {
        this.session = session;
    }

    @Override
    public Advert get(BigInteger id) {
        Query query = session.getNamedQuery("Advert.findById")
                                .setBigInteger("id", id);
        List<Advert> list = query.list();
        if(list.isEmpty())    return null;
        return list.get(0);
    }

    @Override
    public Long save(Advert advert) {
        Advert advertTmp = this.get(advert.getId());
        if(advertTmp == null) {
            session.persist(advert);
            return advert.getAdvertId();
        } 
        return advertTmp.getAdvertId();
    }
    
}
