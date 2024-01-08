CREATE TABLE persons (
  id bigserial,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  constraint persons_pk primary key (id)
);
