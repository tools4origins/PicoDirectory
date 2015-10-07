# Pico Directory

A simple REST application using Spark Framework to store name and mail of some users

Project made for a scholar course

Base URL is `localhost:4567`

Possibles routes are :

> GET "/Directory/" - list all the users (XML Format)
 This route was not requested but added to show to power of spark and lambdas

> GET "/Directory/:email" - get information of a user selected by mail (XML Format)

> PUT "/Directory/" - add a user (requested parameters : name and a valid email)

> DELETE "/Directory/:email" - Delete a user selected by mail


You can download the dependencies and build the jar using :
> `mvn compile`

You can then launch the server using :
> `mvn exec:java`

You can also do both in one command :
> `mvn compile exec:java`