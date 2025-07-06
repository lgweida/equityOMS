 curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{
    "clOrdId": "ORD-005",
    "symbol": "TSLA",
    "side": "1",
    "transactTime": "2025-07-03T11:00:00",
    "orderQty": 200.0,
    "ordType": "1",
    "timeInForce": "DAY",
    "account": "ACC-005"
}'

curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{
    "clOrdId": "ORD-006",
    "symbol": "NVDA",
    "side": "2",
    "transactTime": "2025-07-03T11:05:00",
    "orderQty": 150.0,
    "ordType": "2",
    "price": 800.0,
    "timeInForce": "GTC",
    "account": "ACC-006"
}'

curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{
    "clOrdId": "ORD-007",
    "symbol": "AMZN",
    "side": "2",
    "transactTime": "2025-07-03T11:10:00",
    "orderQty": 80.0,
    "ordType": "1",
    "timeInForce": "IOC",
    "account": "ACC-007"
}'

curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{
    "clOrdId": "ORD-008",
    "symbol": "META",
    "side": "1",
    "transactTime": "2025-07-03T11:15:00",
    "orderQty": 120.0,
    "ordType": "2",
    "price": 350.0,
    "timeInForce": "GTD",
    "account": "ACC-008"
}'

curl -X POST http://localhost:8081/api/executions -H "Content-Type: application/json" -d '{
    "execId": "EXEC-005",
    "clOrdId": "ORD-005",
    "orderId": "5",
    "execType": "F",
    "execTransType": "0",
    "symbol": "TSLA",
    "lastQty": 100.0,
    "lastPx": 250.0,
    "leavesQty": 100.0,
    "cumQty": 100.0,
    "avgPx": 250.0,
    "side": "1",
    "ordStatus": "1",
    "transactTime": "2025-07-03T11:01:00"
}'

curl -X POST http://localhost:8081/api/executions -H "Content-Type: application/json" -d '{
    "execId": "EXEC-006",
    "clOrdId": "ORD-006",
    "orderId": "6",
    "execType": "F",
    "execTransType": "0",
    "symbol": "NVDA",
    "lastQty": 150.0,
    "lastPx": 800.0,
    "leavesQty": 0.0,
    "cumQty": 150.0,
    "avgPx": 800.0,
    "side": "2",
    "ordStatus": "2",
    "transactTime": "2025-07-03T11:06:00"
}'

curl -X POST http://localhost:8081/api/executions -H "Content-Type: application/json" -d '{
    "execId": "EXEC-007",
    "clOrdId": "ORD-007",
    "orderId": "7",
    "execType": "F",
    "execTransType": "0",
    "symbol": "AMZN",
    "lastQty": 80.0,
    "lastPx": 180.0,
    "leavesQty": 0.0,
    "cumQty": 80.0,
    "avgPx": 180.0,
    "side": "2",
    "ordStatus": "2",
    "transactTime": "2025-07-03T11:11:00"
}'

curl -X GET http://localhost:8081/api/orders | jq
