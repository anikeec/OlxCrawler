/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import java.io.IOException;

/**
 *
 * @author apu
 */
public class GetRequestException extends IOException {

    public GetRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
