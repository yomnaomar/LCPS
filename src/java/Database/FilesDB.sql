drop table files;

create table files (
	fileID int primary key generated always as identity,
	fileName varchar (30) NOT NULL,
        fileType varchar (10) NOT NULL,
        filePrediction varchar (200) NOT NULL,
        fileDescription varchar (300) NOT NULL)
;