package com.ecommerce.product.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ecommerce.product.config.S3Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private final String bucket = "product-amway-aditya";

    @Override
    public APIGatewayProxyResponseEvent saveProductLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException {
        var s3Client = getS3Client();
        var headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucket).build();
        try {
            s3Client.headBucket(headBucketRequest);
        } catch (NoSuchBucketException e) {
            S3Waiter s3Waiter = s3Client.waiter();
            var bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucket).build();

            s3Client.createBucket(bucketRequest);
            var bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucket).build();

            s3Waiter.waitUntilBucketExists(bucketRequestWait);
            System.out.println("Bucket product-amway-aditya is ready");
        }
        var objectRequest = PutObjectRequest.builder()
                .bucket(bucket).key(System.currentTimeMillis() + "_product.json").build();

        s3Client.putObject(objectRequest, RequestBody.fromString(apiGatewayProxyRequestEvent.getBody()));
        return createAPIResponse("Object placed into s3 Bucket successfully", 201, new HashMap<>());
    }

    @Override
    public APIGatewayProxyResponseEvent readAllProductLambda(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
            throws JsonProcessingException {
        var s3Client = getS3Client();
        var headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucket).build();
        try {
            s3Client.headBucket(headBucketRequest);
            var getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket).key("product.json").build();
            var responseInputStream = s3Client.getObject(getObjectRequest);

            return createAPIResponse(new String(readAllBytes(responseInputStream), StandardCharsets.UTF_8), 200, new HashMap<>());
        } catch (NoSuchBucketException e) {
            return createAPIResponse("No bucket with name " + bucket + " exists !", 404, new HashMap<>());
        } catch (IOException e) {
            return createAPIResponse(e.getMessage(), 400, new HashMap<>());
        }
    }

    private APIGatewayProxyResponseEvent createAPIResponse(String body, int statusCode, Map<String,String> headers) {
        var responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(body);
        responseEvent.setHeaders(headers);
        responseEvent.setStatusCode(statusCode);
        return responseEvent;
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

    private S3Client getS3Client() {
        return new S3Config().s3Client();
    }
}
