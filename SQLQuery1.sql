use CurrencyConverter

create table COUNTRY(id char(3) primary key, country_name nvarchar(50) not null, abbreviation char(3) not null, currency char(3) not null, foreign key(currency) references CURRENCY(id))

create table CURRENCY(id char(3) primary key, currency_name nvarchar(50) not null, currency_symbol varbinary(max))

drop table COUNTRY
drop table CURRENCY
alter table CURRENCY alter column id char(10)

select * from currency
select * from country