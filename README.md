# Backend part of social network for travelers *Finch*
Belkin Mike (Backend) & Aretm Goncharov (Frontend) Software Engineering Course Work at National Research University Higher School of Economics, Faculty of Computer Science  
Moscow, Russia
2021

See Frontend Repository [here](https://github.com/artemgoncharov2000/finch-frontend) (React Native iOS app)

# Local Deployment Guide
1. Install PostgreSQL 13 with username {dbusername} and password {dbpassword}
2. Create database with name {dbname}
```
> psql -U {dbusername}
# CREATE DATABASE {dbname};
```
3. Clone this repository
4. Open [application-local.properties](/src/main/resources/application-local.properties) file and configure your database:
```
spring.datasource.url=jdbc:postgresql://localhost/{dbname}
spring.datasource.username={dbusername}
spring.datasource.password={dbpassword}
```
5. Open [application.properties](/src/main/resources/application.properties) and set active profile to local:
```
spring.profiles.active=local
```
6. (Optional) Open [StartupRunner.java](/src/main/java/com/belkin/finch_backend/StartupRunner.java) and uncoment line 68 (`//generateMockData();`) - this will add some mock users and posts to the database (if you've generated them once, you should comment this line back to avoid collisions on app restart). Then create folder `/images/` in the projects root directory and extract [the archive](https://drive.google.com/file/d/1yVZ5cDrihbPl04S8nOkYzb_ApN8k_yv0/view?usp=sharing) there (do not change images names!)
7. Run following commands in project's root directory:
```
> ./gradlew clean
> ./gradlew bootRun
```
8. The application is now up and running. You can send your requests to `localhost:8080`
* You can access Swagger2 page at http://localhost:8080/swagger-ui.html
* You can also use [this Postman requests collection](https://www.getpostman.com/collections/28eb112cd9b89d0bc730) to send requests. Use [this guide](https://kb.datamotion.com/?ht_kb=postman-instructions-for-exporting-and-importing) to learn how to import request collections via link in postman
