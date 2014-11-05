ALTER TABLE transactions ALTER COLUMN t_amount TYPE numeric USING t_amount::numeric;
ALTER TABLE offers ALTER COLUMN offer_amount TYPE numeric USING offer_amount::numeric;
ALTER TABLE mobile_money_temp ALTER COLUMN transaction_amount TYPE numeric USING transaction_amount::numeric;
ALTER TABLE donations ALTER COLUMN donation_amount TYPE numeric USING donation_amount::numeric;
ALTER TABLE donation_requests ALTER COLUMN donation_request_amount TYPE numeric USING donation_request_amount::numeric;
ALTER TABLE bank_withdrawal_requests ALTER COLUMN request_amount  TYPE numeric USING request_amount ::numeric;