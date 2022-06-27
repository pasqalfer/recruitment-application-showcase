# Recruitment Application
This is a project for the course IV1201, Design of Global Applications, at KTH, Sweden.
This part of the project is back-end server that is part of a fullstack recruitment platform, where applicants can apply for the job and recruiters can read and approve the applications.

## API
###### prefix : /api/users/{role}/*
The server provides two separate REST api's.
The user role given in the path prefix determines what api to access
and which security configuration to use when authorizing the request. 

#### Rest API for recruiter operations
###### prefix : /api/users/recruiter/*
    - authenticate as recruiter user 
    - approve/reject an application
    - fetch applications filtered by competence (Paginated result)
    - fetch application by id

#### Rest API for applicant operations
###### prefix : /api/users/applicant/*
     - authenticate as applicant user
     - register as applicant user
     - fetch applicant object for authenticated user 
     - fetch application object for authenticated user
     - fetch application by id

For more detailed information and example requests, read the api documentation found [here](https://kth-iv1201.herokuapp.com/swagger-ui/index.html#/).



## Configuration


##### Local datasource (for development)
Start the postgres server locally and create the database.
```
psql -U postgres -c "CREATE DATABASE local_db;"
```
Now, edit the following properties in the application.properties file.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/local_db
spring.datasource.username=postgres
spring.datasource.password=<postgres-password>
```

## Installation & Startup

The project is built with Maven.
Maven will automatically install and build the necessary dependencies provided in the pom.xml file.
 
Run these commands to get the server up and running:
```
./mvnw clean install
./mvnw spring-boot:run
```

## Authors and acknowledgment
 - [Vera Lindström](https://github.com/veralindstrom)
 - [Estelle Hue](https://github.com/estellehue)
 - [Samuel Ferrara](https://github.com/pasqalfer)

