## RUVIK SERVER
[![Build Status](https://app.travis-ci.com/CrissNamon/ruvik-server.svg?branch=master)](https://app.travis-ci.com/CrissNamon/ruvik-server)
<br>
Server app for **Ruvik** messenger, uses:
+ Spring Boot (Web, Security, Data JPA)
+ Postgres DB
+ STOMP over WebSocket

**Ruvik** - messenger, demonstrating implementation of Ruvik Proto, end-to-end encryption protocol.
<br>https://github.com/CrissNamon/ruvik-proto-java

<br>To run in docker container use command:
<br>
<code>
docker-compose up -d --build
</code>

<br>Protocol scheme:
<br>
![Scheme](https://github.com/CrissNamon/ruvik-proto-java/blob/master/RuvikProtoENG.jpg)
