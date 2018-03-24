use olxdb;

CREATE TABLE USER_NAME(
 username_id  INT NOT NULL AUTO_INCREMENT,
 name VARCHAR(30),
 PRIMARY KEY (username_id)
);

CREATE TABLE PHONE_NUMBER(
 phonenumber_id  INT NOT NULL AUTO_INCREMENT,
 number VARCHAR(15),
 PRIMARY KEY (phonenumber_id)
);

CREATE TABLE PHONE_NAME(
 phonename_id  INT NOT NULL AUTO_INCREMENT,
 username_id  INT NOT NULL,
 phonenumber_id  INT NOT NULL,
 date DATE,
 PRIMARY KEY (phonename_id),
 FOREIGN KEY (username_id) REFERENCES USER_NAME (username_id),
 FOREIGN KEY (phonenumber_id) REFERENCES PHONE_NUMBER (phonenumber_id)
);

CREATE TABLE ADVERT(
 advert_id  BIGINT NOT NULL AUTO_INCREMENT,
 id BIGINT,
 price varchar(20),
 publication_date DATE,
 publication_time TIME,
 user_id INT NOT NULL,
 region TEXT, 
 user_since DATE,
 header TEXT,
 description TEXT,
 link TEXT,
 PRIMARY KEY (advert_id)
);

CREATE TABLE USER(
 user_id  INT NOT NULL AUTO_INCREMENT,
 name VARCHAR(15),
 id VARCHAR(15),
 registration_date DATE,
 PRIMARY KEY (user_id)
);

ALTER TABLE ADVERT 
ADD FOREIGN KEY (user_id) REFERENCES USER (user_id);

CREATE TABLE PHONENAME_ADVERT(
 id  INT NOT NULL AUTO_INCREMENT,
 phonename_id  INT NOT NULL,
 advert_id  BIGINT NOT NULL,
 date DATE,
 PRIMARY KEY (id),
 FOREIGN KEY (phonename_id) REFERENCES PHONE_NAME (phonename_id),
 FOREIGN KEY (advert_id) REFERENCES ADVERT (advert_id)
);

CREATE TABLE PHONENAME_USER(
 id  INT NOT NULL AUTO_INCREMENT,
 phonename_id  INT NOT NULL,
 user_id  INT NOT NULL,
 date DATE,
 PRIMARY KEY (id),
 FOREIGN KEY (phonename_id) REFERENCES PHONE_NAME (phonename_id),
 FOREIGN KEY (user_id) REFERENCES USER (user_id)
);















