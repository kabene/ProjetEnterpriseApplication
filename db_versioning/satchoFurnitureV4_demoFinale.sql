DROP SCHEMA IF EXISTS satchoFurniture CASCADE;

CREATE SCHEMA satchoFurniture;

CREATE TABLE satchoFurniture.addresses
(
    address_id      SERIAL          PRIMARY KEY,
    street          varchar(50)     NOT NULL,
    building_number varchar(10)     NOT NULL,
    unit_number     varchar(10)     NULL,
    postcode        varchar(10)     NOT NULL,
    commune         varchar(50)     NOT NULL,
    country         varchar(50)     NOT NULL
);

CREATE TABLE satchoFurniture.users
(
    user_id                 SERIAL        PRIMARY KEY,
    last_name               varchar(50)   NOT NULL,
    first_name              varchar(50)   NOT NULL,
    username                varchar(50)   NOT NULL,
    email                   varchar(50)   NOT NULL,
    address_id              integer       NOT NULL REFERENCES satchoFurniture.addresses (address_id),
    registration_date       timestamp     NOT NULL,
    role                    varchar(15)   NOT NULL,
    password                character(60) NOT NULL,
    purchased_furniture_nbr integer       NOT NULL DEFAULT 0,
    sold_furniture_nbr      integer       NOT NULL DEFAULT 0,
    is_waiting                 boolean       NOT NULL
);

CREATE TABLE satchofurniture.furniture_types
(
    type_id   SERIAL        PRIMARY KEY,
    type_name varchar(50)   NOT NULL
);

CREATE TABLE satchofurniture.requests_for_visit
(
    request_id          SERIAL          PRIMARY KEY,
    request_date        date            NOT NULL,
    time_slot           varchar(50)     NOT NULL,
    address_id          integer         NOT NULL REFERENCES satchoFurniture.addresses (address_id),
    visit_date_time     timestamp       NULL,
    explanatory_note    varchar(200)    NULL,
    status              varchar(50)     NOT NULL,
    user_id             integer         NOT NULL REFERENCES satchoFurniture.users (user_id)
);

CREATE TABLE satchofurniture.furniture
(
    furniture_id         SERIAL         PRIMARY KEY,
    buyer_id             integer        NULL REFERENCES satchofurniture.users(user_id),
    seller_id            integer        NOT NULL REFERENCES satchofurniture.users (user_id),
    status               varchar(50)    NOT NULL,
    sale_withdrawal_date date           NULL,
    description          varchar(200)   NOT NULL,
    type_id              integer        NOT NULL REFERENCES satchofurniture.furniture_types (type_id),
    favourite_photo_id   integer        NULL,
    selling_price        float          NULL,
    special_sale_price   float          NULL,
    date_of_sale         date           NULL,
    is_to_pick_up        boolean        NULL,
    pick_up_date         date           NULL,
    request_id           integer        NULL REFERENCES satchoFurniture.requests_for_visit (request_id),
    purchase_price       float          NULL,
    customer_withdrawal_date    date    NULL,
    deposit_date         date           NULL,
    suitable             boolean        NULL,
    available_for_sale   boolean        NULL
);

CREATE TABLE satchoFurniture.options(
                                        option_id       SERIAL      PRIMARY KEY,
                                        duration        integer     NOT NULL,
                                        date_option     timestamp   NOT NULL,
                                        user_id         integer     NOT NULL REFERENCES satchoFurniture.users (user_id),
                                        furniture_id    integer     NOT NULL REFERENCES satchoFurniture.furniture (furniture_id),
                                        is_canceled     boolean     NOT NULL
);

CREATE TABLE satchofurniture.photos
(
    photo_id        SERIAL       PRIMARY KEY,
    furniture_id    integer      NOT NULL REFERENCES satchofurniture.furniture (furniture_id),
    is_on_home_page boolean      NOT NULL,
    is_visible      boolean      NOT NULL,
    source          text         NOT NULL,
    is_from_request boolean      NULL
);

ALTER TABLE satchofurniture.furniture
    ADD FOREIGN KEY (favourite_photo_id) REFERENCES satchofurniture.photos (photo_id);

SET datestyle = dmy;



-- >>>>>>>>>>> INSERT's Before Demo 02/02/2021 <<<<<<<<<<<<<<


-- 1. Les 24 types de meubles donn??s dans l?????nonc??.
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (1, 'Armoire');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (2,'Bahut');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (3,'Biblioth??que');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (4,'Bonneti??re');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (5,'Buffet');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (6,'Bureau');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (7,'Chaise');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (8,'Chiffonnier');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (9,'Coffre');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (10,'Coiffeuse');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (11,'Commode');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (12,'Confident/Indiscret');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (13,'Console');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (14,'Dresse');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (15,'Fauteil');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (16,'Gu??ridon');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (17,'Ling??re');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (18,'Lit');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (19,'Penderie');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (20,'Secr??taire');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (21,'Table');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (22,'Tabouret');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (23,'Vaisselier');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (24,'Valet muet');


-- 2. Les utilisateurs, dont le r??le est patron et/ou neveu, inscription le 22/03/2021 :

INSERT
INTO satchoFurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country)
VALUES (DEFAULT, 'a', 'a', NULL , '23', 'a', 'a');
INSERT
INTO satchoFurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role,
                            password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting)
