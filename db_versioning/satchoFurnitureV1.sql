--@author GERARD Thibaut

DROP SCHEMA IF EXISTS satchoFurniture CASCADE;

CREATE SCHEMA satchoFurniture;

CREATE TABLE satchoFurniture.addresses(
    address_id SERIAL PRIMARY KEY,
    street varchar(50) NOT NULL,
    building_number varchar(10) NOT NULL,
    unit_number varchar(10) NOT NULL,
    postcode integer NOT NULL,
    commune varchar(50) NOT NULL,
    country varchar(50) NOT NULL
);

CREATE TABLE satchoFurniture.users(
	user_id SERIAL PRIMARY KEY,
	last_name varchar(50) NOT NULL,
	first_name varchar(50) NOT NULL,
    username varchar(15) NOT NULL,
    email varchar(15) NOT NULL,
    id_address integer NOT NULL REFERENCES satchoFurniture.addresses(address_id),
    registration_date timestamp NOT NULL,
    role varchar(10) NOT NULL,
    password character(60) NOT NULL,
    purchased_furniture_nbr integer NOT NULL DEFAULT 0,
    solded_furniture_nbr integer NOT NULL DEFAULT 0,
    waiting boolean NOT NULL
);