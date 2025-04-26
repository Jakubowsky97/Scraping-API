# Etap 1: Budowanie projektu
FROM infotechsoft/maven:3.9.6-openjdk-17 AS build

# Ustaw katalog roboczy
WORKDIR /app

# Kopiuj pliki projektu
COPY . .

# Buduj projekt (bez testów)
RUN mvn clean package -DskipTests

# Etap 2: Finalny obraz aplikacji + Chrome + ChromeDriver
FROM openjdk:19-jdk-slim

# Katalog roboczy
WORKDIR /app

# Instalacja zależności i Chrome
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    curl \
    unzip \
    fonts-liberation \
    libappindicator3-1 \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
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

# Dodaj klucz i repo Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-linux-signing-keyring.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-linux-signing-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list

# Instalacja Chrome i ChromeDriver
RUN apt-get update && \
    apt-get install -y \
    google-chrome-stable \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Instalacja ChromeDriver dopasowanego do Chrome
RUN CHROME_VERSION=$(google-chrome-stable --version | awk '{print $3}' | cut -d '.' -f 1) && \
    DRIVER_VERSION=$(curl -s "https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions-with-downloads.json" | grep -A 10 "\"$CHROME_VERSION\"" | grep "chromedriver" | grep "linux64" | head -1 | cut -d '"' -f 4) && \
    wget -q "$DRIVER_VERSION" -O /tmp/chromedriver.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    chmod +x /usr/local/bin/chromedriver && \
    rm /tmp/chromedriver.zip

# Skopiuj zbudowany plik JAR
COPY --from=build /app/target/*.jar app.jar

# Otwórz port
EXPOSE 8080

# Start aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]
