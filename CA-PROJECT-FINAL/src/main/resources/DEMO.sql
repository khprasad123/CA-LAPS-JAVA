
CREATE SCHEMA `ca_project2` ;

use ca_project2;
drop table if exists role_leave_type;
drop table if exists leave_history_details;
drop table if exists leave_entitled;

drop table if exists employee;

drop table if exists leave_type;
drop table if exists public_hollyday;
 drop table if exists role;

 create table employee (emp_id integer not null auto_increment, email varchar(255) not null, full_name varchar(255) not null, password varchar(255) not null, user_name varchar(255) not null, reports_to integer, role_id integer, primary key (emp_id)) ;
 create table leave_entitled (leave_entitled_id integer not null auto_increment, remaining_leave integer not null, employee_id integer not null, leave_type_id integer not null, primary key (leave_entitled_id)) ;
create table leave_history_details (leave_history_id integer not null auto_increment, applying_reason varchar(255) not null, end_date date not null, rejection_reason varchar(255), start_date date not null, status integer not null, work_desemination varchar(255), emp_id integer, leave_type_id integer, primary key (leave_history_id)) ;
 create table leave_type (leave_type_id integer not null auto_increment, type varchar(255), primary key (leave_type_id)) ;
 create table public_hollyday (hollyday_id integer not null auto_increment, description varchar(255), hollyday_name varchar(255) not null, start_date date not null, primary key (hollyday_id)) ;
create table role (role_id integer not null auto_increment, role_name varchar(100), primary key (role_id)) ;
create table role_leave_type (role_leave_id integer not null auto_increment, no_of_days integer not null, leave_type_id integer not null, role_id integer, primary key (role_leave_id)) ;
alter table role add constraint UK_iubw515ff0ugtm28p8g3myt0h unique (role_name);
alter table employee add constraint FKghecv11ypswk5w7mmcof2dscg foreign key (reports_to) references employee (emp_id) on delete no action;
 alter table employee add constraint FK3046kvjyysq288vy3lsbtc9nw foreign key (role_id) references role (role_id);
 alter table leave_entitled add constraint FKqc3wb4pq044fmqp7tfvcs7k0q foreign key (employee_id) references employee (emp_id) ;
 alter table leave_entitled add constraint FKek3eqh31l3wwfiiwn2ptwpuho foreign key (leave_type_id) references leave_type (leave_type_id) ;
 alter table leave_history_details add constraint FKqmac42on7ad2s2v2cqc57fnql foreign key (emp_id) references employee (emp_id) ;
 alter table leave_history_details add constraint FKh4li8wnuqa9v5hvmculqb53pp foreign key (leave_type_id) references leave_type (leave_type_id) ;
 alter table role_leave_type add constraint FKqp007mrnfmbrx74xc3ok5cexc foreign key (leave_type_id) references leave_type (leave_type_id) ;
 alter table role_leave_type add constraint FKqx6r7w4cseubxoufk7nudc4ei foreign key (role_id) references role (role_id) ;