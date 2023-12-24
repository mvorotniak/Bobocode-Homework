-- Define enum types
CREATE TYPE advisor_role AS ENUM ('associate', 'partner', 'senior');
CREATE TYPE phone_type AS ENUM ('home', 'work', 'mobile');
CREATE TYPE application_status AS ENUM ('new', 'assigned', 'on_hold', 'approved', 'canceled', 'declined');
CREATE TYPE user_type AS ENUM ('advisor', 'applicant');

-- Create users table
CREATE TABLE users (
                       id BIGSERIAL,
                       email VARCHAR(255) NOT NULL CHECK (email LIKE '%@%'),
                       username VARCHAR(255) NOT NULL,
                       user_type user_type NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       CONSTRAINT users_PK PRIMARY KEY (id),
                       CONSTRAINT users_email_UQ UNIQUE (email)
);

-- Create advisors table
CREATE TABLE advisors (
                          id BIGINT,
                          "role" advisor_role NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          CONSTRAINT advisors_PK PRIMARY KEY (id),
                          CONSTRAINT advisors_users_FK FOREIGN KEY (id) REFERENCES users
);

-- Create applicants table
CREATE TABLE applicants (
                            id BIGINT,
                            first_name VARCHAR(255) NOT NULL,
                            last_name VARCHAR(255) NOT NULL,
                            social_security_number INT NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                            updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                            CONSTRAINT applicants_PK PRIMARY KEY (id),
                            CONSTRAINT applicants_users_FK FOREIGN KEY (id) REFERENCES users,
                            CONSTRAINT ssn_UQ UNIQUE (social_security_number)
);

-- Create addresses table
CREATE TABLE addresses (
                           id BIGSERIAL,
                           applicant_id BIGINT NOT NULL,
                           city VARCHAR(50) NOT NULL,
                           street VARCHAR(50) NOT NULL,
                           number INT NOT NULL,
                           zip INT NOT NULL,
                           apt INT NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                           updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                           CONSTRAINT addresses_PK PRIMARY KEY (id),
                           CONSTRAINT applicants_addresses_FK FOREIGN KEY (applicant_id) REFERENCES applicants
);

CREATE INDEX idx_addresses_applicant_id ON addresses (applicant_id);

-- Create phone_numbers table
CREATE TABLE phone_numbers (
                               id BIGSERIAL,
                               applicant_id BIGINT NOT NULL,
                               number VARCHAR(50) NOT NULL,
                               type phone_type NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                               CONSTRAINT phone_numbers_PK PRIMARY KEY (id),
                               CONSTRAINT phone_numbers_applicants_FK FOREIGN KEY (applicant_id) REFERENCES applicants
);

CREATE INDEX idx_phone_numbers_applicant_id ON phone_numbers (applicant_id);

-- Create applications table
CREATE TABLE applications (
                              id BIGSERIAL,
                              applicant_id BIGINT NOT NULL,
                              advisor_id BIGINT,
                              amount NUMERIC(15, 2) NOT NULL,
                              status application_status NOT NULL,
                              assigned_at TIMESTAMP,
                              created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                              updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                              CONSTRAINT applications_PK PRIMARY KEY (id),
                              CONSTRAINT applications_applicants_FK FOREIGN KEY (applicant_id) REFERENCES applicants,
                              CONSTRAINT applications_advisors_FK FOREIGN KEY (advisor_id) REFERENCES advisors,
                              CONSTRAINT applications_advisors_assigned_advisor_id_CHK CHECK (
                                      (advisor_id IS NOT NULL AND assigned_at IS NOT NULL)
                                      OR (advisor_id IS NULL AND assigned_at IS NULL)
                                  )
);

CREATE INDEX idx_applications_applicant_id ON applications (applicant_id);
CREATE INDEX idx_applications_advisor_id ON applications (advisor_id);
