- 주키퍼 실행
- ./bin/windows/zookeeper-server-start.bat ./config/zookeeper.properties

<br>
- 카프카 실행
- ./bin/windows/kafka-server-start.bat ./config/server.properties

<br>

- 커넥트 실행
- ./bin/windows/connect-distributed ./etc/kafka/connect-distributed.properties

<br>
- 토픽 조회
  - ./bin/windows/kafka-topics.bat --bootstrap-server localhost:9092 --list
- 토픽 생성
  - ./bin/windows/kafka-topics.bat --bootstrap-server localhost:9092 --create --topic quickstart-events --partitions 1

<br>
- connect 실행 에러 해결
- log4j 오류는 connect-distributed.bat의 30번째 줄인 
- set KAFKA_LOG4J_OPTS=-Dlog4j.configuration=file:%BASE_DIR%/config/connect-log4j.properties를
- set KAFKA_LOG4J_OPTS=-Dlog4j.configuration=file:%BASE_DIR%/etc/kafka/connect-log4j.properties
- 로 바꾸니 해결

<br>
- jdbc 커넥터, mariadb 플러그인 에러
- connection.url에 mysql -> mariadb 로 변경
```
{
  "name": "my-sink-connect",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url": "jdbc:mariadb://127.0.0.1:3306/ecommerce",
    "connection.user": "root",
    "connection.password": "비번",
    "auto.create": "true",
    "auto.evolve": "true",
    "delete.enabled": "false",
    "tasks.max": "1",
    "topics": "orders",
    "insert.mode": "insert",
    "table.name.format": "orders"
  }
}
```

<br>
- 생성한 connect 지우는 엔드포인트
```
http://localhost:8083/connectors/my-sink-connect
```

<br>

- rabbmit mq

```
docker run -d --name rabbitmq --network ecommerce-network -p 15672:15672 -p 5672:5672 -p 15671:15671 -p 5671:5671 -p 4369:4369 -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:management
```

- config server

```
docker run -d -p 8888:8888 --network ecommerce-network -e "spring.rabbitmq.host=rabbitmq" -e "spring.profiles.actvice=default" --name config-service dgjinsu/config-service
```

- discovery service (유레카)

```
docker run -d -p 8761:8761 --network ecommerce-network -e spirng.cloud.config.uri=http://config-service:8888 --name discovery-service dgjinsu/discovery-service
```

- apigateway service

```
docker run -d -p 8000:8000 --network ecommerce-network -e spring.cloud.config.uri=http://config-servic
e:8888 -e spring.rabbitmq.host=rabbitmq -e eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka --name ap
igateway-service dgjinsu/apigateway-service
```

- mariadb

```
docker run -d --name my_mariadb --network ecommerce-network -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=ecommerce -p 13306:3306 mariadb
```
