# dss-assignment-3
# gRPC-Gradle-MongoDB-RabbitMQ

1. [Abishek Arumugam Thiruselvi](https://www.abishekarumugam.com)('40218896')
2. Srinidhi Honakre Srinivas

# Running the Application in IntelliJ IDEA
#### Prerequisites: IDE plugins Buildship Gradle Integration 3.0, Spring Tools 3, Git, Java 17 or greater, Maven 3.9 or higher and Gradle 7.6 or higher.

1. Open Git Perspective in IntelliJ IDEA and clone this [repository]([https://github.com/abishekat/dss-assignment-3](https://github.com/abishekat/dss-assignment-3))

2. Import the project into project explorer using git url.

3. Open the gradle tab 

   ```shell
   dss-assignment-2 -> Others -> generateProto
   ```

4. Run spring application from src->main.java.cu.dssassignment2.mongospringutil->DssAssignment3Application

5. To run the RabbitMQ listener. Open the terminal in IntelliJ IDEA and perform the following commands.

   ```shell
   cd rabbitlistener
   mvn clean package
   cd target
   java -jar rabbitlistener-0.0.1-SNAPSHOT.jar
   ```
  
6. Create application in run configuration of client. Use the main args as q1 to q5 and run.

7. The result will be found in rabbitlistener terminal.

# AmazonMQ login
  ```shell
   URL: https://b-cdef04c3-e248-40e2-9f6f-0427eb231362.mq.us-east-1.amazonaws.com/
   username: aarumugam
   password: guest1234567
   ```

## RESULTS

#### Structure
![](src/main/resources/images/1.png)

#### Higher Layer Architecture
![](src/main/resources/images/2.png)

#### Queriying and saving the result in new collections
![](src/main/resources/images/3.png)
![](src/main/resources/images/4.png)
![](src/main/resources/images/5.png)
![](src/main/resources/images/6.png)
![](src/main/resources/images/7.png)


### UNIVERSITY

 [CONCORDIA UNIVERSITY](https://www.concordia.ca/).
  
