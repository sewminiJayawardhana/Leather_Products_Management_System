drop database if exists perfect;
create database if not exists perfect;

use perfect;
create table User(
                     username varchar(35)PRIMARY KEY ,
                     password varchar(25),
                     email varchar(55)not null

);

create table customer(
                         cus_id varchar(35) primary key ,
                         name varchar(155) not null,
                         address varchar(25) not null,
                         email varchar(45) not null
);

create table payment(
                        pay_id varchar(35)Primary key,
                        pay_amount double not null,
                        cus_id  varchar(35),
                        foreign key (cus_id) references customer(cus_id)on delete cascade on update cascade
);


create table orders(
                       order_id varchar(35) primary key,
                       cus_id varchar(35) not null,
                       date date not null,
                       foreign key (cus_id) references customer(cus_id)on delete cascade on update cascade
);

create table raw_material(
                             raw_id varchar(35)Primary key,
                             qty int(40)not null,
                             raw_unitprice double not null

);

create table products(
                         code varchar(35) primary key ,
                         description varchar (70)not null,
                         unit_price double not null,
                         qty_on_hand int(50)not null
);


create table order_detail(
                             order_id varchar(35) not null ,
                             code varchar(35) not null,
                             qty int not null,
                             unit_price double not null,
                             foreign key (order_id) references orders(order_id)on delete  cascade on update cascade,
                             foreign key (code) references products(code)
                                 on delete cascade on update cascade
);




create table supplier(
                         supplier_id varchar(35)Primary key,
                         supplier_name varchar(50) not null,
                         address varchar(100)not null ,
                         tel varchar(50)not null
);

create table supplier_details(
                                 supplier_id varchar(35) not null,
                                 raw_id varchar(35) not null,
                                 date date,
                                 foreign key (supplier_id) references supplier(supplier_id)on delete  cascade on update cascade,
                                 foreign key (raw_id) references raw_material(raw_id)on delete  cascade on update cascade
);


create table employee(
                         emp_id varchar(35)primary key,
                         emp_name varchar(40) not null,
                         in_time time,
                         leave_time time
);


create table salary(
                       s_id varchar(35)primary key,
                       emp_id varchar(35)not null ,
                       months varchar(35)not null ,
                       amount double not null,
                       foreign key (emp_id) references employee(emp_id)on delete  cascade on update cascade
);
create table delivery(
    delivery_id varchar(20)primary key ,
    order_id varchar(35)not null ,
    emp_id varchar(35)not null ,
    location varchar(55)not null ,
    delivery_status varchar(50)not null ,
    tel int(50)not null,
    foreign key (emp_id) references employee(emp_id)on delete  cascade on update cascade,
    foreign key (order_id) references orders(order_id)on delete  cascade on update cascade
);

create table sellsDetails as
    select p.code,
           p.description,
           p.unit_price,
           od.order_id,
           od.qty,
           od.qty* od.unit_price AS payments,
           o.date
FROM products p
join order_detail od on p.code = od.code
join orders o on od.order_id = o.order_id;


create table salary_details  as
SELECT
    s.s_id,
    e.emp_name,
    s.months,
    e.leave_time - e.in_time AS working_hours,
    (e.leave_time - e.in_time)*5000 as add_for_hours ,
    ((e.leave_time - e.in_time)*5000)+amount as salary
FROM employee e
         JOIN salary s
              ON e.emp_id = s.emp_id;







