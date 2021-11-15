###### Please consider the blow before you run the app:

    1- Docker agent is up and running 

    2- Port 8080 is free and available.(Otherwise, you can chang it in docker-compose file.)

###### There are two folders in the root directory:

1- **local-run** (The db would be _H2_)

###### You can simply run the application by running the run.sh file:

    sh run.sh

###### If you would like to skip the tests, simply choose the other one:

    sh run-skipTests.sh

2- **prod-run** (The db would be _Postgres_)

###### You can simply run the application by running the run.sh file:

    sh run.sh

###### If you would like to skip the tests:

    sh run-skipTests.sh

-----------------------------------------------

###### Then you can check the swagger url to see all the apis:

[http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config]()