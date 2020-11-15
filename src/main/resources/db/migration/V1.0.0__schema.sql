create table t_sys_role
(
  id     integer      not null constraint t_sys_role_pkey primary key,
  name   varchar(255) not null unique,
  system boolean      not null default false
);

alter table t_sys_role
  owner to symbols;

create table t_sys_user
(
  id        integer not null constraint t_sys_user_pkey primary key,
  email     varchar(127),
  gender    integer,
  name      varchar(16),
  nick_name varchar(16),
  password  varchar(255),
  c_time    timestamp,
  u_time    timestamp
);

alter table t_sys_user
  owner to symbols;

create table t_sys_user_role
(
  user_id integer not null references t_sys_user,
  role_id integer not null references t_sys_role,
  constraint t_sys_user_role_pkey primary key (user_id, role_id)
);

alter table t_sys_user_role
  owner to symbols;

create table if not exists t_example_enum
(
  id   integer      not null constraint t_example_enum_pkey primary key,
  name varchar(127) not null unique
);

alter table t_example_enum
  owner to symbols;


