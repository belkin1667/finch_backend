# Backend part of social network for travelers *Finch*

### References
See Frontend Repository [here](https://github.com/artemgoncharov2000/finch-frontend) (React Native iOS app)  

### Reviews
* [Academic Adviser Review by Alexandrov D. V.](https://docdro.id/sebLfx4)

### Documentation
*GOST 19 (Russian Software Documentation Standard ГОСТ 19 ЕСПД) documentation*
1. [Explanatory Note](https://docdro.id/ZGbGGHS)
3. [Requirements. Backend](https://docdro.id/hd5If0w)
4. [Requirements. Backend & Frontend](https://docdro.id/B88Yi18)
5. [Testing Plan & Methodology. Backend](https://docdro.id/7o8ui1G)
6. [Testing Plan & Methodology. Backend & Frontend](https://docdro.id/aezeC4A)
7. [Programmer's Guide](https://docdro.id/aqDzXjp)
8. [Program Sources](https://docdro.id/3u5KqFF)

## Local Deployment Guide
1. You should use Java 11 to build and run this project
2. Install PostgreSQL 13 with username {dbusername} and password {dbpassword}
3. Create database with name {dbname}
```
> psql -U {dbusername}
# CREATE DATABASE {dbname};
```
4. Clone this repository
5. Open [application-local.properties](/src/main/resources/application-local.properties) file and configure your database:
```
spring.datasource.url=jdbc:postgresql://localhost/{dbname}
spring.datasource.username={dbusername}
spring.datasource.password={dbpassword}
```
6. Open [application.properties](/src/main/resources/application.properties) and set active profile to local:
```
spring.profiles.active=local
```
7. (Optional) Open [StartupRunner.java](/src/main/java/com/belkin/finch_backend/StartupRunner.java) and uncoment line 68 (`//generateMockData();`) - this will add some mock users and posts to the database (if you've generated them once, you should comment this line back to avoid collisions on app restart). Then create folder `/images/` in the projects root directory and extract [the archive](https://drive.google.com/file/d/1yVZ5cDrihbPl04S8nOkYzb_ApN8k_yv0/view?usp=sharing) there (do not change images names!)
8. Run following commands in project's root directory:
```
> ./gradlew clean
> ./gradlew bootRun
```
9. The application is now up and running. You can send your requests to `localhost:8080`
* You can access Swagger2 page at http://localhost:8080/swagger-ui.html
* You can also use [this Postman requests collection](https://www.getpostman.com/collections/28eb112cd9b89d0bc730) to send requests. Use [this guide](https://kb.datamotion.com/?ht_kb=postman-instructions-for-exporting-and-importing) to learn how to import request collections via link in postman
