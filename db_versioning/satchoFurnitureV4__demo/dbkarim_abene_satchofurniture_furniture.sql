create table furniture
(
    furniture_id             serial       not null
        constraint furniture_pkey
            primary key,
    buyer_id                 integer
        constraint furniture_buyer_id_fkey
            references users,
    seller_id                integer      not null
        constraint furniture_seller_id_fkey
            references users,
    status                   varchar(50)  not null,
    sale_withdrawal_date     date,
    description              varchar(200) not null,
    type_id                  integer      not null
        constraint furniture_type_id_fkey
            references furniture_types,
    favourite_photo_id       integer
        constraint furniture_favourite_photo_id_fkey
            references photos,
    selling_price            double precision,
    special_sale_price       double precision,
    date_of_sale             date,
    is_to_pick_up            boolean,
    pick_up_date             date,
    request_id               integer
        constraint furniture_request_id_fkey
            references requests_for_visit,
    purchase_price           double precision,
    customer_withdrawal_date date,
    deposit_date             date,
    suitable                 boolean,
    available_for_sale       boolean
);

alter table furniture
    owner to karim_abene;

INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (4, null, 5, 'withdrawn', null, 'Table en chêne, pieds en fer forgé', 21, 5, 459, null, null, null, null, 3, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (8, null, 5, 'available_for_sale', null, 'Bureau en chêne massif, sous-main intégré', 6, 11, 378, null, null, null, null, 5, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (1, null, 4, 'accepted', null, 'Bahut profond d’une largeur de 112 cm et d’une hauteur de 147 cm.', 2, 1, null, null, null, null, null, 1, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (7, null, 4, 'in_restoration', null, 'Bureau en bois ciré', 6, 8, null, null, null, null, null, 5, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (3, null, 4, 'refused', null, 'Table jardin en bois brut', 21, 4, null, null, null, null, null, 2, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (9, null, 5, 'available_for_sale', null, 'Magnifique bureau en acajou', 6, 13, 239, null, null, null, null, 5, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (5, null, 5, 'accepted', null, 'Secrétaire en acajou, marqueterie', 20, 6, null, null, null, null, null, 3, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (11, null, 5, 'available_for_sale', null, 'Splendide coiffeuse aux reliefs travaillés', 10, 17, 199, null, null, null, null, 5, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (6, null, 5, 'under_option', null, 'Lit à baldaquin en acajou', 18, 7, null, null, null, null, null, 4, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (2, null, 4, 'available_for_sale', null, 'Large bureau 1m87 cm, deux colonnes de tiroirs', 6, 3, 299, null, null, null, null, 1, null, null, null, null, null);
INSERT INTO satchofurniture.furniture (furniture_id, buyer_id, seller_id, status, sale_withdrawal_date, description, type_id, favourite_photo_id, selling_price, special_sale_price, date_of_sale, is_to_pick_up, pick_up_date, request_id, purchase_price, customer_withdrawal_date, deposit_date, suitable, available_for_sale) VALUES (10, null, 5, 'available_for_sale', null, 'Splendide coiffeuse aux reliefs travaillés', 10, 15, 199, null, null, null, null, 5, null, null, null, null, null);