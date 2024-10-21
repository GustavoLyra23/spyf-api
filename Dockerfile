FROM maven:3.9.4-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/spy-price-finder-0.0.1-SNAPSHOT.jar"]
