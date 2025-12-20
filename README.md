**Project Overview**

Worldwide Windsurfer’s Weather Service is a Spring Boot application written in Java 25 that exposes a REST API
for determining the best windsurfing location worldwide for a given day, based on weather forecast data.

The application uses the Weatherbit 16-day Forecast API as a data source and evaluates predefined windsurfing
locations using domain-specific rules such as wind speed and temperature.

**Features:**
- Best Location Selection: Evaluates multiple predefined windsurfing locations and selects the best one for a given day.
- Weather-Based Rules: Filters locations based on acceptable wind speed and temperature ranges.
- Scoring Algorithm: Calculates a score using wind speed and average temperature to determine the optimal location.
- REST API: Exposes a single endpoint to retrieve the best location for a selected date.

**Tech/framework used**

- Java 25
- Spring Boot
- JUnit 5
- Mockito
- MapStruct
- Maven
- Lombok

**Installation and Running**

1. Clone the repository:

```bash
git clone https://github.com/krzysztofKolodziej/windsurf-weather-service.git
cd windsurf-weather-service
```

2. Configure Weatherbit API key (environment variable):

&nbsp; The application expects the Weatherbit API key to be provided as an environment variable:
```bash
export WEATHERBIT_API_KEY=your_api_key_here
```
&nbsp; On Windows (PowerShell):

```bash
$env:WEATHERBIT_API_KEY="your_api_key_here"
```

3. Build the project

```bash
mvn clean install
```

4. Run the application

```bash
mvn spring-boot:run
```
<br>

**API Endpoints**

GET /api/v1/best-location

Returns the best windsurfing location for a given day.

Query Parameters

day – date in YYYY-MM-DD format

Example Request
```bash
curl "http://localhost:8080/api/v1/best-location?day=2026-01-01"
```

Example Success Response (200 OK)
```
{
  "locationName": "Bridgetown",
  "date": "2026-01-01",
  "averageTemperatureC": 12.0,
  "windSpeedMs": 10.0,
  "score": 42.0
}
```

**Testing**

Run the tests using Maven:

```bash
mvn test
```