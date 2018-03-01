/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.entity;

import java.util.Date;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class CookieItem {
    /**
     * Name.
     */
    private String name = null;

    /**
     * Value.
     */
    private String value = null;
    
    /** Comment attribute. */
    private String  cookieComment;

    /** Domain attribute. */
    private String  cookieDomain;

    /** Expiration {@link Date}. */
    private Date    cookieExpiryDate;

    /** Path attribute. */
    private String  cookiePath;

    /** My secure flag. */
    private boolean isSecure;

    /**
     * Specifies if the set-cookie header included a Path attribute for this
     * cookie
     */
    private boolean hasPathAttribute = false;

    /**
     * Specifies if the set-cookie header included a Domain attribute for this
     * cookie
     */
    private boolean hasDomainAttribute = false;

    /** The version of the cookie specification I was created from. */
    private int     cookieVersion = 0;

    public CookieItem(String name,
            String value,
            String cookieComment, 
            String cookieDomain, 
            Date cookieExpiryDate, 
            String cookiePath, 
            boolean isSecure, 
            boolean hasPathAttribute, 
            boolean hasDomainAttribute, 
            int cookieVersion) {
        this.name = name;
        this.value = value;
        this.cookieComment = cookieComment;
        this.cookieDomain = cookieDomain;
        this.cookieExpiryDate = cookieExpiryDate;
        this.cookiePath = cookiePath;
        this.isSecure = isSecure;
        this.hasPathAttribute = hasPathAttribute;
        this.hasDomainAttribute = hasDomainAttribute;
        this.cookieVersion = cookieVersion;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getCookieComment() {
        return cookieComment;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public Date getCookieExpiryDate() {
        return cookieExpiryDate;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public boolean isIsSecure() {
        return isSecure;
    }

    public boolean isHasPathAttribute() {
        return hasPathAttribute;
    }

    public boolean isHasDomainAttribute() {
        return hasDomainAttribute;
    }

    public int getCookieVersion() {
        return cookieVersion;
    }
    
    
}
