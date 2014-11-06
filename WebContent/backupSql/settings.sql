create table harambesa_settings(
harambesa_settings_id serial unique primary key,
entity_id integer references entitys(entity_id) unique,
connection_request boolean default true,
connection_requires_donation boolean default true,
connection_place_offer boolean default true,
connection_sells_points boolean default true,
bids_on_my_points boolean default true,
buyer_accepts_bid boolean default true,
purchases_on_my_points boolean default true,
received_donation boolean default true,
applications_on_my_offer boolean default true,
my_offer_application_denied boolean default true,
my_offer_application_accepted boolean default true

);