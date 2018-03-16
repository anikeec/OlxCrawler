/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.mainlogic;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.repository.AdvertRepository;
import com.apu.olxcrawler.repository.ORM.AdvertRepositoryHB;
import com.apu.olxcrawler.repository.ORM.HibernateUtil;
import com.apu.olxcrawler.repository.ORM.PhoneNameRepositoryHB;
import com.apu.olxcrawler.repository.ORM.PhoneNumberRepositoryHB;
import com.apu.olxcrawler.repository.ORM.UserNameRepositoryHB;
import com.apu.olxcrawler.repository.ORM.UserRepositoryHB;
import com.apu.olxcrawler.repository.PhoneNameRepository;
import com.apu.olxcrawler.repository.PhoneNumberRepository;
import com.apu.olxcrawler.repository.UserNameRepository;
import com.apu.olxcrawler.repository.UserRepository;
import com.apu.olxcrawler.repository.entity.Advert;
import com.apu.olxcrawler.repository.entity.PhoneName;
import com.apu.olxcrawler.repository.entity.PhoneNumber;
import com.apu.olxcrawler.repository.entity.User;
import com.apu.olxcrawler.repository.entity.UserName;
import java.math.BigInteger;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class AnAdvertKeeper {    
    
    /*
        a1 - get NAME from AnAdvert
        a2 - chech if it exists in DB
        a3  - if it doesn't exist -> create and persist its to DB, load usernameId
        a4  - if it exists -> load usernameId
        
        b1 - get PHONE from AnAdvert
        b2 - chech if it exists in DB
        b3  - if it doesn't exist -> create and persist its to DB, load phoneId
        b4  - if it exists -> load phoneId
        
        c1 - get PHONE&NAME from AnAdvert
        c2 - chech if this combination exists in DB
        c3  - if it doesn't exist -> a1, b1, create PHONENAME, fill date and persist its to DB, load phonenameId
        c4  - if it exists -> load phonenameId
        
        d1 - get user ID from AnAdvert
        d2 - chech if he exist in DB
        d3  - if he doesn't exist -> create and persist him to DB, load userId
        d4  - if he exists -> load userId
        
        e1 - check if phonenameId (c3) exists in user's phonenameCollection
        e2  - if it doesn't exist -> add its to user's phonenameCollection
        
        f1 - get phonenameId from c1
        f2 - get userId from d1
        f3 - get other info from AnAdvert
        f4 - get advert ID from AnAdvert
        f5 - check if advert with this ID exists in DB
        f6  - if it doesn't exist -> create and persist it to DB
        f7  - if it exists -> check other info and change it if there is a need,
                              check does phonenameId equal to last phonenameId from advert's phonenameCollection
                              if it is not equal then add phonenameId to advert's phonenameCollection

        Process:   
        c1 -> d1 -> e1 -> f1
        
    */   
    
    public void keepAnAdvert(AnAdvert advert) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
//            session = sessionFactory.getCurrentSession();
            session = sessionFactory.openSession();
            UserNameRepository uNameRepository =
                    new UserNameRepositoryHB(session);
            PhoneNumberRepository pNumberRepository =
                    new PhoneNumberRepositoryHB(session);
            PhoneNameRepository pNameRepository =
                    new PhoneNameRepositoryHB(session);
            AdvertRepository advertRepository =
                    new AdvertRepositoryHB(session);
            UserRepository userRepository =
                    new UserRepositoryHB(session);
            
            session.beginTransaction();
            
            //c1 - get PHONE&NAME from AnAdvert
            String nameStr = advert.getAuthor();
            String phoneStr = advert.getPhone();
            
            //c2 - chech if this combination exists in DB
            PhoneName phoneName = pNameRepository.get(nameStr, phoneStr);
            if(phoneName == null) {
                //c3  - if it doesn't exist -> a1, b1, create PHONENAME, fill date and persist its to DB, load phonenameId
                //a1 - get NAME from AnAdvert
                //a2 - chech if it exists in DB
                UserName uName = uNameRepository.get(nameStr);
                Integer uNameId;
                if(uName == null) {
                    //a3  - if it doesn't exist -> create and persist its to DB, load usernameId
                    uName = new UserName();
                    uName.setName(nameStr);
                    uNameId = uNameRepository.save(uName);
                }
                
                //b1 - get PHONE from AnAdvert
                //b2 - chech if it exists in DB
                PhoneNumber pNumber = pNumberRepository.get(phoneStr);
                Integer pNumberId;
                if(pNumber == null) {
                    //b3  - if it doesn't exist -> create and persist its to DB, load phoneId
                    pNumber = new PhoneNumber();
                    pNumber.setNumber(phoneStr);
                    pNumberId = pNumberRepository.save(pNumber);
                }
                
                phoneName = new PhoneName();
                phoneName.setUserName(uName);
                phoneName.setPhoneNumber(pNumber);
                phoneName.setDate(new Date());
                Integer phoneNameId = pNameRepository.save(phoneName);
            }
            
            String userId = advert.getUserId();
            //d1 - get user ID from AnAdvert
            //d2 - chech if he exist in DB
            User usr = userRepository.get(userId);
            if(usr == null) {
                //d3  - if he doesn't exist -> create and persist him to DB, load userId
                usr = new User();
                usr.setId(userId);
                Integer usrId = userRepository.save(usr);
            }
            
            //e1 - check if phonenameId (c3) exists in user's phonenameCollection
            if(usr.getPhoneNameCollection().contains(phoneName) == false) {
                //e2  - if it doesn't exist -> add its to user's phonenameCollection
                usr.getPhoneNameCollection().add(phoneName);
            }
            
            //f1 - get phonenameId from c1
            //f2 - get userId from d1
            //f3 - get other info from AnAdvert
            //f4 - get advert ID from AnAdvert
            String advertIdStr = advert.getId();
            //f5 - check if advert with this ID exists in DB
            Advert adv = advertRepository.get(new BigInteger(advertIdStr));
            if(adv == null) {
                //f6  - if it doesn't exist -> create and persist it to DB
                adv = new Advert();
                adv.setId(new BigInteger(advertIdStr));
                adv.setUser(usr);
                Long advId = advertRepository.save(adv);
                adv.getPhoneNameCollection().add(phoneName);
            } else {
                /* f7  - if it exists -> check other info and change it if there is a need,
                check does phonenameId equal to last phonenameId from advert's phonenameCollection
                if it is not equal then add phonenameId to advert's phonenameCollection
                */
                if(adv.getPhoneNameCollection().contains(phoneName) == false)
                    adv.getPhoneNameCollection().add(phoneName);
            }
            
            session.flush();
            session.getTransaction().commit();
        } finally {
            if ((session!=null) && (!session.isConnected())) 
                    session.close();
        }
    }
    
    public static void main(String[] args) {
        
        AnAdvert anAdvert = new AnAdvert();
        anAdvert.setAuthor("Pavel");
        anAdvert.setPhone("0501234567");
        anAdvert.setUserId("x1x2x3");
        anAdvert.setId("1234567890");
        new AnAdvertKeeper().keepAnAdvert(anAdvert);
    }
    
}