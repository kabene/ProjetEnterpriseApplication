create table addresses
(
    address_id      serial      not null
        constraint addresses_pkey
            primary key,
    street          varchar(50) not null,
    building_number varchar(10) not null,
    unit_number     varchar(10),
    postcode        varchar(10) not null,
    commune         varchar(50) not null,
    country         varchar(50) not null
);

alter table addresses
    owner to karim_abene;

INSERT INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country) VALUES (1, 'sente des artistes', '1bis', null, '4800', 'Vervier', 'Belgique');
INSERT INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country) VALUES (2, 'sente des artistes', '18', null, '4800', 'Vervier', 'Belgique');
INSERT INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country) VALUES (3, 'Rue de l’Eglise', '11', 'B1', '4987', 'Stoumont', 'Belgique');
INSERT INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country) VALUES (4, 'Rue de Renkin', '7', null, '4800', 'Vervier', 'Belgique');
INSERT INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country) VALUES (5, 'Lammerskreuzstrasse', '6', null, '52159', 'Roetgen', 'Allemagne');
INSERT INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country) VALUES (6, 'Rue des Minières', '45', 'Ter', '4800', 'Verviers', 'Belgique');
INSERT INTO satchofurniture.addresses (address_id, street, building_number, unit_number, postcode, commune, country) VALUES (7, 'Rue Victor Buillenne', '9', '4C', '4800', 'Verviers', 'Belgique');