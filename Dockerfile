# Usamos una imagen base de Java (JRE) que es más ligera para ejecución
FROM eclipse-temurin:17-jre-alpine

# Definimos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado por Maven desde la carpeta target local
# Usamos un comodín para que no importe si la versión cambia (ej: api-0.0.1-SNAPSHOT.jar)
COPY target/*.jar app.jar

# Exponemos el puerto que usa tu app (8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]