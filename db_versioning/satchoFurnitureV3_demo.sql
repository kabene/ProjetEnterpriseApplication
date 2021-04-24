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
    source          text         NOT NULL
);

ALTER TABLE satchofurniture.furniture
    ADD FOREIGN KEY (favourite_photo_id) REFERENCES satchofurniture.photos (photo_id);

SET datestyle = dmy;



-- >>>>>>>>>>> INSERT's Before Demo 02/02/2021 <<<<<<<<<<<<<<


-- 1. Les 24 types de meubles donnés dans l’énoncé.
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (1, 'Armoire');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (2,'Bahut');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (3,'Bibliothèque');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (4,'Bonnetière');
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
VALUES (16,'Guéridon');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (17,'Lingère');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (18,'Lit');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (19,'Penderie');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (20,'Secrétaire');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (21,'Table');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (22,'Tabouret');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (23,'Vaisselier');
INSERT INTO satchoFurniture.furniture_types (type_id, type_name)
VALUES (24,'Valet muet');


-- 2. Les utilisateurs, dont le rôle est patron et/ou neveu, inscription le 22/03/2021 :
INSERT
INTO satchoFurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country)
VALUES (DEFAULT, 'sente des artistes', '1bis', NULL , '4800', 'Vervier', 'Belgique');
INSERT
INTO satchoFurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role,
                            password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting)
VALUES (DEFAULT, 'Satcho', 'Albert', 'bert', 'bert.satcho@gmail.be', 1, now(), 'admin',
        '$2a$04$WWrY8UJlV4bysHDwXVD3.uclmB5AT7oSb78bDd4/6Iq7aHqUrfVhi', 0, 0, 'false');

INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'sente des artistes', '18', NULL , '4800', 'Vervier', 'Belgique');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Satcho', 'Laurent', 'lau', 'laurent.satcho@gmail.be', 2, now(), 'admin',
        '$2a$04$nppXs4TmjaX/NmV0PYdku.7HW4X9VM1K4KImzrCF8LnGYlQOJSQnu', 0, 0, 'false');


-- 3. Les utilisateurs, clients de Mr Satcho, inscription le 23/03/2021, mot de passe : mdpusr.2 :
INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Rue de l’Eglise', '11', 'B1' , '4987', 'Stoumont', 'Belgique');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Line', 'Caroline', 'caro', 'caro.line@hotmail.com', 3, now(), 'antique_dealer',
        '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 0, 'false');

INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Rue de Renkin', '7', NULL , '4800', 'Vervier', 'Belgique');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Ile', 'Achille', 'achil', 'ach.ile@gmail.com', 4, now(), 'customer',
        '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 0, 'false');

INSERT INTO satchoFurniture.addresses
VALUES (DEFAULT, 'Lammerskreuzstrasse', '6', NULL , '52159', 'Roetgen', 'Allemagne');
INSERT INTO satchoFurniture.users
VALUES (DEFAULT, 'Ile', 'Basile', 'bazz', 'bas.ile@gmail.be', 5, now(), 'customer',
        '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 0, 'false');


-- 4. Les demandes de visite, dont vous trouverez les meubles à la page suivante.

INSERT
INTO satchoFurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id)
VALUES (1, '24-03-21', 'lundi de 18h à 22h', 4, '29-03-21 20:00:00', NULL, 'confirmed', 4);

INSERT
INTO satchoFurniture.requests_for_visit
VALUES (2, '25-03-21', 'lundi de 18h à 22h', 4, NULL, 'Meuble trop récent', 'canceled', 4);

INSERT
INTO satchoFurniture.requests_for_visit
VALUES (3, '25-03-21', 'tous les jours de 15h à 18h', 5, '29-03-21 15:00:00', NULL, 'confirmed', 5);


INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id)
VALUES (1, 4, 'accepted', 'Bahut profond d’une largeur de 112 cm et d’une hauteur de 147 cm.', 2);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id)
VALUES (2, 4, 'accepted', 'Large bureau 1m87 cm, deux colonnes de tiroirs', 6);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id)
VALUES (3, 4, 'refused', 'Table jardin en bois brut', 21);


INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id)
VALUES (4, 5, 'accepted', 'Table en chêne, pieds en fer forgé', 21);

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id)
VALUES (5, 5, 'accepted', 'Secrétaire en acajou, marqueterie', 20);

INSERT
INTO satchoFurniture.photos
VALUES (DEFAULT, 4, true, true, 'Table.jpg');

INSERT
INTO satchoFurniture.furniture (furniture_id, seller_id, status, description, type_id)
VALUES (5, 5, 'accepted', 'Secrétaire en acajou, marqueterie', 20);
INSERT
INTO satchoFurniture.photos
VALUES (DEFAULT, 5, true, true, 'Secretaire.png');


INSERT
INTO satchoFurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id)
VALUES (1, '24-03-21', 'lundi de 18h à 22h', 4, '29-03-21 20:00:00', NULL, 'confirmed', 4);

INSERT
INTO satchoFurniture.requests_for_visit
VALUES (2, '25-03-21', 'lundi de 18h à 22h', 4, NULL, 'Meuble trop récent', 'canceled', 4);

INSERT
INTO satchoFurniture.requests_for_visit
VALUES (3, '25-03-21', 'tous les jours de 15h à 18h', 5, '29-03-21 15:00:00', NULL, 'confirmed', 5);