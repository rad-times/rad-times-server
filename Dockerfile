FROM openjdk:22
WORKDIR /rad-times
COPY . .

COPY build/libs/rad-times-server-0.0.1.jar rad-times-server.jar

EXPOSE 3030
ENTRYPOINT ["java", "-jar", "rad-times-server.jar"]
