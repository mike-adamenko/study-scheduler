DROP TABLE doctor IF EXISTS;
DROP TABLE room IF EXISTS;
DROP TABLE patient IF EXISTS;

CREATE TABLE doctor (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(30)
);
CREATE INDEX doctor_name
  ON doctor (name);

CREATE TABLE room (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(30)
);

CREATE TABLE patient (
  id        INTEGER IDENTITY PRIMARY KEY,
  name      VARCHAR(30),
  sex       TINYINT,
  birth_date DATE
);

CREATE TABLE study (
  id        INTEGER IDENTITY PRIMARY KEY,
  patient_id      INTEGER,
  description       VARCHAR(300),
  status TINYINT,
  start_time DATETIME,
  end_time DATETIME,
  doctor_id      INTEGER,
  room_id      INTEGER
);

ALTER TABLE study
  ADD CONSTRAINT fk_study_patient FOREIGN KEY (patient_id) REFERENCES patient (id);
ALTER TABLE study
  ADD CONSTRAINT fk_study_doctor FOREIGN KEY (doctor_id) REFERENCES doctor (id);
ALTER TABLE study
  ADD CONSTRAINT fk_study_room FOREIGN KEY (room_id) REFERENCES room (id);


