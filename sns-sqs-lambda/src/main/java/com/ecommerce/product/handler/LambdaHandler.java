package com.ecommerce.product.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ecommerce.product.service.ProductServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        var applicationContext = new AnnotationConfigApplicationContext(ProductServiceImpl.class);
        var productService = applicationContext.getBean(ProductServiceImpl.class);
        try {
            return productService.pushProductLambda(apiGatewayProxyRequestEvent, context);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}