use olxdb;

/* info table of USER_NAME */
/* DROP TABLE USER_NAME; */
CREATE TABLE USER_NAME(
 username_id  INT NOT NULL AUTO_INCREMENT,
 name VARCHAR(30),
 PRIMARY KEY (username_id)
);

/* info table of PHONE_NUMBER */
/* DROP TABLE PHONE_NUMBER; */
CREATE TABLE PHONE_NUMBER(
 phonenumber_id  INT NOT NULL AUTO_INCREMENT,
 number VARCHAR(15),
 PRIMARY KEY (phonenumber_id)
);

/* table of PHONE_NAME */
/* DROP TABLE PHONE_NAME; */
CREATE TABLE PHONE_NAME(
 id  INT NOT NULL AUTO_INCREMENT,
 username_id  INT NOT NULL,
 phonenumber_id  INT NOT NULL,
 date DATE,
 PRIMARY KEY (id),
 FOREIGN KEY (username_id) REFERENCES USER_NAME (username_id),
 FOREIGN KEY (phonenumber_id) REFERENCES PHONE_NUMBER (phonenumber_id)
);

/* info table of USER */
/* DROP TABLE USER; */
CREATE TABLE USER(
 user_id  INT NOT NULL AUTO_INCREMENT,
 name VARCHAR(15),
 PRIMARY KEY (user_id)
);

/* table of PHONE_USER */
/* DROP TABLE PHONE_USER; */
CREATE TABLE PHONE_USER(
 id  INT NOT NULL AUTO_INCREMENT,
 user_id  INT NOT NULL,
 phonenumber_id  INT NOT NULL,
 date DATE,
 PRIMARY KEY (id),
 FOREIGN KEY (user_id) REFERENCES USER (user_id),
 FOREIGN KEY (phonenumber_id) REFERENCES PHONE_NUMBER (phonenumber_id)
);

/* info table of ADVERT */
/* DROP TABLE ADVERT; */
CREATE TABLE ADVERT(
 advert_id  BIGINT NOT NULL AUTO_INCREMENT,
 description TEXT,
 header varchar(50),
 id BIGINT,
 link varchar(200),
 price varchar(10),
 publication_date DATE,
 region varchar(50), 
 user_since DATE,
 PRIMARY KEY (advert_id)
);

/* table of USER_ADVERT */
/* DROP TABLE USER_ADVERT; */
CREATE TABLE USER_ADVERT(
 id  INT NOT NULL AUTO_INCREMENT,
 user_id  INT NOT NULL,
 advert_id  BIGINT NOT NULL,
 date DATE,
 PRIMARY KEY (id),
 FOREIGN KEY (user_id) REFERENCES USER (user_id),
 FOREIGN KEY (advert_id) REFERENCES ADVERT (advert_id)
);














