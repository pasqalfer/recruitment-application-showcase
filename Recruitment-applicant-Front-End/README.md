# Recruitment Application Applicant Front
This is a project for the course IV1201, Design of Global Applications, at KTH, Sweden.
The project is about creating a recruitment platform where applicants can apply for the job and recruiters can read and approve the applications.

This repository concers the applicant front end. 
- The recruiter front end can be found [here](https://gitlab.com/iv1201group15/recruitment-application-recruiter-front)!
- The back end can be found [here](https://gitlab.com/iv1201group15/recruitment-application)!

## Getting started
This project is built with Maven using Spring Boot, Thymeleaf, PostgreSQL. Run these commands to get started:

```
./mvnw clean install
./mvnw spring-boot:run
```

## Visuals
Applicant Home Page
![Applicant Home](/documentation-images/applicant_home.png)


## Configuration
This project makes use of some known environment variables that are used in the production to facilitate development. These can be found in the file [application.properties](src/main/resources/application.properties) that needs to be set depending on using local or cloud back end server. 

#### For local back end server
These variables needs to be set when using a local back end server.

In [application.properties](src/main/resources/application.properties)
```
server.port=8000
back-end.dev-local.api.new-password.url=http://localhost:8080/api/users/applicant/password/reset
back-end.dev-local.api.user.url=http://localhost:8080/api/users/applicant/
back-end.dev-local.api.login.url=http://localhost:8080/api/users/applicant/authenticate
back-end.dev-local.api.register.url=http://localhost:8080/api/users/applicant/register
back-end.dev-local.api.application.url=http://localhost:8080/api/users/applicant/application
back-end.dev-local.api.competence.url=http://localhost:8080/api/competences/

```

In [APIService.js](src/main/java/com/iv1201/applicantwebapp/applicantwebapp/service/APIService.java)
```
@Value("${back-end.dev-local.api.login.url}")
private String loginURL;
@Value("${back-end.dev-local.api.register.url}")
private String registerURL;
@Value("${back-end.dev-local.api.user.url}")
private String userURL;
@Value("${back-end.dev-local.api.competence.url}")
private String competenceURL;
@Value("${back-end.dev-local.api.application.url}")
private String applicationURL;
@Value("${back-end.dev-local.api.new-password.url}")
private String newPasswordURL;
```

## Installation
Spring Boot will automatically install and build the neccessary dependencies with Maven.

## Authors and acknowledgment
 - [Vera Lindström](https://github.com/veralindstrom)
 - [Estelle Hue](https://github.com/estellehue)
 - [Samuel Ferrara](https://github.com/ferrara-dev)

## Project status
Possible future improvements: 
 - Unit Testing
 - Integration Testing
 - Acceptance Testing
 - Internationalization 
 - Localization
 - New password sent to email 

## Links
Project Homepage: [https://recruiter-app-applicant.herokuapp.com](https://recruiter-app-applicant.herokuapp.com)
<br>
Wiki Page: [https://gitlab.com/iv1201group15/recruitment-application-applicant-front/-/wikis/home](https://gitlab.com/iv1201group15/recruitment-application-applicant-front/-/wikis/home)

