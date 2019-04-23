Build a local proxy for getting Currency Exchange Rates
A local proxy for Forex rates
Forex is a simple application that acts as a local proxy for getting exchange rates. It's a service that can be consumed by other internal services to get the exchange rate between a set of currencies, so they don't have to care about the specifics of third-party providers.

Allowed currencies: AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD

1. fire Rate request with dummy implementation ~write first test
2. write a client to 1Forge
3. rates source as a separate entity with already cached 1Forge
4. refactor and improve API feedback for bad scenarios

# Usage
To run the tests:
```
sbt test
```
To start the service:
```
sbt run
```
# Endpoints

Ask for a rate for pair of Euro and Yen:
```bash
curl "http://localhost:8888/?from=USD&to=JPY"
```
Response:
```
{"from":"USD","to":"JPY","price":111.913,"timestamp":"2019-04-23T06:03:04Z"}
```

Returned time is in the UTC zone with seconds precision.

If server gots a problem with synchronizing with 1Forge you'll receive:
```
 500 Internal Server Error
```
"Proxy error.Service malfunction. Rate too old. Last known Rate: Rate(Pair(USD,JPY),Price(1),Timestamp(2019-04-23T06:03:04Z))"

