CREATE SCHEMA IF NOT EXISTS ems_sponsor;

CREATE TABLE IF NOT EXISTS ems_sponsor.sponsors (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name TEXT UNIQUE NOT NULL,
  website TEXT NOT NULL,
  financial_contribution INTEGER,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);