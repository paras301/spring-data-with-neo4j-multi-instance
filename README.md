# spring-data-with-neo4j-multi-instance

The goal of this project is to configure multiple Neo4j database instance and use them with Springboot. I see a lot of features are available, but I have tried to do this with minimal configuration.

One of the use case that I encountered was to store metadata in one central Neo4j instance and use that metadata to apply mapping to incoming data store in the Neo4j (data instance).

Another use case is when you have multiple Neo4j clusters and you want to build an app to combine data across two and prepare a report.

The project takes an example of Customer order processing via REST calls.

The customer and orders with lineitems are created in Neo4j in a tree structure.

I am using cypher queries to process the data which is kind of old way, but this is faster as compared to OGM because internally Neo4j driver executes a lot of cyphers for each operation. Follow the documentation and story here: https://paras301.medium.com/neo4j-data-processing-with-springboot-with-multiple-database-instance-3bef6e9a477d
