package edu.harvard.libcomm.config;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;

import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;

import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClient;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


@Configuration
@PropertySource("classpath:librarycloud.env.properties")
public class Config {

    private String awsRegion = "us-east-1";

    @Value( "${aws.sqs.endpoint:#{null}}" )
    private String awsSqsEndpoint;

    @Value( "${aws.sns.endpoint:#{null}}" )
    private String awsSnsEndpoint;

    @Value( "${aws.s3.endpoint:#{null}}" )
    private String awsS3Endpoint;

    private AWSStaticCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider( new BasicAWSCredentials("${aws.access.key}", "${aws.secret.key}") );

    @Bean
    public AmazonSQSAsync sqsClient() {

        AmazonSQSAsyncClientBuilder	sqsClientBuilder = AmazonSQSAsyncClientBuilder.standard().withCredentials(awsCredentialsProvider);

        if (awsSqsEndpoint != null) {
            sqsClientBuilder.setEndpointConfiguration(new EndpointConfiguration(awsSqsEndpoint, awsRegion));
        } else {
            sqsClientBuilder.setRegion(awsRegion);
        }

        return sqsClientBuilder.build();
    }


    @Bean
    public AmazonSNSAsync snsClient() {
        AmazonSNSAsyncClientBuilder	snsClientBuilder = AmazonSNSAsyncClientBuilder.standard().withCredentials(awsCredentialsProvider);

        if (awsSnsEndpoint != null) {
            snsClientBuilder.setEndpointConfiguration(new EndpointConfiguration(awsSnsEndpoint, awsRegion));
        } else {
            snsClientBuilder.setRegion(awsRegion);
        }

        return snsClientBuilder.build();
    }

    @Bean
    public AmazonS3 s3Client() {
        AmazonS3ClientBuilder	s3ClientBuilder = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).withPathStyleAccessEnabled(true);

        if (awsS3Endpoint != null) {
            s3ClientBuilder.setEndpointConfiguration(new EndpointConfiguration(awsS3Endpoint, awsRegion));
        } else {
            s3ClientBuilder.setRegion(awsRegion);
        }

        return s3ClientBuilder.build();
    }
}
