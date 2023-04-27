# Helio REST API

# Quickstart

Using docker run docker-compose with the following recipe
````yml
version: '3'
services:
  helio-publisher:
    image: acimmino/helio-rest:latest
    volumes: 
      - ./local/:/usr/src/app/local/
      - ./db/:/usr/src/app/db/
    ports:
      - "4567:4567"
````
It creates two folders, one with the database containing all the mappings and configuration so security copies can be made and, another, that is empty in case some local files are required for the mappings.

Using java download the [latest released version](https://github.com/helio-ecosystem/helio-rest/releases) and run the script `run.sh`


# API
| Endpoint | Method | Description |
|--|--|--|
| `/api/`  |  `GET` | Returns the list of data endpoints provided by the service|
| `/api/:id`  |  `POST` | Creates a translation task that using the mapping sent in the `body` of this request. You can use a specific mapping compiler (BUILDER component) using the URL parameter ?builder=[BUILDER_NAME] |
| `/api/:id`  |  `DELETE` | Deletes the translation task with the provided `:id`  |
| `/api/:id/mapping`  |  `GET` | Returns the mapping registered for the translation task associated to the `:id`  |
| `/api/:id/data`  |  `GET` | Returns the translated data as a result of the translation task  |
| `/component`  |  `GET` | Returns the list of components registered in the service that can be used in the mappings  |
| `/component`  |  `POST` | Register the component sent in the `body` of this request   |
| `/component`  |  `DELETE` | Deletes the component with the provided `:id`  |

# Settings

Several features of the service can be set up using arguments
* `--port=` is used to change the default service port 4567
* `--persistence=` is used to specify a different database (or change its location)
* `--components=` is used to specify where the default components are read (default is file `default-components.json`)
* `--default_builder=` is used to specify the default mapping builder used for compiling the mappings (default is `SIoTRx`)
