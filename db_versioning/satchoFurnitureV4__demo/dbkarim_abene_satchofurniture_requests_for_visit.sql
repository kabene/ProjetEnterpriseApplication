create table requests_for_visit
(
    request_id       serial      not null
        constraint requests_for_visit_pkey
            primary key,
    request_date     date        not null,
    time_slot        varchar(50) not null,
    address_id       integer     not null
        constraint requests_for_visit_address_id_fkey
            references addresses,
    visit_date_time  timestamp,
    explanatory_note varchar(200),
    status           varchar(50) not null,
    user_id          integer     not null
        constraint requests_for_visit_user_id_fkey
            references users
);

alter table requests_for_visit
    owner to karim_abene;

INSERT INTO satchofurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id) VALUES (1, '2021-03-24', 'lundi de 18h à 22h', 4, '2021-03-29 20:00:00.000000', null, 'confirmed', 4);
INSERT INTO satchofurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id) VALUES (2, '2021-03-25', 'lundi de 18h à 22h', 4, null, 'Meuble trop récent', 'canceled', 4);
INSERT INTO satchofurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id) VALUES (3, '2021-03-25', 'tous les jours de 15h à 18h', 5, '2021-03-29 15:00:00.000000', null, 'confirmed', 5);
INSERT INTO satchofurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id) VALUES (4, '2021-04-21', 'tous les matins 9h à 13h', 7, null, null, 'waiting', 6);
INSERT INTO satchofurniture.requests_for_visit (request_id, request_date, time_slot, address_id, visit_date_time, explanatory_note, status, user_id) VALUES (5, '2021-04-22', 'tous les matins 16h à 19h', 3, '2021-04-26 18:00:00.000000', null, 'confirmed', 3);