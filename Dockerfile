# Use uma imagem base com o Debian (Ubuntu)
FROM openjdk:21-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Instalar dependências necessárias, Microsoft Edge e Edge WebDriver
RUN apt-get update && \
    apt-get install -y \
    curl \
    gnupg \
    apt-transport-https \
    ca-certificates \
    software-properties-common \
    unzip && \
    curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > /etc/apt/trusted.gpg.d/microsoft.gpg && \
    echo "deb [arch=amd64] https://packages.microsoft.com/repos/edge stable main" > /etc/apt/sources.list.d/microsoft-edge.list && \
    apt-get update && \
    apt-get install -y microsoft-edge-stable

# Baixar e instalar o Microsoft Edge WebDriver
RUN curl -o /tmp/edgedriver.zip https://msedgedriver.azureedge.net/115.0.1901.203/edgedriver_linux64.zip && \
    unzip /tmp/edgedriver.zip -d /usr/local/bin/ && \
    rm /tmp/edgedriver.zip

# Copia o seu código para o container
COPY . .

# Compila o projeto usando Maven
RUN ./mvnw clean package -DskipTests

# Comando para rodar a aplicação
CMD ["java", "-jar", "target/spy-price-finder-0.0.1-SNAPSHOT.jar"]

