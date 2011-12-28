drop table if exists TRANSACTION;
drop table if exists CASH_TRANSACTION;
drop table if exists EXECUTION_PLACE;
drop table if exists CASH_TRANSACTION_PURPOSE;
drop table if exists INVESTMENT_SERVICE;
drop table if exists CLAIM_DEBT;
drop table if exists CLAIM_DEBT_TYPE;
drop table if exists PARTY;
drop table if exists ACCOUNT;
drop table if exists ACCOUNT_TYPE;
drop table if exists FINANCIAL_INSTRUMENT;
drop table if exists PORTFOLIO;
drop table if exists MANAGEMENT_TYPE;
drop table if exists AGREEMENT;
drop table if exists AGREEMENT_TYPE;
drop table if exists PORTFOLIO_STRATEGY;
drop table if exists CLIENT;
drop table if exists PERSON;
drop table if exists COMPANY;
drop table if exists ADDRESS;
create table ADDRESS
(
   id_address SERIAL,
   eff_from DATETIME,
   eff_to DATETIME,
   street VARCHAR(100),
   locality VARCHAR(50),
   postal_code VARCHAR(10),
   country_code VARCHAR(100),
   primary key (id_address)
);
create table COMPANY
(
   id_company SERIAL,
   eff_from DATETIME,
   eff_to DATETIME,
   name VARCHAR(100),
   ico VARCHAR(10),
   dic VARCHAR(10),
   id_address bigint unsigned,
   email VARCHAR(100),
   phone VARCHAR(25),
   primary key (id_company),
   foreign key (id_address) references ADDRESS (id_address)
);
create table PERSON
(
   id_person SERIAL,
   eff_from DATETIME,
   eff_to DATETIME,
   name VARCHAR(50),
   surname VARCHAR(50),
   salutation VARCHAR(50),
   title_before VARCHAR(50),
   title_after VARCHAR(50),
   birth_date DATETIME,
   birth_number VARCHAR(10),
   state_card_id VARCHAR(20),
   dic VARCHAR(10),
   id_address bigint unsigned,
   email VARCHAR(100),
   phone VARCHAR(25),
   primary key (id_person),
   foreign key (id_address) references ADDRESS (id_address)
);
create table CLIENT
(
   id_client SERIAL,
   symbol VARCHAR(3),
   eff_from DATETIME,
   eff_to DATETIME,
   id_address_coresponding bigint unsigned,
   id_address_billing bigint unsigned,
   id_company bigint unsigned,
   id_person bigint unsigned,
   is_domestic INTEGER,
   email VARCHAR(100),
   phone VARCHAR(25),
   primary key (id_client), 
   foreign key (id_address_coresponding) references ADDRESS (id_address),
   foreign key (id_address_billing) references ADDRESS (id_address),
   foreign key (id_company) references COMPANY (id_company),
   foreign key (id_person) references PERSON (id_person)
);
create table AGREEMENT_TYPE
(
   id_agreement_type SERIAL,
   name VARCHAR(50),
   primary key (id_agreement_type)   
);
INSERT INTO AGREEMENT_TYPE  (name) VALUES ('Agreement type 1');
INSERT INTO AGREEMENT_TYPE (name) VALUES ('Agreement type 2');
create table AGREEMENT
(
   id_agreement SERIAL,
   eff_from DATETIME,
   eff_to DATETIME,
   code VARCHAR(10),
   id_agreement_type bigint unsigned,
   id_client bigint unsigned,
   primary key (id_agreement),   
   foreign key (id_agreement_type) references AGREEMENT_TYPE (id_agreement_type),   
   foreign key (id_client) references CLIENT (id_client)
);
create table MANAGEMENT_TYPE
(
   id_management_type SERIAL,
   name VARCHAR(50),
   primary key (id_management_type)   
);
INSERT INTO MANAGEMENT_TYPE (name) VALUES ('Default portfolio type'); --created just as a container for client's transactions when client is managging his or her portfolio outside the investment company
INSERT INTO MANAGEMENT_TYPE (name) VALUES ('Managed as customer service');
INSERT INTO MANAGEMENT_TYPE (name) VALUES ('Customer managed');
create table PORTFOLIO_STRATEGY
(
   id_portfolio_strategy SERIAL,
   name VARCHAR(50),
   primary key (id_portfolio_strategy)   
);
INSERT INTO PORTFOLIO_STRATEGY  (name) VALUES ('Strategy 1');
INSERT INTO PORTFOLIO_STRATEGY (name) VALUES ('Strategy 2');
create table PORTFOLIO
(
   id_portfolio SERIAL,
   id_client bigint unsigned,
   symbol VARCHAR(10),
   eff_from DATETIME,
   eff_to DATETIME,
   id_management_type bigint unsigned,
   id_portfolio_strategy bigint unsigned,
   primary key (id_portfolio),   
   foreign key (id_client) references CLIENT (id_client),
   foreign key (id_management_type) REFERENCES MANAGEMENT_TYPE (id_management_type),
   foreign key (id_portfolio_strategy) REFERENCES PORTFOLIO_STRATEGY (id_portfolio_strategy)
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
create table ACCOUNT_TYPE
(
   id_account_type SERIAL,
   name VARCHAR(50),
   primary key (id_account_type)   
);
INSERT INTO ACCOUNT_TYPE  (name) VALUES ('Cash account');
INSERT INTO ACCOUNT_TYPE (name) VALUES ('Security account');
create table ACCOUNT
(
   id_account SERIAL,
   id_account_type bigint unsigned,
   id_financial_instrument bigint unsigned,
   id_portfolio bigint unsigned,
   symbol VARCHAR(10),
   unit_count decimal(18,4),
   primary key (id_account),
   foreign key (id_account_type) references ACCOUNT_TYPE (id_account_type),
   foreign key (id_financial_instrument) references FINANCIAL_INSTRUMENT (id_financial_instrument),
   foreign key (id_portfolio) references PORTFOLIO (id_portfolio)
);
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
   note VARCHAR(1000),
   primary key (id_party)
);
create table EXECUTION_PLACE
(
   id_execution_place SERIAL,
   name VARCHAR(50),
   primary key (id_execution_place)   
);
INSERT INTO EXECUTION_PLACE  (name) VALUES ('Stock Exchange');
INSERT INTO EXECUTION_PLACE (name) VALUES ('OTC');
create table TRANSACTION
(
   id_transaction SERIAL,
   id_financial_instrument bigint unsigned,
   reference_id varchar(30),
   id_account bigint unsigned,
   id_parent_transaction bigint unsigned,
   transaction_datetime DATETIME,
   is_buy INTEGER,
   transaction_currency_code VARCHAR(3),
   unit_count decimal(18,4),
   unit_price decimal(18,4),
   transaction_volume decimal(18,4),
   accrued_interest decimal(18,4),
   third_party_fee decimal(18,4),
   own_fee decimal(18,4),
   id_party_contra bigint unsigned,
   id_party_agent bigint unsigned,
   id_execution_place bigint unsigned,
   submitted_to VARCHAR(100),
   settlement_date DATETIME,
   instrument_settlement_date DATETIME,
   note VARCHAR(1000),
   primary key (id_transaction),
   foreign key (id_financial_instrument) references FINANCIAL_INSTRUMENT (id_financial_instrument),
   foreign key (id_account) references ACCOUNT (id_account),
   foreign key (id_parent_transaction) references TRANSACTION (id_transaction),
   foreign key (id_party_contra) references PARTY (id_party),
   foreign key (id_party_agent) references PARTY (id_party),
   foreign key (id_execution_place) references EXECUTION_PLACE (id_execution_place)
);
create table CLAIM_DEBT_TYPE
(
   id_claim_debt_type SERIAL,
   name VARCHAR(50),
   primary key (id_claim_debt_type)
);
create table CLAIM_DEBT
(
   id_claim_debt SERIAL,
   id_claim_debt_type bigint unsigned,
   id_account bigint unsigned,
   id_portfolio bigint unsigned,
   amount decimal(18,4),
   id_party_contra bigint unsigned,
   settlement_date DATETIME,
   note VARCHAR(1000),
   primary key (id_claim_debt),
   foreign key (id_claim_debt_type) references CLAIM_DEBT_TYPE (id_claim_debt_type),
   foreign key (id_account) references ACCOUNT (id_account),
   foreign key (id_party_contra) references PARTY (id_party),
   foreign key (id_portfolio) references PORTFOLIO (id_portfolio)
);
create table CASH_TRANSACTION_PURPOSE
(
   id_cash_transaction_purpose SERIAL,
   name VARCHAR(50),
   primary key (id_cash_transaction_purpose)   
);
INSERT INTO CASH_TRANSACTION_PURPOSE  (name) VALUES ('Purpose 1');
INSERT INTO CASH_TRANSACTION_PURPOSE (name) VALUES ('Purpose 2');
create table INVESTMENT_SERVICE
(
   id_investment_service SERIAL,
   name VARCHAR(50),
   primary key (id_investment_service)   
);
INSERT INTO INVESTMENT_SERVICE  (name) VALUES ('Investment service 1');
INSERT INTO INVESTMENT_SERVICE (name) VALUES ('Investment service 2');
create table CASH_TRANSACTION
(
   id_cash_transaction SERIAL,
   reference_id varchar(30),
   id_account bigint unsigned,
   specific_symbol VARCHAR(10),
   variable_symbol VARCHAR(10),
   constant_symbol VARCHAR(4),
   id_cash_transaction_purpose bigint unsigned,
   id_investment_service bigint unsigned,
   is_external_io INTEGER,
   id_parent_cash_transaction bigint unsigned,
   cash_transaction_datetime DATETIME,
   cash_transaction_currency_code VARCHAR(3),
   amount decimal(18,4),
   id_party_contra bigint unsigned,
   settlement_date DATETIME,
   note1 VARCHAR(1000),
   note2 VARCHAR(1000),
   primary key (id_cash_transaction),
   foreign key (id_account) references ACCOUNT (id_account),
   foreign key (id_cash_transaction_purpose) references CASH_TRANSACTION_PURPOSE (id_cash_transaction_purpose),
   foreign key (id_investment_service) references INVESTMENT_SERVICE (id_investment_service),
   foreign key (id_parent_cash_transaction) references CASH_TRANSACTION (id_cash_transaction),
   foreign key (id_party_contra) references PARTY (id_party)
);
