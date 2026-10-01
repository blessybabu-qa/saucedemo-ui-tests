FROM mcr.microsoft.com/playwright/java:v1.52.0-noble

WORKDIR /tests
COPY . .

CMD ["mvn", "-B", "test"]