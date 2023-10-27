# ms_movie

# Table of Contents
1. [Instruction](#1-instruction)
2. [How to run application](#2-how-to-run-application)
3. [Non Functional Testing](#3-non-functional-testing)
4. [Functional Testing](#4-functional-testing)


### 1. Instruction:
1. Open **ms_movie/movie-swagger.yml** using Swagger Editor (https://editor.swagger.io/) to get details about all apis
   ![swagger](https://github.com/voduythao56/ms_movie/assets/90848380/f41ffb94-f98f-441e-8296-bb914ebb7706)

### 2. How to run application:
1. Make sure your local already install docker
2. run: **cd ms_movie**
3. run: **./gradlew clean build -x test** to make sure everything ok. (Exclude test because it may take time to pull MySQL image. We can include this in Non Functional testing part)
   - if we got **permission denied: /.gradlew** then grant permission for ./gradlew (run on macos for grant permission: **chmod +x gradlew**)
4. run: **docker compose up -d**
   
![docker-compose-up](https://github.com/voduythao56/ms_movie/assets/90848380/6a674bb6-c344-47b5-b084-c4dbfd0360e5)

![container](https://github.com/voduythao56/ms_movie/assets/90848380/87a7fe76-07db-42e8-86bc-b263ec2c5392)

![running](https://github.com/voduythao56/ms_movie/assets/90848380/8ec7429a-49e1-42c9-86e6-d6810c7f86c2)

5. ![#f03c15](https://placehold.co/15x15/f03c15/f03c15.png) ONLY REFER if we got issue related to PORT conflict on docker
      + Open **ms_movie/.env**
      + Change port for 2 value: **MYSQLDB_LOCAL_PORT** and **SPRING_LOCAL_PORT**
        
         Example:
      
         ![PORT](https://github.com/voduythao56/ms_movie/assets/90848380/e5ecdb98-0fe2-4193-b4b5-9b125c222507)
         ![change port](https://github.com/voduythao56/ms_movie/assets/90848380/f0dbe9b6-46b4-4ca0-91fb-b335aaa36743)

### 3. Non Functional Testing
1. Unit test (src/test/java/com/assessment/movie/unit)
2. Integration test using testcontainers (Integrate with MySQL DB container) (src/test/java/com/assessment/movie/integration)
3. Running
   - Run: **cd ms_movie**
   - Run: **./gradlew test** (or **./gradlew clean build**)

### 4. Functional Testing
1. Import **ms_movie/Movie Management.postman_collection.json** to POSTMAN for testing
![postman](https://github.com/voduythao56/ms_movie/assets/90848380/5b944e4f-624a-4110-b327-f639f1b9592d)

2. Example:

![GetList](https://github.com/voduythao56/ms_movie/assets/90848380/6aaee99f-d4f6-4b10-a6d0-bbac45d9ecc2)
![Delete](https://github.com/voduythao56/ms_movie/assets/90848380/fdcdc6cc-94b2-4893-8a9e-5b45e695471d)
![Create](https://github.com/voduythao56/ms_movie/assets/90848380/991d449b-093e-4eeb-80d6-f3db7c7d79e8)
![Update](https://github.com/voduythao56/ms_movie/assets/90848380/e7848609-13cc-4719-93e3-6aa506fad65f)
![Get](https://github.com/voduythao56/ms_movie/assets/90848380/2f5d69f6-611a-41eb-91fe-d62d18e66635)
