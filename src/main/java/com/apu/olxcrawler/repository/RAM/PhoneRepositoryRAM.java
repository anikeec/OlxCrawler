/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.PhoneRepository;
import com.apu.olxcrawler.repository.entity.PhoneEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class PhoneRepositoryRAM implements PhoneRepository {
    
    private List<String> list = new ArrayList<>();

    @Override
    public PhoneEntity getPhoneById(Integer id) {
        if(id >= list.size()) 
                return null;
        return new PhoneEntity(id, list.get(id));
    }

    @Override
    public Integer getIdByPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) 
                return null;
        Integer id = list.indexOf(phoneNumber);
        if(id == -1)
                return null;
        else 
                return id;
    }

    @Override
    public Integer add(String phoneNumber) {
        Integer index = null;
        if(getIdByPhoneNumber(phoneNumber) == null) {
            index = list.size();
            list.add(index, phoneNumber);
        }
        return index;
    }
    
}
