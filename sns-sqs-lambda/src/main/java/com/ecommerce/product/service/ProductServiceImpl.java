package com.ecommerce.product.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ecommerce.product.config.DynamodbConfig;
import com.ecommerce.product.config.S3Config;
import com.ecommerce.product.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public APIGatewayProxyResponseEvent pushProductLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException {
        var s3Client = getS3Client();
        var bucket = "product-amway-aditya";
        var headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucket).build();
        try {
            s3Client.headBucket(headBucketRequest);
            var getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket).key("product.json").build();
            var responseInputStream = s3Client.getObject(getObjectRequest);
            var fileContent = new String(readAllBytes(responseInputStream));

            var objectMapper = new ObjectMapper();
            var product = objectMapper.readValue(fileContent, Product.class);

            var tableSchema = getDynamoDbEnhancedClient().table("Product", TableSchema.fromBean(Product.class));
            tableSchema.putItem(product);
        } catch (NoSuchBucketException e) {
            return createAPIResponse("No bucket with name " + bucket + " exists !", 404, new HashMap<>());
        } catch (IOException e) {
            return createAPIResponse(e.getMessage(), 400, new HashMap<>());
        }
        return createAPIResponse("Object placed into DynamoDb Bucket successfully", 201, new HashMap<>());
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 1024;
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                outputStream.write(buf, 0, readLen);
            return outputStream.toByteArray();
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                System.out.println("Error while reading file content from S3");
                exception.addSuppressed(e);
            }
        }
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

    private S3Client getS3Client() {
        return new S3Config().s3Client();
    }
}
