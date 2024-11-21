create table if not exists users (
  id bigint generated by default as identity not null,
  name varchar(255) not null,
  email varchar(512) not null,
  constraint pk_user primary key (id),
  constraint uq_user_email unique (email)
);

create table if not exists item_requests (
  id bigint generated by default as identity not null,
  description varchar(255) not null,
  requestor_id int references users(id) on delete cascade,
  constraint pk_request primary key (id)
);

create table if not exists items (
  id bigint generated by default as identity not null,
  name varchar(255) not null,
  description varchar(255) not null,
  is_available bool,
  owner_id int references users(id) on delete cascade,
  request_id int references item_requests(id) on delete cascade,
  constraint pk_item primary key (id)
);

create table if not exists comments (
  id bigint generated by default as identity not null,
  text varchar(255) not null,
  item_id int references items(id) on delete cascade,
  author_id int references users(id) on delete cascade,
  constraint pk_comment primary key (id)
);

create table if not exists booking (
  id bigint generated by default as identity not null,
  start_booking timestamp without time zone,
  end_booking timestamp without time zone,
  item_id int references items(id) on delete cascade,
  booker_id int references users(id) on delete cascade,
  status varchar(50),
  constraint pk_booking primary key (id)
);