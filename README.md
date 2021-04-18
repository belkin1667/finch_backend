# Backend part of social network for travelers *Finch*
Belkin Mike (Backend) & Aretm Goncharov (Frontend) Software Engineering Course Work at National Research University Higher School of Economics, Faculty of Computer Science  
Moscow, Russia
2021

See Frontend Repository [here](https://github.com/artemgoncharov2000/finch-frontend) (React Native iOS app)

# Local Deployment Guide
1. Install Docker
2. Install PostgreSQL 13 with username {dbusername} and password {dbpassword}
3. Create database with name {dbname}
```
> psql -U {dbusername}
# CREATE DATABASE {dbname};
```
5. Clone this repository
6. Open [application-local.properties](/src/main/resources/application-local.properties) file and configure your database:
```
spring.datasource.url=jdbc:postgresql://localhost/{dbname}
spring.datasource.username={dbusername}
spring.datasource.password={dbpassword}
```
7. Open [application.properties](/src/main/resources/application.properties) and set active profile to local:
```
spring.profiles.active=local
```
9. Run following commands in project's root directory (this directory contains Dockerfile):
```
> docker build -t finch-backend .
> docker run -p 8080:8080 finch-backend
```
8. The application is now up and running. You can send your requests to `localhost:8080`
9. You can access Swagger2 page at http://localhost:8080/swagger-ui.html
10. You can also use [this Postman requests collection](https://www.getpostman.com/collections/28eb112cd9b89d0bc730) to send requests. Use [this guide](https://kb.datamotion.com/?ht_kb=postman-instructions-for-exporting-and-importing) to learn how to import request collections via link in postman
