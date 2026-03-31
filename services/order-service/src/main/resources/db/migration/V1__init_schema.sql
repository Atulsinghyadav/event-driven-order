create table if not exists orders (
  id uuid primary key,
  customer_id varchar(255) not null,
  total_amount numeric(19,2) not null,
  currency varchar(16) not null,
  status varchar(32) not null,
  created_at timestamp not null
);

create table if not exists order_items (
  id uuid primary key,
  order_id uuid not null references orders(id) on delete cascade,
  sku varchar(255) not null,
  quantity integer not null,
  unit_price numeric(19,2) not null
);

create table if not exists outbox_events (
  id uuid primary key,
  aggregate_type varchar(64) not null,
  aggregate_id uuid not null,
  type varchar(128) not null,
  payload text not null,
  status varchar(32) not null,
  created_at timestamp not null
);

create index if not exists idx_outbox_status_created_at
  on outbox_events(status, created_at);
