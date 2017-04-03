CREATE TABLE ELCDuser
(
userID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
firstname VARCHAR(140),
lastname VARCHAR(140),
sex VARCHAR(1),
dob DATE,
age INT,
email VARCHAR(140) UNIQUE,
password VARCHAR(140),
hospitalName VARCHAR(140)
);

INSERT INTO ELCDuser(firstname, lastname, sex, dob, age, email, password, hospitalName) VALUES ('Yomna', 'Omar', 'F', '1996-04-07', 21, 'yomna.hassan@hotmail.com', '123456', 'Al-Zahra Hospital');
INSERT INTO ELCDuser(firstname, lastname, sex, dob, age, email, password, hospitalName) VALUES ('Abdullah', 'Tasleem', 'M', '1995-05-05', 21, 'ath@gmail.com', '123456', 'Al-Zahra Hospital');
INSERT INTO ELCDuser(firstname, lastname, sex, dob, age, email, password, hospitalName) VALUES ('Rowan', 'Ibrahim', 'F', '1995-10-14', 21, 'rowanib@hotmail.com', '123456', 'Al-Zahra Hospital');

CREATE TABLE Doctor
(
doctorID INT,
CONSTRAINT doctor_id_pk PRIMARY KEY(doctorID),
CONSTRAINT doctor_userID_fk FOREIGN KEY(doctorID) REFERENCES ELCDuser(userID)
);

INSERT INTO Doctor(doctorID) VALUES(1);

CREATE TABLE Patient
(
patientID INT,
doctorID INT,
CONSTRAINT patient_id_pk PRIMARY KEY(patientID),
CONSTRAINT patient_userID_fk FOREIGN KEY(patientID) REFERENCES ELCDuser(userID),
CONSTRAINT patient_doctorID_fk FOREIGN KEY(doctorID) REFERENCES Doctor(doctorID)
);

INSERT INTO Patient(patientID, doctorID) VALUES(2, 1);
INSERT INTO Patient(patientID, doctorID) VALUES(3, 1);

CREATE TABLE Comment
(
commentID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
patientID INT,
doctorID INT,
comment VARCHAR(1000),
dateAdded DATE,
isVisible BOOLEAN,
CONSTRAINT comment_patientID_fk FOREIGN KEY(patientID) REFERENCES Patient(patientID),
CONSTRAINT comment_doctorID_fk FOREIGN KEY(doctorID) REFERENCES Doctor(doctorID)
);

INSERT INTO Comment(patientID, doctorID, comment, dateAdded, isVisible) VALUES(2, 1, 'Low-risk, but still needs to stop smoking', '2017-03-28', TRUE);
INSERT INTO Comment(patientID, doctorID, comment, dateAdded, isVisible) VALUES(2, 1, 'If smoking does not stop, he may develop several types of cancers', '2017-03-28', FALSE);
INSERT INTO Comment(patientID, doctorID, comment, dateAdded, isVisible) VALUES(3, 1, 'No worry at all! Good health :)', '2017-03-25', TRUE);

CREATE TABLE Gene
(
geneID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
patientID INT,
doctorID INT,
name VARCHAR(140),
result VARCHAR(1000),
isFatal BOOLEAN,
CONSTRAINT gene_patientID_fk FOREIGN KEY(patientID) REFERENCES Patient(patientID),
CONSTRAINT gene_doctorID_fk FOREIGN KEY(doctorID) REFERENCES Doctor(doctorID)
);

INSERT INTO Gene(patientID, doctorID, name, result, isFatal) VALUES(2, 1, 'Gene 1', 'This gene is a direct correspondent to lung cancer, better watch out', TRUE);
INSERT INTO Gene(patientID, doctorID, name, result, isFatal) VALUES(3, 1, 'Gene 1', 'Not present', FALSE);
INSERT INTO Gene(patientID, doctorID, name, result, isFatal) VALUES(3, 1, 'Gene 2', 'Must take care a little', TRUE);