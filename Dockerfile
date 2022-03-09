FROM openjdk:11

COPY . .

RUN ./gradlew build -x test # skip tests

CMD ["./gradlew", "run"]