 create table if not exists processed_events (
    event_key varchar(255) primary key,
    consumer_name varchar(128) not null,
    processed_at timestamp not null
 );

 create table if not exists inventory_reservations (
    id uuid primary key,
    order_id uuid not null,
    sku varchar(255) not null,
    quantity integer not null,
    status varchar(255) not null,
    created_at timestamp not null
 );

 create index if not exists idx_inventory_order_id
    on inventory_reservations(order_id);