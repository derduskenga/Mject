---profile pics paths must be unique because pics might be placed in same folder. In the event user upload the same picture, it should be renamed
-- ALTER TABLE entitys
-- ADD UNIQUE(profile_pic_path)
--.............................................................
---every post may have very many comments, which makes it hard to defferentiate profile pic paths for post owner pic path and comment owner pic path
-- ALTER TABLE donation_request_comments
-- ADD comment_owner_pic_path varchar(150) references entitys(profile_pic_path)
--.................................................................................
---i experienced a problem in retrieving names of comment owner by relationship between comments and entity tables. sinces the names are in the session, we can insert it with other details into 
-- alter table donation_request_comments
-- add comment_owner_full_name varchar(50) 





--all the queries are in order
--...........................................................
ALTER TABLE donation_request_comments
ADD comment_owner_entity_id integer
references entitys(entity_id)
--............................................................
--///////////////////////////////////////////////////////////////////////////////////////////////////////
CREATE TYPE operations AS ENUM ('withdraw','deposit','donation','purchase_points','sale_points');
CREATE TABLE transactions(
    t_id serial PRIMARY KEY,
    t_date timestamp without time zone default now(),
    t_time time,
    t_type operations,
    t_amount real,
    entity_id integer references entitys );
CREATE INDEX transactions_entity_id ON transactions (entity_id);
---////////////////////////////////////////////////////////////////

CREATE TABLE balances(
    balance_id serial PRIMARY KEY,
    entity_id integer references entitys,
    starting_balance real default 0 
);


CREATE INDEX balances_entity_id ON balances (entity_id);

ALTER TYPE operations ADD VALUE 'donation_receive' AFTER 'sale_points';
ALTER TABLE transactions ADD currency varchar(32) NOT NULL;

CREATE TYPE sources AS ENUM ('wallet','aggregator');

ALTER TABLE donations ADD donation_source sources NOT NULL;
ALTER TABLE donations ADD donation_time timestamp WITHOUT time zone DEFAULT now();

-- CREATE TABLE points transactions (id,t_date,entity_id,t_type,value)
-- create ENUM points transactions

CREATE TYPE points_operation AS ENUM ('donation','sale','parchase');
CREATE TABLE points_transactions(
	points_transactions_id serial PRIMARY KEY,
	points_transactions_date timestamp NOT NULL,
	points_transactions_entity_id integer references entitys(entity_id),
	points_transactions_type points_operation NOT NULL,
	number_of_points integer NOT NULL
);

-- donation_already_made column

ALTER TABLE donation_requests ADD donation_already_made real;

ALTER TABLE balances ADD starting_points_balance integer;
ALTER TABLE balances ADD balance_period varchar(10) NOT NULL;

CREATE TABLE services(
	service_id serial PRIMARY KEY,
	service_name text NOT NULL
);

CREATE TABLE items(
	item_id serial PRIMARY KEY,
	item_name text NOT NULL
);

CREATE TABLE service_offer(
	s_offer_id serial PRIMARY KEY,
	s_entity_id integer references entitys(entity_id),
	s_offer_date timestamp NOT NULL,
	s_offer_name varchar(32) NOT NULL,
	s_offer_country varchar(32) NOT NULL,
	s_offer_state varchar(32) NOT NULL,
	s_offer_residence text,
	s_working_hours integer NOT NULL,
	s_starting_date date NOT NULL,
	s_offer_details text NOT NULL
);
CREATE INDEX service_offer_entity_id ON service_offer (s_entity_id);

CREATE TABLE material_offer(
	  m_offer_id serial PRIMARY KEY,
	  m_entity_id integer references entitys(entity_id),
	  m_offer_date timestamp DEFAULT now() NOT NULL,
	  m_offer_category text NOT NULL,
	  m_offer_name text NOT NULL,
	  m_offer_units integer NOT NULL,
	  m_offer_country varchar(32) NOT NULL,
	  m_offer_state varchar(32) NOT NULL,
	  m_offer_residence text NOT NULL,
	  m_offer_main_photo text NOT NULL,
	  m_offer_other_photo_1 text,
	  m_offer_other_photo_2 text,
	  m_offer_details text
    );


ALTER TABLE service_offer DROP s_starting_date;
ALTER TABLE service_offer ADD s_starting_date date;
ALTER TABLE service_offer DROP s_offer_date;
ALTER TABLE service_offer ADD s_offer_date timestamp DEFAULT now() NOT NULL;

-- ADDED ON 30th June 2014
ALTER TABLE points_sales RENAME COLUMN entity_id TO seller_entity_id;

--Create a table for keeping points transactions extras

CREATE TABLE points_transaction_extras(
points_transaction_extras_id serial NOT NULL,
points_transaction_extras_transaction_id integer NOT NULL REFERENCES points_transactions(points_transactions_id),
participating_entity_id integer NOT NULL REFERENCES entitys(entity_id);
);

--Altering the new ENUM Types to fit the new points transactions namely
--sale_purchase and donation

ALTER TABLE points_transactions
DROP COLUMN points_transactions_type;
DROP TYPE points_operation;
CREATE TYPE points_operation AS ENUM ('donation', 'sale_purchase');

ALTER TABLE points_transactions
ADD COLUMN points_transactions_type points_operation;

--Add PK to points_transactions_extras
ALTER TABLE
points_transaction_extras
ADD PRIMARY KEY (points_transaction_extras_id);

--Alter table points_sales make price per point a decimal point with
--a precision of 2 decimal places to match currency

ALTER TABLE points_sales ALTER COLUMN price_per_point TYPE decimal(10,2);


CREATE TYPE offer_table AS ENUM('offers','service_offer','material_offer');------------Start from here-----------
create table offer_tracker(
	offer_tracker_id serial primary key,
	offer_tracker_date timestamp default now() not null,
	offer_table_id integer not null,
	tracked_table_name offer_table not null
);

