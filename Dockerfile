# Usa imagem do JDK 21
FROM eclipse-temurin:21-jdk

# Define o diretório de trabalho
WORKDIR /app

# Copia o jar gerado pelo Maven para dentro do contêiner
COPY target/rpe-api-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que sua aplicação usa
EXPOSE 8081

# Comando para rodar o jar
ENTRYPOINT ["java", "-jar", "app.jar"]
