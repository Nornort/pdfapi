FROM maven AS build

WORKDIR /app
COPY pom-frozen.xml pom.xml
RUN mvn dependency:go-offline
COPY pom.xml pom.xml
RUN mvn dependency:resolve
COPY src/main src/main
COPY src/test src/test
RUN mvn package -DskipTests

FROM java

WORKDIR /app
COPY --from=build /app/target/*.jar /app

CMD java -jar /app/*.jar com.philippevienne.pdfapi.PdfApiApplication
