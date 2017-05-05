drop table files;
drop table edited;

create table files (
	fileID int primary key generated always as identity,
	fileName varchar (30) NOT NULL,
        fileType varchar (10) NOT NULL,
        filePrediction varchar (200) NOT NULL,
        fileDescription varchar (300) NOT NULL,
        fileUploader VARCHAR(140) NOT NULL)
;

create table edited (
        editID int primary key generated always as identity,
	fileID int NOT NULL,
	fileName varchar (200) NOT NULL,
        fileType varchar (10) NOT NULL,
        filePrediction varchar (200) NOT NULL,
        fileDescription varchar (300) NOT NULL,
        fileUpdater VARCHAR(140) NOT NULL)
;
