-- Insert into users table
INSERT INTO users (email, username, user_type) VALUES
                                        ('user1@example.com', 'User1', 'advisor'),
                                        ('user2@example.com', 'User2', 'advisor'),
                                        ('user3@example.com', 'User3', 'advisor'),
                                        ('user4@example.com', 'User4', 'applicant'),
                                        ('user5@example.com', 'User5', 'applicant'),
                                        ('user6@example.com', 'User6', 'applicant');

-- Insert into advisors table
INSERT INTO advisors (id, role) VALUES
                                    (1, 'associate'),
                                    (2, 'partner'),
                                    (3, 'senior');

-- Insert into applicants table
INSERT INTO applicants (id, first_name, last_name, social_security_number) VALUES
                                                                               (4, 'John', 'Doe', 123456789),
                                                                               (5, 'Jane', 'Smith', 987654321),
                                                                               (6, 'Bob', 'Johnson', 456789123);

-- Insert into addresses table
INSERT INTO addresses (applicant_id, city, street, number, zip, apt) VALUES
                                                                         (4, 'City1', 'Street1', 123, 45678, 101),
                                                                         (5, 'City2', 'Street2', 456, 78901, 202),
                                                                         (6, 'City3', 'Street3', 789, 12345, 303);

-- Insert into phone_numbers table
INSERT INTO phone_numbers (applicant_id, number, type) VALUES
                                                           (4, '123-456-7890', 'home'),
                                                           (5, '456-789-0123', 'work'),
                                                           (6, '789-012-3456', 'mobile');

-- Insert into applications table
INSERT INTO applications (applicant_id, assigned_advisor_id, amount, status, assigned_at) VALUES
                                                                                 (4, null, 1000.00, 'new', null),
                                                                                 (5, 2, 1500.00, 'assigned', now()),
                                                                                 (6, 3, 2000.00, 'approved', now());
