drop table if exists TODO;
create table TODO (
	id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	title varchar(30)
);