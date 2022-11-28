## HTTP Methods With curl for MealRestController

### get
`curl -X GET http://localhost:8080/topjava/rest/profile/meals/100009`

### delete
`curl -X DELETE http://localhost:8080/topjava/rest/profile/meals/100009`

### getAll
`curl -X GET http://localhost:8080/topjava/rest/profile/meals`

### createWithLocation
`curl -X POST -d '{"dateTime": "2022-11-27T21:00:00", "description": "Ужин", "calories": 500}' -H 'Content-Type: application/json; charset=UTF-8' http://localhost:8080/topjava/rest/profile/meals`

### update
`curl -X PUT -d '{"dateTime": "2022-11-27T21:00:00", "description": "Ужин", "calories": 490}' -H 'Content-Type: application/json; charset=UTF-8' http://localhost:8080/topjava/rest/profile/meals/100009`

### getBetween
`curl -X GET http://localhost:8080/topjava/rest/profile/meals/filter?startDate=2020-01-30&startTime=&endDate=2020-01-30&endTime=`
