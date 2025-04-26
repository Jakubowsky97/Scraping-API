# Etap 1: Build
FROM infotechsoft/maven:3.9.6-openjdk-17 AS build

# Ustaw katalog roboczy
WORKDIR /app

# Kopiuj pliki projektu
COPY . .

# Budowanie projektu (bez testów)
RUN mvn clean package -DskipTests

# Etap 2: Runtime
FROM openjdk:19-jdk-slim

# Ustaw katalog roboczy
WORKDIR /app

# Instalacja zależności systemowych + Chrome + ChromeDriver
ENV CHROMEDRIVER_VERSION 124.0.6367.78
ENV CHROME_VERSION 124.0.6367.78-1

RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    gnupg2 \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdbus-1-3 \
    libgdk-pixbuf2.0-0 \
    libnspr4 \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    xdg-utils \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Instalacja Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable=$CHROME_VERSION --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Instalacja ChromeDriver
RUN wget -O /tmp/chromedriver.zip https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/${CHROMEDRIVER_VERSION}/linux64/chromedriver-linux64.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    mv /usr/local/bin/chromedriver-linux64/chromedriver /usr/local/bin/chromedriver && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf /tmp/chromedriver.zip

# Skopiuj zbudowany plik JAR
COPY --from=build /app/target/*.jar app.jar

# Otwórz port
EXPOSE 8080

# Start aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]
