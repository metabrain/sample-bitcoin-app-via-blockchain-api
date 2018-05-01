FROM openjdk:8u151

MAINTAINER Daniel Parreira <dany9995@gmail.com>

WORKDIR /home/metabrain/bitcoin-api/
RUN mkdir -p /home/metabrain/bitcoin-api/
COPY ./target/*.jar  /home/metabrain/bitcoin-api/bitcoin-api-via-blockchain.jar

CMD java -XX:MinHeapFreeRatio=3 -XX:MaxHeapFreeRatio=8 -Xmx512m -jar bitcoin-api-via-blockchain.jar com.metabrain.bitcoin.blockchain.Main
