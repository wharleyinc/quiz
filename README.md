# QuizApp
REST Quiz App - GetCova Coding Test

# Welcome to the QuizZz project!

This application is being developed as a coding assessment test by [GetCova](https://getcova.com/).

The application requirement is simple; it's a quiz application in which users can attempt quiz questions and obtain results when marked. quizzes. Other users can then play those quizzes. To make the application a bit more interesting, it also includes a user management module with functionality such as email sign up, login or forgot my password. It then uses Spring Security and Spring AOP for authentication and authorization purposes.

It is solely based on exposing REST endpoints.

### The Quiz application lives on [Heroku](https://getcova-quiz.herokuapp.com)
You can access the exposed apis via this [postman collection][![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/10554287-d8ce4f66-b7ce-43e9-8435-ee325c0571ea?action=collection%2Ffork&collection-url=entityId%3D10554287-d8ce4f66-b7ce-43e9-8435-ee325c0571ea%26entityType%3Dcollection%26workspaceId%3D1c590a9f-9c02-4c3a-8373-d5dd8a35c892)

This collection accesses the REST APIs exposed on https://getcova-quiz.herokuapp.com.

HowTos

POST /register - used to register new users on the API service, returning an activation key.

POST /activate - used to activate user registration, returns true if successful.

POST /authenticate - handles the login of the user using valid credentials, used previously in the /register endpoint, returns JWT token, to be used as bearer tokens in subsequent API calls.

GET /authenticate - returns the username of current user logged-in into the service.

POST /quizzes - Creates a quiz with 10 questions with four question options each, and returns the whole quiz object.

POST /submit-answer - takes in ids of a question and option, then returns a true if correct or false if wrong.

GET /mark-quiz - attempts to mark the quiz, taking in the quiz id, and returning the quiz score.



## Getting Started

All the code required to run this project is available in this Git repository. You can either download it as a zip file from Github or run:

```bash
$ git clone https://github.com/wharleyinc/quiz.git
```

### Environment

In order to run `Quiz` you will need:

* [Java](https://www.java.com) - Main Backend Language
* [Maven](https://maven.apache.org/) - Dependency Management
* [Postgres](https://www.postgresql.org) - The default RDBMS.

In addition, there are a few optional yet recommended installations:

* [Git](https://git-scm.com)
* Your preferred IDE. I am using [IntelliJ](https://https://www.jetbrains.com/idea/business/), but feel free to use whichever you find more convenient.


### Prerequisites

#### Database Setup

Before starting the application, you will need to set up the database. By default, QuizZz attempts to connect to a database called QuizZz in localhost:3306, or one called QuizZzTest in the same location for the E2E automatic tests. You can change the default location and name of the databases in application.properties and application-test.properties respectively.

If you don't wish to set up a PostgreSQL database, you can switch to any database of your choice, by tinkering with the `application.yml` file


### Run the Quiz application!

You can run the application either using maven on the command line or using your IDE to import and run the code.

#### Using Maven

If you don't want to go through the process of using an IDE and just want to get the project running to explore it, navigate to the directory where you downloaded the source code and run:

```bash
$ mvn spring-boot:run
```

If everything went well, you should be able to access the web app here: http://localhost:8080

#### Using your IDE

Importing this project into your IDE should be a stroll in the park.
Please refer to your IDE's help pages.

#### Generating JAR file

To generate the JAR file and start up the server, run:

```bash
$ mvn clean package
$ cd target
$ java -jar Quiz-0.0.1-SNAPSHOT.jar
```

## Running the tests

At the moment, tests would be written after now to adequately cater for various use cases.

### Unit Tests

Still in the pipeline.


## Tehcnologies Used


* [Java](https://www.java.com) - Main Backend Language
* [Maven](https://maven.apache.org/) - Dependency Management
* [Postgres](https://www.postgresql.org) - The default RDBMS.
#### Spring Frameworks -
* [Spring Boot](https://projects.spring.io/spring-boot/) - The Framework of Frameworks
* [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) - The Web framework
* [Spring Security](https://projects.spring.io/spring-security/) - Security, Authentication, Authorization
* [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/) - Abstraction Layer on top of JPA

#### Databases

* [MySQL](https://www.mysql.com) - The default RDBMS.
* [Hibernate 5.6.3.Final](https://www.hibernate.org) - ORM


#### Others

* [Git](https://www.git-scm.com) - Version Control
* [Heroku Cli](http://heroku.com) - Application Deployment Container Server

## Author

* **OLADAPO Olawale** - - [wharleyinc](https://github.com/wharleyinc)

## What next?

* Unit Testing
* REST APIs test
* `README.md`. This readme file contains everything about this assessment test.


