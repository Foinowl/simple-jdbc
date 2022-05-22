
## Docker Compose
### Start
`docker-compose --project-name="pgdb" up -d`

### Stop
`docker-compose --project-name="pgdb" down`

## Console commands:

add [name company][name employee][value salary] - add company, organizations and salary if they exists. Ex: add rt makaka 5000

list - show all employees. shows the employee's name and employee's salary in the format

get [name employee] - get employee by name. Ex: get semenov

delete [name employee] - delete employee by name. Ex: delete semenov

update [id employee][name employee][salary] - update employee by parameters. update 1 petyas 1920

find [name company] - find company by title. Ex: find rt