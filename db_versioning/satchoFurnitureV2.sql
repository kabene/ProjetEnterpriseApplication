DROP SCHEMA IF EXISTS satchoFurniture CASCADE;

CREATE SCHEMA satchoFurniture;

CREATE TABLE satchoFurniture.addresses
(
    address_id      SERIAL PRIMARY KEY,
    street          varchar(50) NOT NULL,
    building_number varchar(10) NOT NULL,
    unit_number     varchar(10) NULL,
    postcode        varchar(10) NOT NULL,
    commune         varchar(50) NOT NULL,
    country         varchar(50) NOT NULL
);

CREATE TABLE satchoFurniture.users
(
    user_id                 SERIAL PRIMARY KEY,
    last_name               varchar(50)   NOT NULL,
    first_name              varchar(50)   NOT NULL,
    username                varchar(15)   NOT NULL,
    email                   varchar(15)   NOT NULL,
    address_id              integer       NOT NULL REFERENCES satchoFurniture.addresses (address_id),
    registration_date       timestamp     NOT NULL,
    role                    varchar(10)   NOT NULL,
    password                character(60) NOT NULL,
    purchased_furniture_nbr integer       NOT NULL DEFAULT 0,
    sold_furniture_nbr      integer       NOT NULL DEFAULT 0,
    waiting                 boolean       NOT NULL
);

CREATE TABLE satchofurniture.furniture_types
(
    type_id   SERIAL PRIMARY KEY,
    type_name varchar(50) NOT NULL
);

CREATE TABLE satchoFurniture.options(
    id_option SERIAL PRIMARY KEY,
    duree  integer NOT NULL,
    date_option timestamp NOT NULL,
    client_id integer NOT NULL REFERENCES satchoFurniture.users (user_id),
    furniture_id integer NOT NULL REFERENCES satchoFurniture.furniture (furniture_id),
    is_canceled boolean NOT NULL

);

--CREATE TYPE cond AS ENUM ('requested_for_visit', 'refused', 'accepted', 'in_restoration', 'available_for_sale', 'under_option', 'sold', 'reserved', 'delivered', 'collected', 'withdrawed');

CREATE TABLE satchofurniture.furniture
(
    furniture_id         SERIAL PRIMARY KEY,
    buyer_id             integer NULL REFERENCES satchofurniture.users(user_id),
    seller_id            integer      NOT NULL REFERENCES satchofurniture.users (user_id),
    condition            varchar(50)  NOT NULL,
    sale_withdrawal_date date NULL,
    description          varchar(200) NOT NULL,
    type_id              integer      NOT NULL REFERENCES satchofurniture.furniture_types (type_id),
    favourite_photo_id   integer NULL,
    selling_price        float NULL,
    special_sale_price   float NULL,
    date_of_sale         date NULL,
    is_to_pick_up        boolean NULL,
    pick_up_date         date NULL
);
CREATE TABLE satchoFurniture.options(
                                        id_option SERIAL PRIMARY KEY,
                                        duree  integer NOT NULL,
                                        date_option timestamp NOT NULL,
                                        client_id integer NOT NULL REFERENCES satchoFurniture.users (user_id),
                                        furniture_id integer NOT NULL REFERENCES satchoFurniture.furniture (furniture_id),
                                        is_canceled boolean NOT NULL

);

CREATE TABLE satchofurniture.photos
(
    photo_id        SERIAL PRIMARY KEY,
    furniture_id    integer      NOT NULL REFERENCES satchofurniture.furniture (furniture_id),
    is_on_home_page bool         NOT NULL,
    is_visible      bool         NOT NULL,
    source          varchar(200) NOT NULL
);

ALTER TABLE satchofurniture.furniture
    ADD FOREIGN KEY (favourite_photo_id) REFERENCES satchofurniture.photos (photo_id);



INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'sente des artistes', '1bis', 1 , '4800', 'Vervier', 'Belgique');

INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Albert', 'Satcho', 'bert', 'bert.satcho@gmail.be', 1, now(), 'admin',
        '$2y$04$Q8iu7vV9XiJCq4G3pAqd6eF63CvuwcM07sy3/FLk9iMoctjizRdl6', 0, 0, 'false');
INSERT
INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country)
VALUES (2, 'johndoe street', '4', '1', '1567', 'Beijing', 'Germany');

INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role,
                                   password, purchased_furniture_nbr, sold_furniture_nbr, waiting)
VALUES (2, 'doe', 'john', 'johndoe', 'john@gmail.com', 2, '02/02/2021', 'admin',
        '$2a$04$62XdSoqyDOBZWQCk/cuh1.OY/x3mnPi2wjcmDC0HCCzc7MVcj/VmW', 0, 0, false);



INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Armoire');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Bahut');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Bibliothèque');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Bonnetière');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Buffet');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Bureau');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Chaise');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Chiffonnier');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Coffre');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Coiffeuse');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Commode');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Confident/Indiscret');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Console');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Dresse');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Fauteil');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Guéridon');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Lingère');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Lit');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Penderie');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Secrétaire');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Table');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Tabouret');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Vaisselier');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('Valet muet');
INSERT INTO satchoFurniture.furniture_types (type_name)
VALUES ('...');


INSERT
INTO satchoFurniture.furniture (seller_id, condition, description, type_id, selling_price)
VALUES (1, 'available for sale', 'description', 23, 149.90);
