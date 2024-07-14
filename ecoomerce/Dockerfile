FROM confluentinc/cp-kafka-connect:7.0.1

# Confluent Hub에서 JDBC 커넥터 설치
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:latest

# MySQL JDBC 드라이버 다운로드 및 설치
ADD https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-8.0.28.tar.gz /tmp/
RUN tar -xzf /tmp/mysql-connector-java-8.0.28.tar.gz -C /tmp && \
    mv /tmp/mysql-connector-java-8.0.28/mysql-connector-java-8.0.28.jar /usr/share/confluent-hub-components/