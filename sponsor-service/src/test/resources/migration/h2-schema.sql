CREATE SCHEMA IF NOT EXISTS ems_sponsor;

CREATE TABLE IF NOT EXISTS ems_sponsor.sponsors (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR UNIQUE NOT NULL,
  website VARCHAR NOT NULL,
  financial_contribution INTEGER,
  email VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  physical_address VARCHAR NOT NULL
);