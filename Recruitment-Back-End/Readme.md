# Recruitment Application
This is a project for the course IV1201, Design of Global Applications, at KTH, Sweden.
The project is back-end server that is part of a fullstack recruitment platform, where applicants can apply for the job and recruiters can read and approve the applications.

This server is responsible for providing service endpoints that are consumed by front-end applications.
- The applicant front end can be found [here](https://gitlab.com/iv1201group15/recruitment-application-applicant-front)!
- The recruiter front end can be found [here](https://gitlab.com/iv1201group15/recruitment-application-recruiter-front)!

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
## Development
clone the repository to get started with development 
```
git clone git@gitlab.com:iv1201group15/recruitment-application-recruiter-front.git
cd recruitment-application-recruiter-front
./mvnw clean install
```

## Test and Deploy
This project uses the built-in continuous integration in GitLab. The [.gitlab-ci.yml](.gitlab-ci.yml) file will run for each push to any branch, but only deploy the main branch.

#### Future testing
Unit tests is not yet implemented in this project. If future testers decides to create such tests and use, for example, [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/) the result of each test will be displayed in the build job log for that particular pipeline.

#### Static analysing
The project uses [Maven PMD Plugin](https://maven.apache.org/plugins/maven-pmd-plugin/) for static analysing which is added in the [pom.xml](pom.xml). In the [excludeFromPmdCheck.properties](excludeFromPmdCheck.properties) can one add rules to be excluded in the analysis.

#### Deployment
The environment variables below are added in the GitLab repository to be able to test and deploy with CI/CD pipelines. The [.gitlab-ci.yml](.gitlab-ci.yml) script will first install ruby to be able to later use dpl to deploy the main branch to heroku. 

Read more about the .gitlab-ci.yml file [here](https://docs.gitlab.com/ee/ci/yaml/gitlab_ci_yaml.html) and about deploying with dpl [here](https://docs.gitlab.com/ee/ci/examples/deployment/)
```
$SPRING_DATASOURCE_DB: The database name
$SPRING_DATASOURCE_USER: The database username
$SPRING_DATASOURCE_PASSWORD: The database password
$HEROKU_APP: The heroku app link for deployment
$HEROKU_API_KEY: The personal heroku API key for the owner of the heroku app
```

## Authors and acknowledgment
 - [Vera Lindström](https://github.com/veralindstrom)
 - [Estelle Hue](https://github.com/estellehue)
 - [Samuel Ferrara](https://github.com/ferrara-dev)

