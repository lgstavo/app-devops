FROM maven:3.9-eclipse-temurin-17 AS builder

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# Copia primeiro o pom.xml para aproveitar o cache de camadas do Docker.
# Se as dependências não mudarem, o Docker não as baixará novamente.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código fonte do projeto.
COPY src ./src

RUN mvn package -DskipTests


FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# Copia APENAS o arquivo .jar gerado do estágio "builder" para a nossa imagem final.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta 8080, na qual a aplicação Spring Boot roda.
EXPOSE 8080

# O comando para iniciar a aplicação quando o contêiner for executado.
ENTRYPOINT ["java", "-jar", "app.jar"]