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

        switch (apiGatewayProxyRequestEvent.getHttpMethod()) {
            case "POST":
                try {
                    return productService.createProductLambda(apiGatewayProxyRequestEvent, context);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            case "GET":
                if (apiGatewayProxyRequestEvent.getPathParameters() != null) {
                    try {
                        return productService.getProductByIdLambda(apiGatewayProxyRequestEvent, context);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        return productService.getAllProductLambda(apiGatewayProxyRequestEvent, context);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            default:
                throw new Error("Unsupported Methods:::" + apiGatewayProxyRequestEvent.getHttpMethod());
        }
    }
}