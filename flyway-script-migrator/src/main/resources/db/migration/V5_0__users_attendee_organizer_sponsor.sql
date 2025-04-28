
CREATE TYPE user_role_enum AS ENUM ('ATTENDEE', 'SPONSOR', 'ORGANIZER', 'ADMIN');

CREATE TABLE IF NOT EXISTS ems_admin.users(
  id UUID NOT NULL, -- corresponds to FusionAuth
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  username TEXT UNIQUE NOT NULL,
  email TEXT UNIQUE NOT NULL,
  first_name TEXT,
  last_name TEXT,
  role user_role_enum NOT NULL,
  mobile_phone TEXT,
  birth_date DATE
);

