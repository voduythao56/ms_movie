# ms_movie

### Instruction:
1. Open ms_movie/movie-swagger.yml using Swagger Editor (https://editor.swagger.io/) to get details about all apis
   ![swagger](https://github.com/voduythao56/ms_movie/assets/90848380/f41ffb94-f98f-441e-8296-bb914ebb7706)

### How to run application:
1. Make sure your local already install docker
2. run: **cd ms_movie**
3. run: **./gradlew clean build** to make sure everything ok
   - if we got **permission denied: /.gradlew** then grant permission for ./gradlew (run: **chmod +x gradlew**)
4. run: **docker compose up -d**
   
![docker-compose-up](https://github.com/voduythao56/ms_movie/assets/90848380/6a674bb6-c344-47b5-b084-c4dbfd0360e5)

![container](https://github.com/voduythao56/ms_movie/assets/90848380/87a7fe76-07db-42e8-86bc-b263ec2c5392)




### Testing
1. Import ms_movie/Movie Management.postman_collection.json to POSTMAN for testing
![postman](https://github.com/voduythao56/ms_movie/assets/90848380/5b944e4f-624a-4110-b327-f639f1b9592d)

2. Example:

![GetList](https://github.com/voduythao56/ms_movie/assets/90848380/6aaee99f-d4f6-4b10-a6d0-bbac45d9ecc2)
![Delete](https://github.com/voduythao56/ms_movie/assets/90848380/fdcdc6cc-94b2-4893-8a9e-5b45e695471d)
![Create](https://github.com/voduythao56/ms_movie/assets/90848380/991d449b-093e-4eeb-80d6-f3db7c7d79e8)
![Update](https://github.com/voduythao56/ms_movie/assets/90848380/e7848609-13cc-4719-93e3-6aa506fad65f)
![Get](https://github.com/voduythao56/ms_movie/assets/90848380/2f5d69f6-611a-41eb-91fe-d62d18e66635)
