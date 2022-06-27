# Recruitment Application Showcase
This is a project for the course IV1201, Design of Global Applications, at KTH, Sweden.
This project is a fullstack recruitment platform, where applicants can apply for the job and recruiters can read and approve the applications.

## Configuration & Startup

##### Create Local datasource (for development/testing)
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

Follow installation and startup instructions in each repository.
