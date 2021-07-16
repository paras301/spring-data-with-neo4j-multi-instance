FROM openjdk:14-jdk-alpine

ARG SPRING_PROFILE_ENV
ENV SPRING_PROFILE_ENV=$SPRING_PROFILE_ENV

COPY build/libs/spring-data-with-neo4j.jar /

EXPOSE 8080

CMD java -jar -Dspring.profiles.active=${SPRING_PROFILE_ENV} /spring-data-with-neo4j.jar