# Currency API

## Setup
* Java 8
* Spring Boot 2.3.5
* Maven

## Overview
Currency API allows you to perform some basic operations with currencies
* get exchange rate for popular currencies
* convert currencies
* get links to charts of currency pairs

The currency data is being populated on application starup and updated every midnight.

Currency data is coming from European Central Bank website: https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html
Currency charts are there: https://www.xe.com/currencycharts/

## Usage
* Get ECB reference rate(base currency is EUR) for a currency: ```GET /api/v1/currencies/reference-rate?cur=USD```
* Get exchange rate for a currency pair: ```GET /api/v1/currencies/exchange-rate?from=USD&to=RUB```
* Get a list of available currencies with their frequencies ```GET /api/v1/currencies/statistic```
* Get convert money from one currency to another one: ```GET /api/v1/currencies/convert?from=USD&to=SGD&amount=66```
* Get a link to a public website showing an interactive chart for a given
currency pair: ```GET /api/v1/currencies/chart?from=GBP&to=RUB```

## Running all tests locally
```mvn clean test```

## Running docker image locally
* ```docker build -t currency-api .``` The project will be build, tested and docker image will be created
* ```docker run -p 8080:8080 currency-api``` Run project locally on ```8080``` port
