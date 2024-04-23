package com.ecommerce.product.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class Product {

    private String id;
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String type;
    @Getter
    private Double price;
    @Getter
    private String currency;
    @Getter
    private String createdAt;
    @Getter
    private String updatedAt;
    @Getter
    private String status;
    @Getter
    private String errorMessage;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
