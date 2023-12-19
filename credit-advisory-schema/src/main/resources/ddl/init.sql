-- Define enum types
CREATE TYPE advisor_role AS ENUM ('associate', 'partner', 'senior');
CREATE TYPE phone_type AS ENUM ('home', 'work', 'mobile');
CREATE TYPE application_status AS ENUM ('new', 'assigned', 'on_hold', 'approved', 'canceled', 'declined');

-- Create users table
CREATE TABLE users (
                       id BIGSERIAL CONSTRAINT users_PK PRIMARY KEY,
                       email VARCHAR(255) NOT NULL CONSTRAINT users_email_UQ UNIQUE CHECK (email LIKE '%@%'),
                       username VARCHAR(255) NOT NULL
);

-- Create advisors table
CREATE TABLE advisors (
                          id BIGSERIAL CONSTRAINT advisors_PK PRIMARY KEY REFERENCES users,
                          role advisor_role NOT NULL
);

-- Create applicants table
CREATE TABLE applicants (
                            id BIGSERIAL CONSTRAINT applicants_PK PRIMARY KEY REFERENCES users,
                            first_name VARCHAR(255) NOT NULL,
                            last_name VARCHAR(255) NOT NULL,
                            social_security_number INT NOT NULL
);

-- Create addresses table
CREATE TABLE addresses (
                           id BIGSERIAL CONSTRAINT addresses_PK PRIMARY KEY,
                           applicant_id BIGINT NOT NULL,
                           city VARCHAR(50) NOT NULL,
                           street VARCHAR(50) NOT NULL,
                           number INT NOT NULL,
                           zip INT NOT NULL,
                           apt INT NOT NULL,
                           CONSTRAINT applicants_addresses_FK FOREIGN KEY (applicant_id) REFERENCES applicants
);

-- Create phone_numbers table
CREATE TABLE phone_numbers (
                               id BIGSERIAL CONSTRAINT phone_numbers_PK PRIMARY KEY,
                               applicant_id BIGINT NOT NULL,
                               number VARCHAR(50) NOT NULL,
                               type phone_type NOT NULL,
                               CONSTRAINT phone_numbers_applicants_FK FOREIGN KEY (applicant_id) REFERENCES applicants
);

-- Create applications table
CREATE TABLE applications (
                              id BIGSERIAL CONSTRAINT applications_PK PRIMARY KEY,
                              applicant_id BIGINT NOT NULL,
                              assigned_advisor_id BIGINT,
                              amount NUMERIC(15, 2) NOT NULL,
                              status application_status NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                              assigned_at TIMESTAMP,
                              CONSTRAINT applications_applicants_FK FOREIGN KEY (applicant_id) REFERENCES applicants,
                              CONSTRAINT applications_advisors_FK FOREIGN KEY (assigned_advisor_id) REFERENCES advisors,
                              CHECK ( (assigned_advisor_id IS NOT NULL AND assigned_at IS NOT NULL) 
                                          OR (assigned_advisor_id IS NULL AND assigned_at IS NULL))
);
