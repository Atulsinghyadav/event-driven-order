alter table outbox_events
  add column if not exists attempt_count integer not null default 0;

alter table outbox_events
  add column if not exists last_error text;

alter table outbox_events
  add column if not exists next_retry_at timestamp;

alter table outbox_events
  add column if not exists published_at timestamp;

create index if not exists idx_outbox_status_next_retry
  on outbox_events(status, next_retry_at);

create index if not exists idx_outbox_status_created
  on outbox_events(status, created_at);
