/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.entity.UserName;
import java.util.ArrayList;
import java.util.List;
import com.apu.olxcrawler.repository.UserNameRepository;

/**
 *
 * @author apu
 */
public class UserNameRepositoryRAM implements UserNameRepository {
    
    private List<String> list = new ArrayList<>();

    @Override
    public UserName getNameById(Integer id) {
        if(id >= list.size()) 
                return null;
        UserName item = new UserName();
        item.setName(list.get(id));
        return item;
    }

    @Override
    public Integer getIdByNameValue(String name) {
        if(name == null) 
                return null;
        Integer id = list.indexOf(name);
        if(id == -1)
                return null;
        else 
                return id;
    }

    @Override
    public Integer add(String name) {
        Integer index = null;
        if(getIdByNameValue(name) == null) {
            index = list.size();
            list.add(index, name);
        }
        return index;
    }

    @Override
    public UserName get(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer save(UserName userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
