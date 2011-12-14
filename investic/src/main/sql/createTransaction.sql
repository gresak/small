drop table if exists TRANSACTION;
drop table if exists PARTY;
drop table if exists FINANCIAL_INSTRUMENT;
create table PARTY
(
   id_party SERIAL,
   symbol VARCHAR(3),
   eff_from DATETIME,
   eff_to DATETIME,
   name VARCHAR(100),
   swift VARCHAR(20),
   bic VARCHAR(20),
   mic VARCHAR(20),
   other_id VARCHAR(20),
   is_counterparty INTEGER,
   is_broker INTEGER,
   is_bank INTEGER,
   is_custodian INTEGER,
   execution_place VARCHAR(100),
   note VARCHAR(1000),
   primary key (id_party)
);
create table FINANCIAL_INSTRUMENT
(
   id_financial_instrument SERIAL,
   eff_from DATETIME,
   eff_to DATETIME,
   name VARCHAR(100),
   short_name VARCHAR(12),
   isin VARCHAR(12),
   bloomberg_ticker VARCHAR(10),
   instrument_currency_code VARCHAR(3),
   primary key (id_financial_instrument)
);
create table TRANSACTION
(
   id_transaction SERIAL,
   id_financial_instrument bigint unsigned,
   reference_id varchar(30),
   id_portfolio bigint unsigned,
   transaction_datetime DATETIME,
   is_buy INTEGER,
   transaction_currency_code VARCHAR(3),
   unit_count decimal(18,4),
   unit_price decimal(18,4),
   transaction_volume decimal(18,4),
   accrued_interest decimal(18,4),
   third_party_fee decimal(18,4),
   administration_fee decimal(18,4),
   id_party_contra bigint unsigned,
   id_party_agent bigint unsigned,
   settlement_date DATETIME,
   instrument_settlement_date DATETIME,
   note VARCHAR(1000),
   primary key (id_transaction),
   foreign key (id_financial_instrument) references FINANCIAL_INSTRUMENT (id_financial_instrument),
   foreign key (id_portfolio) references PORTFOLIO (id_portfolio),
   foreign key (id_party_contra) references PARTY (id_party),
   foreign key (id_party_agent) references PARTY (id_party)
);
