# aws-ald-spring-boot-example
Spring Boot application integrated with Amazon Web Services given Gradle config.

API Gateway (endpoints) -> Lambda (execution) -> Dynamodb (database)
i.e.
Code jar should be present inside Amazon S3
Services are managed by Amazon Cloudformation.

Endpoints.
/product (POST) - Save product
/product (GET) - Get all products
/product/{id} (GET) - Get product by id
