drop table if exists organizations, employees, employees CASCADE;

create table organizations(
	id bigserial,
	title text,

	CONSTRAINT pk_organizations_id primary key(id)
);

create table salary (
	id bigserial,
	value bigint,

	CONSTRAINT pk_salary_id primary key(id)
);

create table employees (
	id bigserial,
	ename text,
	salary_id bigint,
	org_id bigint,

	CONSTRAINT pk_employees_id primary key(id),
	CONSTRAINT FK_employees_org_id foreign key(org_id) references organizations(id),
	CONSTRAINT FK_employees_salary_id foreign key(salary_id) references salary(id)
);
