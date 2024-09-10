FROM openjdk:17


WORKDIR /app1
EXPOSE 8080
COPY ./target/apigateway.jar /app1
CMD [ "java","-jar","apigateway.jar" ]