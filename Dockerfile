FROM amazoncorretto:17-alpine-jdk as base
RUN apk add maven
COPY . .

FROM base as build
RUN mvn package -Dmaven.skipTests

FROM amazoncorretto:17-alpine-jdk as production
COPY --from=build ./target/api_documentation-0.0.1-SNAPSHOT.jar ./api_documentation-0.0.1.jar
ENTRYPOINT ["java","-jar","./api_documentation-0.0.1.jar"]