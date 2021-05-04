create table options
(
    option_id    serial    not null
        constraint options_pkey
            primary key,
    duration     integer   not null,
    date_option  timestamp not null,
    user_id      integer   not null
        constraint options_user_id_fkey
            references users,
    furniture_id integer   not null
        constraint options_furniture_id_fkey
            references furniture,
    is_canceled  boolean   not null
);

alter table options
    owner to karim_abene;

INSERT INTO satchofurniture.options (option_id, duration, date_option, user_id, furniture_id, is_canceled) VALUES (1, 5, '2021-05-04 09:14:25.882715', 3, 6, false);