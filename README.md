# microservice-patterns

May work it out if I have time...

tl;dr - Implementation of the "Idempotency + Distributed Saga + Transactional Outbox" idea in clojure

## Infra and Components

- Debezium for CDC and transactional outbox. [Tutotial](https://debezium.io/documentation/reference/stable/tutorial.html) [Docker-compose for testing](https://github.com/debezium/debezium-examples/tree/main/tutorial)
- A server to submit the initial transaction triggering the saga
- A worker to scan Kafka topic and perform tasks
- A websocket server (sente?) to let end user get updated status

## Libraries (to be) used

- DB: Currently the old `clojure.java.jdbc`. Hopefully use `toucan` later. (DB Schema?)
- Kafka: Use raw/official kafka Java library
- Websocket etc: `sente`
