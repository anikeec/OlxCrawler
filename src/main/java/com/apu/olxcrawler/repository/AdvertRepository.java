/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository;

import com.apu.olxcrawler.repository.entity.Advert;
import java.math.BigInteger;

/**
 *
 * @author apu
 */
public interface AdvertRepository {
    
    Advert get(BigInteger id);
    
    Long save(Advert advert);
    
}
