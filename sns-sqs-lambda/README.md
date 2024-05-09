# sns-sqs-lambda
Spring Boot application integrated with Amazon Web Services given Gradle config.

S3 (event notification) -> SNS -> SQS -> Lambda
i.e.
Code jar should be present inside Amazon S3
Services are managed by Amazon Cloudformation.

SNS access policy.

{
"Version": "2008-10-17",
"Id": "__default_policy_ID",
"Statement": [
{
"Sid": "__default_statement_ID",
"Effect": "Allow",
"Principal": {
"Service": "s3.amazonaws.com"
},
"Action": "SNS:Publish",
"Resource": "arn:aws:sns:ap-south-1:211125576450:NagarroTopic",
"Condition": {
"StringEquals": {
"aws:SourceAccount": "211125576450"
},
"ArnLike": {
"aws:SourceArn": "arn:aws:s3:*:*:product-amway-aditya"
}
}
},
{
"Sid": "sqs_statement",
"Effect": "Allow",
"Principal": {
"Service": "sqs.amazonaws.com"
},
"Action": "sns:Subscribe",
"Resource": "arn:aws:sns:ap-south-1:211125576450:NagarroTopic",
"Condition": {
"ArnEquals": {
"aws:SourceArn": "arn:aws:sqs:ap-south-1:211125576450:NagarroQueue"
}
}
}
]
}

SQS access policy.

{
"Version": "2008-10-17",
"Id": "__default_policy_ID",
"Statement": [
{
"Sid": "Stmt1234",
"Effect": "Allow",
"Principal": {
"Service": "lambda.amazonaws.com"
},
"Action": [
"sqs:ReceiveMessage",
"sqs:sendMessage"
],
"Resource": "arn:aws:sqs:ap-south-1:211125576450:NagarroQueue",
"Condition": {
"ArnEquals": {
"aws:SourceArn": "arn:aws:lambda:ap-south-1:211125576450:NagarroLambda"
}
}
},
{
"Sid": "Stmt12345",
"Effect": "Allow",
"Principal": {
"AWS": "*"
},
"Action": "sqs:SendMessage",
"Resource": "arn:aws:sqs:ap-south-1:211125576450:NagarroQueue",
"Condition": {
"ArnLike": {
"aws:SourceArn": "arn:aws:sns:ap-south-1:211125576450:NagarroTopic"
}
}
}
]
}

Steps.

Event-based architecture.

S3 -> SNS -> SQS -> Lambda -> DynamoDb

1. Create S3 bucket and upload sns-sqs-lambda/product.json.
2. Create SNS topic with custom access policy.
3. Create SQS queue with custom access policy.
4. Create Lambda with custom access policy. (Change handler path - com.ecommerce.product.handler.LambdaHandler::handleRequest)
5. Create S3 event notification.
6. Create SNS subscription to SQS.
7. Create SQS Lambda trigger.
8. Create DynamoDb.

References.

https://www.youtube.com/watch?v=B2Ra7X8rymw
https://www.youtube.com/watch?v=O2j8k7j9mLM
https://www.itonaut.com/2018/07/11/sqs-queue-as-lambda-trigger-in-aws-cloudformation/
