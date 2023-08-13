drop table if exists TOPIC;
create table TOPIC (
	id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	title varchar(30)
);