
# Recruitment Application Recruiter Front

## Getting started
This project is built with Maven using Spring Boot, Thymeleaf, PostgreSQL. Run these commands to get started:

```
./mvnw clean install
./mvnw spring-boot:run
```

## Visuals
Recruiter Home Page
![Home Page](documentation-images/recruiter_home.png)

## Configuration

#### For local front end server
```
const host = "http://localhost:8005"
```

#### For local back end server
These variables needs to be set when using a local back end server.

In [application.properties](src/main/resources/application.properties)
```
server.port=8005
backend.dev-local=http://localhost:8080
back-end.dev-local.api.application.url=http://localhost:8080/api/users/recruiter/applications/%s/
back-end.dev-local.api.login.url=http://localhost:8080/api/users/recruiter/authenticate
back-end.dev-local.api.filter.url=http://localhost:8080/api/users/recruiter/filter
back-end.dev-local.api.update-application.url=http://localhost:8080/api/users/recruiter/applications/%s/state
back-end.dev-local.api.competence.url=http://localhost:8080/api/competences/
back-end.dev-local.api.application-by-competence.url=http://localhost:8080/api/users/recruiter/applications/competences/page?size=%s&page=%s
```
## Installation
Spring Boot will automatically install and build the neccessary dependencies with Maven.

## Authors and acknowledgment
 - [Vera Lindström](https://github.com/veralindstrom)
 - [Estelle Hue](https://github.com/estellehue)
 - [Samuel Ferrara](https://github.com/ferrara-dev)


## Links
Project Homepage: [https://recruiter-app-recruiter.herokuapp.com](https://recruiter-app-recruiter.herokuapp.com)
<br>
Wiki Page: [https://gitlab.com/iv1201group15/recruitment-application-recruiter-front/-/wikis/home](https://gitlab.com/iv1201group15/recruitment-application-recruiter-front/-/wikis/home)

