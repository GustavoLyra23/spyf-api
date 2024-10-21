
FROM openjdk:21-jdk

WORKDIR /app

RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg && \
    install -o root -g root -m 644 microsoft.gpg /etc/apt/trusted.gpg.d/ && \
    sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/repos/edge stable main" > /etc/apt/sources.list.d/microsoft-edge.list' && \
    rm microsoft.gpg && \
    apt-get update && \
    apt-get install -y microsoft-edge-stable

RUN apt-get install -yqq unzip && \
    curl -o /tmp/edgedriver.zip https://msedgedriver.azureedge.net/115.0.1901.203/edgedriver_linux64.zip && \
    unzip /tmp/edgedriver.zip -d /usr/local/bin/ && \
    rm /tmp/edgedriver.zip

COPY . .

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/proxy-cache-server-1.0-SNAPSHOT.jar"]
