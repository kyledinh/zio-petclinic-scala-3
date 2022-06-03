CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS owner
(
    id         uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name text NOT NULL,
    last_name  text NOT NULL,
    address    text NOT NULL,
    phone      text NOT NULL,
    email      text NOT NULL
);

CREATE TABLE IF NOT EXISTS pet
(
    id        uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    name      text NOT NULL,
    birthdate date NOT NULL,
    species   text NOT NULL,
    owner_id  uuid NOT NULL REFERENCES owner (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vet
(
    id        uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    last_name text NOT NULL,
    specialty text NOT NULL
);

CREATE TABLE IF NOT EXISTS visit
(
    id          uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    date        date NOT NULL,
    description text      NOT NULL,
    vet_id      uuid      NOT NULL REFERENCES vet (id) ON DELETE CASCADE,
    pet_id      uuid      NOT NULL REFERENCES pet (id) ON DELETE CASCADE
);


-- Vet data for display list
INSERT INTO vet (last_name, specialty)
VALUES ('Dr. Baloo', 'Cardiology');
INSERT INTO vet (last_name, specialty)
VALUES ('Dr. Hedwig', 'Oncology');
INSERT INTO vet (last_name, specialty)
VALUES ('Dr. Totoro', 'Large Animal Internal Medicine');
INSERT INTO vet (last_name, specialty)
VALUES ('Dr. Rajah', 'Dentistry');
INSERT INTO vet (last_name, specialty)
VALUES ('Dr. Sid', 'Neurology');
INSERT INTO vet (last_name, specialty)
VALUES ('Dr. Stuart', 'Small Animal Internal Medicine');


-- Add example owners
INSERT INTO owner (first_name, last_name, address, phone, email)
VALUES ('Emily', 'Elizabeth', '1 Birdwell Island, New York, NY', '212-215-1928', 'emily@bigreddog.com');

-- Add example pet
INSERT INTO pet (name, birthdate, species, owner_id)
VALUES ('Clifford', '1962-02-14', 'Canine', (SELECT id FROM owner WHERE email = 'emily@bigreddog.com'));

-- Add visits
INSERT INTO visit (date, description, vet_id, pet_id)
VALUES ('2001-01-01', 'Checkup', (SELECT id FROM vet WHERE last_name = 'Dr. Baloo'), (SELECT id FROM pet WHERE name = 'Clifford'));