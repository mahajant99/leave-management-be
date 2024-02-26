# leave-management-be
This is an initial backend setup for leave management in Java using Spring Boot.

## Requirements
JDK 17 <br />
[Link for JDK installation](https://docs.oracle.com/en/java/javase/17/install/installation-jdk-microsoft-windows-platforms.html#GUID-A7E27B90-A28D-4237-9383-A58B416071CA)

## How to run

To build the project as a Spring Boot application: <br />
`./gradlew build` <br />
>Run this command when you have made changes to the code or dependencies <br />

To run the project as a Spring Boot application: <br/>
`./gradlew bootRun`<br />
>Run this command when you have to start the development server <br />

Prerequisites
Java 11 or later
PostgreSQL 10 or later
Gradle (for building the project)
A text editor or IDE

Setup and Configuration
Database Setup
Install PostgreSQL: Ensure PostgreSQL is installed on your system. If not, download and install it from the official PostgreSQL website.
Create Database: Connect to PostgreSQL using a client like pgAdmin or psql and create a new database named leave_management.
User and Permissions: Create a new user postgres and grant it all privileges on the leave_management database.
Application Configuration
Clone the Repository: Clone this repository to your local machine using git clone <repository-url>.
Update application.properties: Navigate to src/main/resources and open the application.properties file. Update the database connection details as follows:

spring.datasource.url=jdbc:postgresql://localhost:port/leave_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=none
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration

After every table alteration in the table, update the migration directory, create a new SQL file for the migration. The naming convention for Flyway migration files is V<VERSION>__<DESCRIPTION>.sql, where <VERSION> is a version number and <DESCRIPTION> is a brief description of the migration.
