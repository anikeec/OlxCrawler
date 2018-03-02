/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.PhoneNameRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author apu
 */
public class PhoneNameRepositoryRAM implements PhoneNameRepository {
    
    private final List<Integer> phoneIdList = new ArrayList<>();
    private final List<Integer> nameIdList = new ArrayList<>();
    private final List<Date> dateList = new ArrayList<>();

    @Override
    public Integer getPhoneIdByNameId(Integer nameId) {
        if(nameId == null)
                return null;
        Integer id = nameIdList.indexOf(nameId);
        if(id == -1)
                return null;
        Integer phoneId = phoneIdList.get(id);
        if(phoneId == -1)
                return null;
        else    
                return phoneId;
    }

    @Override
    public Integer getNameIdByPhoneId(Integer phoneId) {
        if(phoneId == null)
                return null;
        Integer id = nameIdList.indexOf(phoneId);
        if(id == -1)
                return null;
        Integer nameId = phoneIdList.get(id);
        if(nameId == -1)
                return null;
        else    
                return nameId;
    }

    @Override
    public Integer add(Integer phoneId, Integer nameId, Date date) {
        if((phoneId == null) || (nameId == null) || (date == null) ||
           (phoneId < 0) || (nameId < 0))
                return null;
        Integer index = null;
        for(Integer id:phoneIdList) {
            if((phoneId.intValue() == id) &&
               (nameId.intValue() == nameIdList.get(id))) {
                if(date.equals(dateList.get(id)) == false) {
                    dateList.set(id, date);
                }
                return id;
            }
        }        
        index = phoneIdList.size();
        phoneIdList.add(index, phoneId);
        nameIdList.add(index, nameId);
        dateList.add(index, date);
        return index;
    }
    
    
    
}
