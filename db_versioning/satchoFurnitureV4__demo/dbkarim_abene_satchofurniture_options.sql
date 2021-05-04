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

