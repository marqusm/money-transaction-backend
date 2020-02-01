# money-transaction-backend

Here I would like to provide more information about the thinking process while implementing the service.
Since the requirements are to stay as simple as possible, I decided to go without a database, so I am keeping everything in memory.

## Dependencies

The project was written using Java 11. I picked that version since the memory footprint is smaller.
Speaking about features, I was using those that belong up to Java 9.
Speaking about REST implementation, I have chosen Java Spark since I've been told you are using that one and I liked the framework very fast.
DI is done by Google Juice, as I got the info you are doing the same way.
Gson is responsible for JSON (de)serialization.
About the other dependency, I tried to be as light as possible.
The project is build using Gradle as it is almost a standard nowadays.
I am using Lombok to reduce boilerplate.
This was a bit hard decision since I got a feeling you prefer to go as native as possible.
But my experience with Lombok is super positive so I decided to use it.
There is another argument - why not using Kotlin then?
It makes sense completely, but I am not fluent with Kotlin yet, so I went Java/Lombok way.
For testing, I am using JUnit5 (since it has some small improvements comparing to JUnit4) and RestAssured to test API directly.
Your request was to have integration and unit tests. Here, I am using the following approach.
I am trying to cover as much as possible using API level testing.
The corner-cases that remain as well as some important business logic details I like to cover with unit tests.
Also, I am trying to keep coverage as high as possible, but not only in terms of covered lines but also in covering business cases and edge cases.
Formatting is google-java-format.
Plugin spotless is responsible to assure no build is possible if code is not properly formatted.
Shadow plugin is responsible for creating a fat jar, so we can run a service with all dependency included.

The project can be built (assuming you are in project's root folder) using command
`./gradlew clean build` and run using the command
`java --add-opens=java.base/java.lang=ALL-UNNAMED -jar build/libs/money-transaction-backend-all.jar`.
Can be started using Gradle application plugin, as well - `./gradlew run` 
VM parameter has to be specified since Java Spark and Java 11 are not in good relations so far.
Looks like the fix will come in the recent future, but as of now, it is necessary.

Let's speak about the implementation of a transaction between two accounts.
Since the idea is to go small, I decided not to have anything unnecessary.
So no currency, no date of the transaction. Only id and amount.
I was thinking about how to make it be as performant as possible, having in mind data consistency should not be compromised.
Idea was to lock the part of code when we are communicating to the database.
I wanted to avoid locking all the parallel requests, so I ended up with the idea that I lock only two accounts involved in a transaction.
I did that using double synchronize methods.
That can produce deadlock, so I am sorting two values before locking, making sure the order is always the same, in order to avoid a deadlock.
Since I have not been involved in this problem very deep recently, I was investigating what is Postgres behavior in this scenario (since you are using it as a database).
I found the fact that open transactions would lock the rows that had been read.
That makes really handy as we can read two account rows we want to perform a transaction on, then create a row in the transaction table, write new, updated values in two account rows we read already and commit the transaction.
In that way, we should be able to make looking with minimal impact on the performance.
Since in my case I am not using a database, this scenario does not work, but from my point of view, performance should be very similar.
One more fact - this solution is not scalable.
Once we would like to scale services, internal locking is not enough, we have to have an external one.
Having one instance of Postgres is fine, we can lock it on a database level.
But if we go with sharding databases, the problem can become much larger.

Project code is organized having in mind that one class should have one responsibility.
There is ApiConfig as an entry point, creating routes.
Routes methods are defined in controller classes that are responsible for taking data from the request and call service methods.
Service classes contain business logic, without having any idea about Rest API.
An independent code is placed in the util package.
Non-changeable data belong to the constant class.
Specific exceptions are defined in the exception package.
Particularly interesting is the model package where all models are defined.
Usually, I prefer having separate model classes for database mapping (ORM or record) and classes for transfer data between a server and a client (DTO).
I do that to separate data structure from both worlds and to have a clearer view of which data belongs where.
But, there is a big "but" here - I could not find any argument to make it separate and not looking almost the same.
And you requested to be as simple as possible, without overengineering.
So I decided to go with the same model both for a repository (ORM) and DTO classes.

From a code style perspective, I like following SOLID principles.
Here I have to add a comment. Speaking about the "interface" part of SOLID, I don't like blindly follow the principle.
The idea is to have an interface and an implementation separated.
Gain is that it's easy to have replacement implementation of an existing interface and it's also easy to see the main responsibilities of the interface (having listed all the methods).
From my experience, sometimes it can be an overengineering since we usually have small classes with a single responsibility, where it's super easy to understand the implementation and see all the methods it is supporting.
Usually, it never happens we have more than one implementation.
So in this scenario, having interface looks like overengineering to me, introducing complexity with no good reason.
My stand here is to keep classes separated and small.
If it happens that class grows up or that we need another implementation, I am creating an interface immediately (it's easy with today's IDEs).
Until then, I am keeping class as it is.
In this code sample, you can see interface defined for JSON (de)serialization, since I have an idea to replace it with Jackson and measure performance.

## Commands
Start database

`docker-compose up`

Run Flyway initial migration

`gradlew flywayMigrate`

Generate JOOQ models

```gradlew generateSqlJooqSchemaSource```

