package com.ecommerce.product.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ecommerce.product.config.DynamodbConfig;
import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public APIGatewayProxyResponseEvent createProductLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var product = objectMapper.readValue(apiGatewayProxyRequestEvent.getBody(), Product.class);

        var tableSchema = getDynamoDbEnhancedClient().table("Product", TableSchema.fromBean(Product.class));
        tableSchema.putItem(product);

        return createAPIResponse(objectMapper.writeValueAsString(product), 201, new HashMap<>());
    }

    @Override
    public APIGatewayProxyResponseEvent getAllProductLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException {
        var tableSchema = getDynamoDbEnhancedClient().table("Product", TableSchema.fromBean(Product.class));
        var products = tableSchema.scan().stream().toList();

        var productDtos = new ArrayList<ProductDto>();

        for (var page : products) {
            for (var product : page.items()) {
                productDtos.add(productMapper(product));
            }
        }
        return createAPIResponse(new ObjectMapper().writeValueAsString(productDtos), 200, new HashMap<>());
    }

    @Override
    public APIGatewayProxyResponseEvent getProductByIdLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException {
        var id = apiGatewayProxyRequestEvent.getPathParameters().get("id");

        var tableSchema = getDynamoDbEnhancedClient().table("Product", TableSchema.fromBean(Product.class));

        Optional<Product> product = Optional.ofNullable(tableSchema.getItem(Key.builder()
                .partitionValue(id).build()));
        if (product.isEmpty()) {
            return createAPIResponse("Product with id : " + id + " not found !", 404, new HashMap<>());
        } else {
            return createAPIResponse(new ObjectMapper().writeValueAsString(product.get()), 200, new HashMap<>());
        }
    }

    private ProductDto productMapper(Product product) {

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).currency(product.getCurrency())
                .createdAt(product.getCreatedAt()).updatedAt(product.getUpdatedAt())
                .type(product.getType())
                .status(product.getStatus()).errorMessage(product.getErrorMessage()).build();
    }

    private APIGatewayProxyResponseEvent createAPIResponse(String body, int statusCode, Map<String,String> headers) {
        var responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(body);
        responseEvent.setHeaders(headers);
        responseEvent.setStatusCode(statusCode);
        return responseEvent;
    }

    private DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
        return new DynamodbConfig().dynamoDbEnhancedClient();
    }
}
