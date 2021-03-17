--@author AGRO lucas, GERARD Thibaut

DROP SCHEMA IF EXISTS satchoFurniture CASCADE;

CREATE SCHEMA satchoFurniture;

CREATE TABLE satchoFurniture.addresses (
                                           address_id SERIAL PRIMARY KEY,
                                           street varchar(50) NOT NULL,
                                           buildingNumber varchar(10) NOT NULL,
                                           unitNumber varchar(10) NOT NULL,
                                           postcode integer NOT NULL,
                                           commune varchar(50) NOT NULL,
                                           country varchar(50) NOT NULL
);

CREATE TABLE satchoFurniture.users(
                                      user_id SERIAL PRIMARY KEY,
                                      lastName varchar(50) NOT NULL,
                                      firstName varchar(50) NOT NULL,
                                      username varchar(15) NOT NULL,
                                      email varchar(15) NOT NULL,
                                      address_id integer NOT NULL REFERENCES satchoFurniture.addresses(address_id),
                                      registration_date timestamp NOT NULL,
                                      role varchar(10) NOT NULL,
                                      password character(60) NOT NULL,
                                      purchased_furniture_nbr integer NOT NULL DEFAULT 0,
                                      solded_furniture_nbr integer NOT NULL DEFAULT 0,
                                      waiting boolean NOT NULL
);

CREATE TABLE satchofurniture.furniture_types (
                                                 type_id SERIAL PRIMARY KEY,
                                                 type_name varchar(50) NOT NULL
);

--CREATE TYPE cond AS ENUM ('requested_for_visit', 'refused', 'accepted', 'in_restoration', 'available_for_sale', 'under_option', 'sold', 'reserved', 'delivered', 'collected', 'withdrawed');

CREATE TABLE satchofurniture.furniture (
                                           furniture_id SERIAL PRIMARY KEY,
                                           buyer_id integer NULL REFERENCES satchofurniture.users(user_id),
                                           seller_id integer NOT NULL REFERENCES satchofurniture.users(user_id),
                                           condition cond NOT NULL,
                                           sale_withdrawal_date date NOT NULL,
                                           description varchar(200) NOT NULL,
                                           type_id integer NOT NULL REFERENCES satchofurniture.furniture_types(type_id),
                                           favourite_photo_id integer NULL,
                                           selling_price float NULL,
                                           special_sale_price float NULL,
                                           date_of_sale date NULL,
                                           is_to_pick_up boolean NULL,
                                           pick_up_date date NULL
);

CREATE TABLE satchofurniture.photos (
                                        photo_id SERIAL PRIMARY KEY,
                                        furniture_id integer NOT NULL REFERENCES satchofurniture.furniture(furniture_id),
                                        is_on_home_page bool NOT NULL,
                                        is_visible bool NOT NULL,
                                        source varchar(200) NOT NULL
);

ALTER TABLE satchofurniture.furniture ADD FOREIGN KEY (favourite_photo_id) REFERENCES satchofurniture.photos(photo_id);


INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'ez', 1, 1, 1, 'a', 'a');

INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'a', 'a', 'ex', 'a@gmail.com', 1, now(), 'customer',
        '$2a$04$yS2WqRPYnf2Tb7GjbiSBSeLcKDa1ExJXzvUQpPOcJUGdJkdzbJhnC', 0, 1, 'true')