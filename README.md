# Employee API

## Create a docker image - 

```bash
./mvnw clean compile jib:build -Dimage=rathnapandi/employee:0.0.1
```

## Sample Requests

### Add Employee

```bash
curl --location 'http://localhost:8080/employees' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=8115970117F29FA806BA7B0B6A48CDFC' \
--data-raw '{
    "first_name": "New",
    "last_name": "Employee",
    "email": "newemployee@company.com",
    "gender": "Male",
    "job_title": "Editor",
    "state": "Florida",
    "city": "Pensacola",
    "consumer": "A"
}'
```

### Update Employee
```bash
curl --location --request PUT 'http://localhost:8080/employees' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=8115970117F29FA806BA7B0B6A48CDFC' \
--data '{
    "first_name": "New2"
}'
```

### Get Employee
```bash
curl --location --request GET 'http://localhost:8080/employees' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=8115970117F29FA806BA7B0B6A48CDFC' \
--data '{
    "first_name": "New2"
}'
```

### Delete Employee
```bash
curl --location --request DELETE 'http://localhost:8080/employees' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=8115970117F29FA806BA7B0B6A48CDFC' \
--data '{
    "first_name": "New2"
}'
```

### Delete All Employee
```bash
curl --location --request DELETE 'http://localhost:8080/employees' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=8115970117F29FA806BA7B0B6A48CDFC' \
--data '{
    "first_name": "New2"
}'
```

### Get Recently employees by date
```bash
curl --location 'http://localhost:8080/employees/latest?dateTime=2024-02-13T19%3A16%3A16.018Z' 
```
