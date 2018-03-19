/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

import java.io.IOException;

/**
 *
 * @author apu
 */
public class IllegalInputValueException extends IOException {

    public IllegalInputValueException(String message) {
        super(message);
    }

    public IllegalInputValueException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
