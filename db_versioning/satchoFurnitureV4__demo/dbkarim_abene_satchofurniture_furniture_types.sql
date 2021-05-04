create table furniture_types
(
    type_id   serial      not null
        constraint furniture_types_pkey
            primary key,
    type_name varchar(50) not null
);

alter table furniture_types
    owner to karim_abene;

INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (1, 'Armoire');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (2, 'Bahut');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (3, 'Bibliothèque');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (4, 'Bonnetière');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (5, 'Buffet');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (6, 'Bureau');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (7, 'Chaise');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (8, 'Chiffonnier');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (9, 'Coffre');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (10, 'Coiffeuse');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (11, 'Commode');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (12, 'Confident/Indiscret');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (13, 'Console');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (14, 'Dresse');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (15, 'Fauteil');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (16, 'Guéridon');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (17, 'Lingère');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (18, 'Lit');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (19, 'Penderie');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (20, 'Secrétaire');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (21, 'Table');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (22, 'Tabouret');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (23, 'Vaisselier');
INSERT INTO satchofurniture.furniture_types (type_id, type_name) VALUES (24, 'Valet muet');