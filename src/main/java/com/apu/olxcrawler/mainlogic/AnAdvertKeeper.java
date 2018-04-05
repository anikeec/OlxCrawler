/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.mainlogic;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.parseProcess.PhoneNumberQuery;
import com.apu.olxcrawler.parser.IllegalInputValueException;
import com.apu.olxcrawler.parser.OlxAnAdvertParser;
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
import com.apu.olxcrawler.utils.Log;
import com.apu.olxcrawler.utils.Time;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class AnAdvertKeeper {   
    
    private static final Log log = Log.getInstance();
    private final Class classname = AnAdvertKeeper.class;
    
    private UserNameRepository uNameRepository;
    private PhoneNumberRepository pNumberRepository;
    private PhoneNameRepository pNameRepository;
    private AdvertRepository advertRepository;
    private UserRepository userRepository;
    
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
    public boolean isLinkExistInDB(ExpandedLink link) {
        if(link == null) {
            log.error(classname, "Error in method isLinkExistInDB() - Input link is NULL.");
            return false;
        }
        PhoneNumberQuery returnQuery = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
//            session = sessionFactory.getCurrentSession();
            session = sessionFactory.openSession();
            
            repositoriesInit(session);
            
            session.beginTransaction();
            
            String userId = OlxAnAdvertParser.getUserIdFromLink(link.getLink());
            if(userId == null)
                return false;
            
            User user = getUserById(userId);
            if(user == null)
                return false;
            
            Collection<Advert> advertCollection = user.getAdvertCollection();
            
            for(Advert advert:advertCollection) {
                if(advert.getLink() == null)
                    continue;
                String advertLink = 
                        OlxAnAdvertParser.cleanLinkForCompare(advert.getLink());
                String inputLink = 
                        OlxAnAdvertParser.cleanLinkForCompare(link.getLink());
                if(advertLink.equals(inputLink)) {
                    log.info(classname, "Links compare is TRUE.");
                    //check is advert has phone
                    for(PhoneName phoneName:advert.getPhoneNameCollection()) {
                        String phoneNumber = 
                                phoneName.getPhoneNumber().getNumber();
                        if(phoneNumber != null)
                            return true;
                    }
                    log.info(classname, "Looking for not NULL phone is FALSE.");
                    return false;
                }
                log.info(classname, "Links compare is FALSE." + 
                        "Advert link is: " + advert.getLink() + 
                        " ,\r\n" +
                        "Input link is:  " + link.getLink());
            }
            
            session.flush();
            session.getTransaction().commit();
        } finally {
//            if ((session!=null) && (!session.isConnected()))
            if(session != null)
                    session.close();
            repositoriesClose();
        }
        return false;
    }
    

    public PhoneNumberQuery keepAnAdvert(AnAdvert advert) {
        PhoneNumberQuery returnQuery = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
//            session = sessionFactory.getCurrentSession();
            session = sessionFactory.openSession();
            
            repositoriesInit(session);
            
            session.beginTransaction();
            
            if((advert.getPhone() != null)&&(advert.getPhone().equals("") == false)) {
                //if it is only phone number:
                
                //get PHONE&NAME from AnAdvert                
                PhoneName phoneName = 
                    getPhonenameByNameAndPhone(advert.getAuthor(), advert.getPhone());             
                
                //check if advert with this ID exists in DB
                Advert adv = getAdvertById(advert.getId());
                
                if(adv != null) {
                    /* if it exists -> check other info and change it if there is a need,
                    check does phonenameId equal to last phonenameId from advert's phonenameCollection
                    if it is not equal then add phonenameId to advert's phonenameCollection
                    */
                    if(adv.getPhoneNameCollection().contains(phoneName) == false) {
                        List<PhoneName> removeList = new ArrayList<>();
                        for(PhoneName pn: adv.getPhoneNameCollection()) {
                            if(pn.getPhoneNumber().getNumber() == null) {                               
                                
                                if((pn.getUserName().getName() == null) && 
                                        (advert.getAuthor() == null)) {
                                    removeList.add(pn);
                                } else if((advert.getAuthor() != null) &&
                                        (pn.getUserName().getName() != null) &&
                                        (pn.getUserName().getName().equals(advert.getAuthor())))
                                    removeList.add(pn);
                                
                            }
                        }
                        if(removeList.size() > 0) {
                            adv.getPhoneNameCollection().removeAll(removeList);
                        }
                        adv.getPhoneNameCollection().add(phoneName);
                    }
                }               
                
            } else {
                
                //check if this advert exists
                Advert advTemp = getAdvertById(advert.getId());
                
                if(advTemp != null) {
                    if(advTemp.getPhoneNameCollection().isEmpty()) {
                        log.info(classname, "Advert doesn't have phone. ID: " + advTemp.getId());
                        returnQuery = new PhoneNumberQuery(advert, advert.getPreviousQueryResult());
                    }
                    session.flush();
                    session.getTransaction().commit();
                    return returnQuery;
                }           
            
                //get PHONE&NAME from AnAdvert
                PhoneName phoneName = 
                    getPhonenameByNameAndPhone(advert.getAuthor(), advert.getPhone());

                //chech if user exist in DB
                User usr = getUserById(advert.getUserId());

                //check if phonenameId (c3) exists in user's phonenameCollection
                if(usr.getPhoneNameCollection().contains(phoneName) == false) {
                    //if it doesn't exist -> add its to user's phonenameCollection
                    usr.getPhoneNameCollection().add(phoneName);
                }

                //check if advert with this ID exists in DB
                Advert adv = getAdvertById(advert.getId());
                BigInteger advertId = getAdvertIdById(advert.getId());
                
                if(adv == null) {
                    //if it doesn't exist -> create and persist it to DB
                    saveAdvert(adv, advertId, advert, usr, phoneName);
                } else {
                    /* if it exists -> check other info and change it if there is a need,
                    check does phonenameId equal to last phonenameId from advert's phonenameCollection
                    if it is not equal then add phonenameId to advert's phonenameCollection
                    */
                    updateAdvert(adv, advertId, advert, usr, phoneName);
                }

                returnQuery = new PhoneNumberQuery(advert, advert.getPreviousQueryResult());
            
            }
            session.flush();
            session.getTransaction().commit();
        } finally {
//            if ((session!=null) && (!session.isConnected()))
            if(session != null)
                    session.close();
            repositoriesClose();
        }
        return returnQuery;
    }
    
    private void saveAdvert(Advert adv, BigInteger advertId, AnAdvert advert, User usr, PhoneName phoneName) {
        if(advertId != null) {
            adv = new Advert();
            adv.setId(advertId);
            adv.setUser(usr);
            Long advId = advertRepository.save(adv);
            adv.getPhoneNameCollection().add(phoneName);
            adv.setDescription(advert.getDescription());
            adv.setHeader(advert.getHeader());
            adv.setLink(advert.getLink());
            adv.setPrice(advert.getPrice());
            adv.setPublicationDate(Time.timeStrToDate(advert.getPublicationDate()));
            adv.setPublicationTime(Time.timeStrToTime(advert.getPublicationDate()));
            adv.setRegion(advert.getRegion());
            adv.setUserSince(Time.timeStrToDate(advert.getUserSince()));
        }
    }
    
    private void updateAdvert(Advert adv, BigInteger advertId, AnAdvert advert, User usr, PhoneName phoneName) {
        if(advertId != null) {
            adv.setId(advertId);
            adv.setUser(usr);
            if(adv.getPhoneNameCollection().contains(phoneName) == false)
                adv.getPhoneNameCollection().add(phoneName);
            adv.setDescription(advert.getDescription());
            adv.setHeader(advert.getHeader());
            adv.setLink(advert.getLink());
            adv.setPrice(advert.getPrice());
            adv.setPublicationDate(Time.timeStrToDate(advert.getPublicationDate()));
            adv.setPublicationTime(Time.timeStrToTime(advert.getPublicationDate()));
            adv.setRegion(advert.getRegion());
            adv.setUserSince(Time.timeStrToDate(advert.getUserSince()));
        } else {
            log.info(classname, "advertId is NULL.");
        }
    }
    
    private PhoneName getPhonenameByNameAndPhone(String nameStr, String phoneStr) {
        //chech if this combination exists in DB
        PhoneName phoneName = pNameRepository.get(nameStr, phoneStr);
        if(phoneName == null) {
            //if it doesn't exist -> a1, b1, create PHONENAME, fill date and persist its to DB, load phonenameId
            //get NAME from AnAdvert
            //chech if it exists in DB
            UserName uName = uNameRepository.get(nameStr);
            Integer uNameId;
            if(uName == null) {
                //if it doesn't exist -> create and persist its to DB, load usernameId
                uName = new UserName();
                uName.setName(nameStr);
                uNameId = uNameRepository.save(uName);
            }

            //get PHONE from AnAdvert
            //chech if it exists in DB
            PhoneNumber pNumber = pNumberRepository.get(phoneStr);
            Integer pNumberId;
            if(pNumber == null) {
                //if it doesn't exist -> create and persist its to DB, load phoneId
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
        
        return phoneName;
    }
    
    private BigInteger getAdvertIdById(String advertIdStr) {
        if(advertIdStr == null) 
                return null;
        BigInteger advertId = null;
        try {
            advertId = new BigInteger(advertIdStr);
        } catch(NumberFormatException ex) {
            log.error(classname, "Error BigInteger from advertIdStr: " + advertIdStr);
        }        
        return advertId;
    }
    
    private Advert getAdvertById(String advertIdStr) {
        //check if advert with this ID exists in DB        
        BigInteger advertId = getAdvertIdById(advertIdStr);
        if(advertId == null)    
                return null;
        Advert adv = null;
        try {
            adv = advertRepository.get(advertId);
        } catch(NumberFormatException ex) {
            log.error(classname, "Error BigInteger from advertIdStr: " + advertIdStr);
        }        
        return adv;
    }
    
    private User getUserById(String userId) {
        //get user ID from AnAdvert
        //chech if he exist in DB
        User usr = userRepository.get(userId);
        if(usr == null) {
            //if he doesn't exist -> create and persist him to DB, load userId
            usr = new User();
            usr.setId(userId);
            Integer usrId = userRepository.save(usr);
        }
        
        return usr;
    }
    
    private void repositoriesInit(Session session) {
        this.uNameRepository = new UserNameRepositoryHB(session);
        this.pNumberRepository = new PhoneNumberRepositoryHB(session);
        this.pNameRepository = new PhoneNameRepositoryHB(session);
        this.advertRepository = new AdvertRepositoryHB(session);
        this.userRepository = new UserRepositoryHB(session);
    }
    
    private void repositoriesClose() {
        this.uNameRepository = null;
        this.pNumberRepository = null;
        this.pNameRepository = null;
        this.advertRepository = null;
        this.userRepository = null;
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
