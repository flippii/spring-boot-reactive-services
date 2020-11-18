# Getting started

You need Docker and Java 15 installed.
	
	docker-compose up -d

Then run every Spring Boot app.

    ./mvn spring-boot:run

# TODO
* Logging:
  * Mongo DB
  * webflux
* MessageService -> Translations  
* ReactiveMongoOperations umstellen oder @Query nutzen
  * Article -> Tags Aggregation nutzen
* Web-Client zur Kommunikation zw. den Services?
  * Feed Service macht den call zum Article Service -> Details
  * Basic Auth
* Service Discovery - Consul + Config Management  
* OAuth2 Security einziehen
  * zuerst mit Okta - dann auf Keycloak umstellen.
  * Messaging Security
  * Prüfung ob der Benutzer den Article löschen, bearbeiten ... kann
* Mongo DB 4.x 
  * Transactions
  * Index in Modellen nutzen
* Reactive Unit Tests implementieren
* ReactiveUserDetailsPasswordService -> Wo init. wird es ind er Auto Configuration?
* JWT Token ist abgelaufen - Exception nicht zusätzlich tum Token invalid Header 
* Prüfung das beim Löschen von Datensätzen auch Abhängige Services alles löschen
* Properties - Bean validation
*Article Slug über Kafka nicht al Identifier
