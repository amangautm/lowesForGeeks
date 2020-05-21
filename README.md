# LowesForGeeks
![lowes logo](https://user-images.githubusercontent.com/37938543/82596757-7e6a9c00-9bc5-11ea-8127-ae568960b12f.jpg)

An Unified Portal

# Description

LowesForGeeks is an unified portal which is aimed at our people and our organization itself where they can host events and formation of teams.The basic entities 
involved are
- **Event:** Includes Private event, Team event, Organization event
- **Member:** Includes Normal member, Team admin and Organization admin
- **Team:** Includes list of members
- **Organization:** Include list of members and teams

# Tech Stack Details
The languages, frameworks and libraries. used in the project are:
- **Java**(Core Language)
- **MySQL**(Open-Source Relational Database Management System)
- **Git**(Free and Open-Source Distributed Version Control system)
#
- **Spring MVC**(Framework to ease the bootstrapping and development of new Spring Applications)
- **Maven**( Dependency Management)
- **Hibernate**(ORM)
- **Postman**(API Development Environment (Testing))

# Requirements
- Install Java 1.8 or higher.
- Add java to PATH environment variables.
- Install a sql database server.
- Install Git

# Installation

- Click on `Clone or Download` button and copy the `URL`.
- Open `Git Bash` or `CMD` and type `git clone <URL>`.
- Install IntelliJ Community Edition
- Open the loweforgeeks folder as IntelliJ Community Edition Project
- Choose the `SpringBootApplication` file(i.e LowesForGeeksApplication)
- Right Click or open it and Run it's main function.

# Runtime
- Once the application starts running, in the CMD or terminal, you would see "Tomcat started on port(s): `your port no.` " in the second  last line.(By default 
   it is hosted on `localhost:8080/ `)
- But if `Web server failed to start. Port 8080 was already in use` pops up in terminal ,then to change port type `server.port:8084` in 
`resources/application.properties`
- All the HTTP requests can be made by prefixing "localhost:`your port number`/`your request url`
- In the start insert 1 row in organization,member and team tables then can check mappings from Postman.

# Packages
- **model**— To hold our entities
- **repository**— To communicate with the database
- **service**— To hold our business logic
- **controller**—To listen to the person
- **resources/**— Contains all the static resources, templates and property files
- **resources/application.properties** — It contains application-wide properties. Spring reads the properties defined in this file to configure your application. 
You can define server’s default port, server’s context path, database URLs etc, in this file.
- **pom.xml** — contains all the project dependencies