ALTER TABLE offer_tracker RENAME tracked_table_name TO tracked_offer_table_type

--create PROCEDURE:

CREATE FUNCTION log_offers()
RETURNS trigger AS $$
BEGIN
      INSERT INTO offer_tracker(offer_table_id,tracked_offer_table_type) VALUES (NEW.offer_id,TG_TABLE_NAME::offer_table);
      return new;
END;
$$ LANGUAGE plpgsql;	


--A triger to log offer insert operations for offers table 
CREATE TRIGGER log_money_offers 
AFTER INSERT ON offers
FOR EACH ROW
EXECUTE PROCEDURE log_offers(offer_id,money);

--A triger to log offer insert operations for offers table

--........................................................./////////////////////////////////////
CREATE FUNCTION log_s_offers()
RETURNS trigger AS $$
BEGIN
      INSERT INTO offer_tracker(offer_table_id,tracked_offer_table_type) VALUES (NEW.s_offer_id,TG_TABLE_NAME::offer_table);
      return new;
END;
$$ LANGUAGE plpgsql;	


--A triger to log offer insert operations for service table 
CREATE TRIGGER log_service_offers 
AFTER INSERT ON service_offer
FOR EACH ROW
EXECUTE PROCEDURE log_s_offers(s_offer_id,service_);
--............................................................./////////////////////////////////////////////////

CREATE FUNCTION log_m_offers()
RETURNS trigger AS $$
BEGIN
      INSERT INTO offer_tracker(offer_table_id,tracked_offer_table_type) VALUES (NEW.m_offer_id,TG_TABLE_NAME::offer_table);
      return new;
END;
$$ LANGUAGE plpgsql;	


--A triger to log offer insert operations for material table 
CREATE TRIGGER log_material_offers 
AFTER INSERT ON material_offer
FOR EACH ROW
EXECUTE PROCEDURE log_m_offers(m_offer_id,material);
--................................................................////////////////
ALTER TABLE balances ALTER balance_period DROP NOT NULL;

CREATE FUNCTION set_new_user_balance()
RETURNS TRIGGER AS $$
BEGIN
	INSERT INTO balances (entity_id,starting_balance,starting_points_balance) VALUES(NEW.entity_id,0,0);
	return new;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER new_user_balance 
AFTER UPDATE ON entitys
FOR EACH ROW
EXECUTE PROCEDURE set_new_user_balance(entity_id,starting_balance,starting_points_balance);

--Altering offer tables and offer_tracker
ALTER TABLE offers ADD accepted_application_id integer;
ALTER TABLE service_offer ADD accepted_application_id integer;
ALTER TABLE material_offer ADD accepted_application_id integer;

ALTER TABLE offer_applications ADD is_accepted boolean DEFAULT false;
ALTER TABLE offer_applications ADD table_name offer_table NOT NULL;

ALTER TABLE offer_tracker ADD is_complete boolean DEFAULT false;

CREATE TYPE service_material_status AS ENUM('pending','accepted','completed');
--pending means that an offer post has been made and either no application has been made or applications have been made and none has been accpeted

ALTER TABLE service_offer ADD status service_material_status DEFAULT 'pending';

ALTER TABLE material_offer ADD status service_material_status DEFAULT 'pending';

ALTER TABLE offer_applications DROP offer_id;
ALTER TABLE offer_applications ADD offer_id integer;


CREATE FUNCTION update_offer_tracker_on_application_acceptance()
RETURNS TRIGGER AS $$
BEGIN
	IF TG_TABLE_NAME='service_offer' THEN
		UPDATE offer_tracker SET is_complete=TRUE WHERE offer_table_id=NEW.s_offer_id AND tracked_offer_table_type = TG_TABLE_NAME::offer_table;
	ELSIF TG_TABLE_NAME='material_offer' THEN
		UPDATE offer_tracker SET is_complete=TRUE WHERE offer_table_id=NEW.m_offer_id AND tracked_offer_table_type = TG_TABLE_NAME::offer_table;
	ELSIF TG_TABLE_NAME='offers' THEN
		UPDATE offer_tracker SET is_complete=TRUE WHERE offer_table_id=NEW.offer_id AND tracked_offer_table_type = TG_TABLE_NAME::offer_table;
	END IF;
	return new;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_offer_tracker_on_service_application
AFTER UPDATE ON service_offer
FOR EACH ROW 
EXECUTE PROCEDURE update_offer_tracker_on_application_acceptance(service_table_id,table_service);

CREATE TRIGGER update_offer_tracker_on_material_application
AFTER UPDATE ON material_offer
FOR EACH ROW 
EXECUTE PROCEDURE update_offer_tracker_on_application_acceptance(material_table_id,table_material);

CREATE TRIGGER update_offer_tracker_on_money_application
AFTER UPDATE ON offers
FOR EACH ROW 
EXECUTE PROCEDURE update_offer_tracker_on_application_acceptance(offers_table_id,table_offers);

ALTER TABLE transactions ADD transaction_root text NOT NULL;


ALTER TABLE points_transactions
ADD buyer_entity_id integer REFERENCES entitys(entity_id),
ADD points_sale_id integer REFERENCES points_sales(points_sale_id);
DROP TABLE points_transaction_extras;

ALTER TABLE entitys ADD state varchar(32);

//13/08/2013

ALTER TABLE points_transactions ALTER COLUMN points_transactions_date SET DEFAULT now();
ALTER TABLE transactions ALTER COLUMN currency DROP NOT NULL;
ALTER TABLE transactions ALTER COLUMN donation_source DROP NOT NULL;
ALTER TABLE transactions ALTER COLUMN transaction_root DROP NOT NULL;
