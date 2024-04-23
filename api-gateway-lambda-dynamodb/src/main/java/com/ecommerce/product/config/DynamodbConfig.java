package com.ecommerce.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamodbConfig {
    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(
                                DynamoDbClient.builder()
                                        .region(Region.AP_SOUTH_1)
                                        .credentialsProvider(StaticCredentialsProvider.create(
                                                AwsBasicCredentials.create("accessKeyId", "secretAccessKey")))
                                        .build())
                .build();
    }
}