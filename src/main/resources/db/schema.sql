drop table if exists organizations, salary, employees CASCADE;

create table organizations(
	id bigserial,
	title text unique ,

	CONSTRAINT pk_organizations_id primary key(id)
);

create table salary (
	id bigserial,
	value bigint unique,

	CONSTRAINT pk_salary_id primary key(id)
);

create table employees (
	id bigserial,
	ename text unique,
	salary_id bigint,
	org_id bigint,

	CONSTRAINT pk_employees_id primary key(id),
	CONSTRAINT FK_employees_org_id foreign key(org_id) references organizations(id),
	CONSTRAINT FK_employees_salary_id foreign key(salary_id) references salary(id)
);
