/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.entity;

/**
 *
 * @author apu
 */
public class AnAdvert {
    
    private String author;
    private String description;
    private String header;
    private String id;
    private String link;    
    private String phone;
    private String price;
    private String publicationDate;    
    private String region;
    private String userOffers;
    private String userSince;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
 
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getUserOffers() {
        return userOffers;
    }

    public void setUserOffers(String offers) {
        this.userOffers = offers;
    }

    public String getUserSince() {
        return userSince;
    }

    public void setUserSince(String userSince) {
        this.userSince = userSince;
    }
    
}
