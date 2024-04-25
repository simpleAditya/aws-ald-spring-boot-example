Spring Boot application integrated with Amazon Web Services given Gradle config.

API Gateway (endpoints) -> Lambda (execution) -> S3 (storage) -> Athena (analytics) i.e. Code jar should be present inside Amazon S3 Services are managed by Amazon Cloudformation.

Endpoints. /product (POST) - Save product /product (GET) - Get all products
