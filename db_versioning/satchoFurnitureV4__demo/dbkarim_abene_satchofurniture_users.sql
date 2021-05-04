create table users
(
    user_id                 serial            not null
        constraint users_pkey
            primary key,
    last_name               varchar(50)       not null,
    first_name              varchar(50)       not null,
    username                varchar(50)       not null,
    email                   varchar(50)       not null,
    address_id              integer           not null
        constraint users_address_id_fkey
            references addresses,
    registration_date       timestamp         not null,
    role                    varchar(15)       not null,
    password                char(60)          not null,
    purchased_furniture_nbr integer default 0 not null,
    sold_furniture_nbr      integer default 0 not null,
    is_waiting              boolean           not null
);

alter table users
    owner to karim_abene;

INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role, password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting) VALUES (1, 'Satcho', 'Albert', 'bert', 'bert.satcho@gmail.be', 1, '2021-05-03 21:27:51.929685', 'admin', '$2a$04$WWrY8UJlV4bysHDwXVD3.uclmB5AT7oSb78bDd4/6Iq7aHqUrfVhi', 0, 0, false);
INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role, password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting) VALUES (2, 'Satcho', 'Laurent', 'lau', 'laurent.satcho@gmail.be', 2, '2021-05-03 21:27:52.617978', 'admin', '$2a$04$nppXs4TmjaX/NmV0PYdku.7HW4X9VM1K4KImzrCF8LnGYlQOJSQnu', 0, 0, false);
INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role, password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting) VALUES (3, 'Line', 'Caroline', 'caro', 'caro.line@hotmail.com', 3, '2021-05-03 21:27:53.214474', 'antique_dealer', '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 0, false);
INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role, password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting) VALUES (4, 'Ile', 'Achille', 'achil', 'ach.ile@gmail.com', 4, '2021-05-03 21:27:53.945707', 'customer', '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 0, false);
INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role, password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting) VALUES (5, 'Ile', 'Basile', 'bazz', 'bas.ile@gmail.be', 5, '2021-05-03 21:27:54.528224', 'customer', '$2a$04$rvF3zWgvRyeKz2qSBeud5uXfAcHDkMeJWsOez01YPomtSBBqPTmmO', 0, 0, false);
INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role, password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting) VALUES (6, 'Ile', 'Théophile', 'Theo', 'theo.phile@proximus.be', 4, '2021-05-03 21:27:54.801720', 'antique_dealer', '$2a$04$UHAY4jc17zGro8tm3aNd4u90ap/N4/ZIAl07qHCT1/Yd.RocMzvum', 0, 0, false);
INSERT INTO satchofurniture.users (user_id, last_name, first_name, username, email, address_id, registration_date, role, password, purchased_furniture_nbr, sold_furniture_nbr, is_waiting) VALUES (7, 'Line', 'Charles', 'Charline', 'charline@proximus.be', 6, '2021-05-03 21:27:55.482179', 'customer', '$2a$04$Jiu02H9pbvg6a83RNVVBXezBnxI2qtPRYzm4ZANRtZXywH0guqo9.', 0, 0, false);