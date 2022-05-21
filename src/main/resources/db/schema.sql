drop table if exists organizations, salary, employees CASCADE;

create table organizations(
	id bigserial,
	title text unique,

	CONSTRAINT pk_organizations_id primary key(id)
);

create table salary (
	id bigserial,
	value bigint unique ,

	CONSTRAINT pk_salary_id primary key(id)
);

create table employees (
	id bigserial,
	ename text unique ,
	salary_id bigint,
	org_id bigint,

	CONSTRAINT pk_employees_id primary key(id),
	CONSTRAINT FK_employees_org_id foreign key(org_id) references organizations(id),
	CONSTRAINT FK_employees_salary_id foreign key(salary_id) references salary(id)
);

insert into salary (value) values (500);
insert into salary (value) values (1500);
insert into salary (value) values (3100);

insert into organizations (title) values ('rt');

insert into organizations (title) values ('tesla');

insert into employees (ename, salary_id, org_id) values ('petrov', 1, 1);
insert into employees (ename, salary_id, org_id) values ('ivanov', 2, 2);
insert into employees (ename, salary_id, org_id) values ('ovanov', 3, 1);
insert into employees (ename, salary_id, org_id) values ('semenov', 2, 2);
insert into employees (ename, salary_id, org_id) values ('elona', 3, 2);