use CurrencyConverter

create table COUNTRY(id char(3) primary key, country_name nvarchar(50) not null, abbreviation char(3) not null, currency char(3) not null, foreign key(currency) references CURRENCY(id))

create table CURRENCY(id char(3) primary key, currency_name nvarchar(50) not null, currency_symbol varbinary(max))

create table CONVERSION_HISTORY
(
	id int identity(1,1) primary key,
	from_value decimal(15,2) not null default 1,
	from_currency char(3) not null,
	to_value decimal(15,2) not null default 1,
	to_currency char(3) not null,
	conversion_date varchar(15),
	foreign key(from_currency) references CURRENCY(id),
	foreign key(to_currency) references CURRENCY(id)
)

drop table CONVERSION_HISTORY
	 
	
drop table COUNTRY
drop table CURRENCY
alter table CURRENCY alter column id char(10)

truncate table CONVERSION_HISTORY;

select * from currency
select * from country
select * from CONVERSION_HISTORY

delete from currency