VALUES (DEFAULT, 'none', 'none', 'in-store purchase', 'none', 1, now(), 'customer',
        'none', 0, 0, 'false');


INSERT
INTO satchoFurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country)
VALUES (DEFAULT, 'sente des artistes', '1bis', NULL , '4800', 'Vervier', 'Belgique');
INSERT
INTO satchoFurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role,
                            password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting)
VALUES (DEFAULT, 'Satcho', 'Albert', 'bert', 'bert.satcho@gmail.be', 2, now(), 'admin',
        '$2a$04$WWrY8UJlV4bysHDwXVD3.uclmB5AT7oSb78bDd4/6Iq7aHqUrfVhi', 0, 0, 'false');

INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'sente des artistes', '18', NULL , '4800', 'Vervier', 'Belgique');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Satcho', 'Laurent', 'lau', 'laurent.satcho@gmail.be', 3, now(), 'admin',
        '$2a$04$nppXs4TmjaX/NmV0PYdku.7HW4X9VM1K4KImzrCF8LnGYlQOJSQnu', 0, 0, 'false');


-- 3. Les utilisateurs, clients de Mr Satcho, inscription le 23/03/2021, mot de passe : mdpusr.2 :
INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Rue de l???Eglise', '11', 'B1' , '4987', 'Stoumont', 'Belgique');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Line', 'Caroline', 'Caro', 'caro.line@hotmail.com', 4, now(), 'antique_dealer',
        '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 5, 'false');

INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Rue de Renkin', '7', NULL , '4800', 'Vervier', 'Belgique');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Ile', 'Achille', 'achil', 'ach.ile@gmail.com', 5, now(), 'customer',
        '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 2, 'false');

INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Lammerskreuzstrasse', '6', NULL , '52159', 'Roetgen', 'Allemagne');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Ile', 'Basile', 'bazz', 'bas.ile@gmail.be', 6, now(), 'customer',
        '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 2, 'false');

INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Ile', 'Th??ophile', 'Theo', 'theo.phile@proximus.be', 5 , now(), 'antique_dealer',
        '$2a$04$UHAY4jc17zGro8tm3aNd4u90ap/N4/ZIAl07qHCT1/Yd.RocMzvum', 0, 0, 'false');

INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Rue des Mini??res', '45', 'Ter' , '4800', 'Verviers', 'Belgique');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Line', 'Charles', 'charline', 'charline@proximus.be', 7 , now(), 'customer',
        '$2a$04$Jiu02H9pbvg6a83RNVVBXezBnxI2qtPRYzm4ZANRtZXywH0guqo9.', 0, 0, 'false');


-- 4. Les demandes de visite, dont vous trouverez les meubles ?? la page suivante.

INSERT
INTO satchoFurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id)
VALUES (DEFAULT, '24-03-21', 'lundi de 18h ?? 22h', 5, '29-03-21 20:00:00', NULL, 'confirmed', 5);

INSERT
INTO satchoFurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id)
VALUES (DEFAULT, '25-03-21', 'lundi de 18h ?? 22h', 5, NULL, 'Meuble trop r??cent', 'canceled', 5);

INSERT
INTO satchoFurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id)
VALUES (DEFAULT, '25-03-21', 'tous les jours de 15h ?? 18h', 6, '29-03-21 15:00:00', NULL, 'confirmed', 6);



INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Rue Victor Bouillenne', '9', '4C' , '4800', 'Verviers', 'Belgique');

INSERT
INTO satchoFurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id)
VALUES (DEFAULT, '21-04-21', 'tous les matins 9h ?? 13h',8,NULL, NULL, 'waiting', 7);

INSERT
INTO satchoFurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id)
VALUES (DEFAULT, '22-04-21', 'tous les matins 16h ?? 19h',4,'26-04-21 18:00:00', NULL, 'confirmed', 4);


INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT, 5, 'accepted', 'Bahut profond d???une largeur de 112 cm et d???une hauteur de 147 cm.', 2,null,null,1);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT, 5, 'available_for_sale', 'Large bureau 1m87 cm, deux colonnes de tiroirs', 6,null,299.00,1);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT, 5, 'refused', 'Table jardin en bois brut', 21,null,null,2);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT, 6, 'withdrawn', 'Table en ch??ne, pieds en fer forg??', 21,null,459.00,3);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT, 6, 'accepted', 'Secr??taire en acajou, marqueterie', 20,null,null,3);


INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT,7, 'requested_for_visit', 'Lit ?? baldaquin en acajou', 18,null,null,4);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT,4, 'in_restoration', 'Bureau en bois cir??',6,null,null,5);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT,4, 'available_for_sale', 'Bureau en ch??ne massif, sous-main int??gr??',6,null,378.00,5);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT,4, 'available_for_sale', 'Magnifique bureau en acajou',6,null,239.00,5);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT,4, 'available_for_sale', 'Splendide coiffeuse aux reliefs travaill??s',10,null,199.00,5);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id,favourite_photo_id,selling_price,request_id)
VALUES (DEFAULT,4, 'available_for_sale', 'Coiffeuse marqueterie',10,null,199.00,5);
