# Getting started

This app uses a compose.yaml to build the underlying Postgres DB.
The default credentials are (shouldn't need them, as the app should start fine due to the docker-compose gradle dependency)
- `POSTGRES_DB=postgres`
- `POSTGRES_USER=postgres`
- `POSTGRES_PASSWORD=secretpwd123`

This app uses CurrencyBeacon for accessing exchange rates between currencies. 
You need an API key for this: https://currencybeacon.com (registration with Google/GitHub is available for quick access)
<br>
The API key needs to be set as an environment variable `CURRENCY_BEACON_KEY` (or just modify the application.yaml).

This project uses Liquibase for the Database Migrations. Running the migrations adds some clients and accounts for testing.

There is a swagger endpoint available at http://localhost:8080/swagger-ui/index.html. 
<br>
The transfer endpoint has a prefilled example for convenience.