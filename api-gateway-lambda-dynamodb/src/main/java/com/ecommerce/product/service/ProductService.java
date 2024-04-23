package com.ecommerce.product.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductService {
    APIGatewayProxyResponseEvent createProductLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException;

    APIGatewayProxyResponseEvent getAllProductLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException;

    APIGatewayProxyResponseEvent getProductByIdLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException;
}
