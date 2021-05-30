create table user
(
    id                 bigint primary key auto_increment,
    username           varchar(255),
    encrypted_password varchar(255),
    avatar             varchar(1000) default '_',
    created_at         datetime,
    updated_at         datetime
)