create sequence payment_txn_id_seq start with 1 increment by 50;

create table payment_transactions
(
    id              bigint default nextval('payment_txn_id_seq') not null,
    transaction_id  text                                         not null unique,
    created_at      timestamp                                    not null,
    status          text                                         not null,
    amount          numeric                                      not null,
    name_transactor text                                         not null,
    paypal_payment_id text,
    primary key (id)
);