# Helio REST API


| Endpoint | Method | Description |
|--|--|--|
| `/api/`  |  `GET` | Returns the list of data endpoints provided by the service|
| `/api/:id`  |  `POST` | Creates a translation task that using the mapping sent in the `body` of this request  |
| `/api/:id`  |  `DELETE` | Deletes the translation task with the provided `:id`  |
| `/api/:id/mapping`  |  `GET` | Returns the mapping registered for the translation task associated to the `:id`  |
| `/api/:id/data`  |  `GET` | Returns the translated data as a result of the translation task  |
| `/configuration`  |  `GET` | Returns the configuration of this service  |
| `/configuration`  |  `POST` | Modifies the configuration of the service with the one sent in the `body` of this request  |
| `/component`  |  `GET` | Returns the list of components registered in the service that can be used in the mappings  |
| `/component`  |  `POST` | Register the component sent in the `body` of this request   |
| `/component`  |  `DELETE` | Deletes the component with the provided `:id`  |
