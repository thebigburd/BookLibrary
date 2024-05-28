
# Library API: Athena

A Spring Boot project utilising Hibernate and a PostgreSQL database to simulate an online library where users can borrow books.


## How to run the Application

Clone the project

```bash
  git clone https://github.com/thebigburd/BookLibrary.git
```

Create a database in PostgreSQL named "athenadb". Alternatively change the datasource in the application.yml file to your own database.

```bash
  CREATE DATABASE athenadb;
```

Then go to the project directory

```bash
  cd src/main/java/com/thebigburd/LibraryApplication/LibraryApplication.java
```

Run LibraryApplication.java

The API will be available at:
```bash
 http://localhost:8080/athena/
```
