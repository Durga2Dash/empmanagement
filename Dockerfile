# For Java 8, try this
FROM openjdk:8-jdk-alpine

# Setting the data-management artifact path to a variable
ARG JAR_FILE=target/empmanagement-0.0.1-SNAPSHOT.jar

# Setting the working directory path
WORKDIR /opt/app

# Copying the data-management artifact to override the app.jar
COPY ${JAR_FILE} app.jar

# Starting the application
ENTRYPOINT ["java","-jar","app.jar"]

# Exposing the application port with embedded tomcat running
EXPOSE 8010
