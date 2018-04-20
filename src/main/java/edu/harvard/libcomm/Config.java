package edu.harvard.libcomm.config;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import org.apache.camel.spi.DataFormat;

import edu.harvard.libcomm.pipeline.MessageBodyS3Marshaller;
import edu.harvard.libcomm.pipeline.IProcessor;
import edu.harvard.libcomm.pipeline.LibCommProcessor;
import edu.harvard.libcomm.pipeline.enrich.AddMarcLocationProcessor;


// 1.11.x
// import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

// 1.11.x
//import com.amazonaws.auth.AWSStaticCredentialsProvider;

// 1.11.x
// import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
// import com.amazonaws.services.sqs.AmazonSQSAsync;
// import com.amazonaws.services.sqs.AmazonSQSAsyncClient;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;


// 1.11.x
// import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
// import com.amazonaws.services.sns.AmazonSNSAsync;
// import com.amazonaws.services.sns.AmazonSNSAsyncClient;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;


// 1.11.x
// import com.amazonaws.services.s3.AmazonS3ClientBuilder;
// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;


// import edu.harvard.libcomm.AmazonSQSAsyncShim;

@Configuration
@PropertySource("classpath:librarycloud.env.properties")
public class Config {

    private String awsRegion = "us-east-1";

    @Value( "${aws.sqs.endpoint:#{null}}" )
    private String awsSqsEndpoint;

    @Value( "${aws.sns.endpoint:#{null}}" )
    private String awsSnsEndpoint;

    @Value( "${aws.s3.endpoint:}" )
    private String awsS3Endpoint;

    @Value( "${aws.access.key}" )
    private String awsAccessKey;

    @Value( "${aws.secret.key}" )
    private String awsSecretKey;

    @Value( "${librarycloud.s3.marc_bucket}" )
    private String marcBucket;

    @Value( "${librarycloud.sqs.environment}" )
    private String sqsEnvironment;

    @Bean
    // public AmazonSQSAsync sqsClient() {
    public AmazonSQS sqsClient() {
        AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(awsAccessKey, awsSecretKey));

        if (awsSqsEndpoint != null) {
            sqs.setEndpoint(awsSqsEndpoint);
        }

        return sqs;

        // AWSStaticCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider( new BasicAWSCredentials(awsAccessKey, awsSecretKey) );
        // AWSCredentials aw = awsCredentialsProvider.getCredentials();

        // AmazonSQSAsyncClientBuilder	sqsClientBuilder = AmazonSQSAsyncClientBuilder.standard().withCredentials(awsCredentialsProvider);

        // if (awsSqsEndpoint != null) {
        //     sqsClientBuilder.setEndpointConfiguration(new EndpointConfiguration(awsSqsEndpoint, awsRegion));
        // } else {
        //     sqsClientBuilder.setRegion(awsRegion);
        // }

        // return sqsClientBuilder.build();



        // AmazonSQSAsync orig = sqsClientBuilder.build();
        // AmazonSQSAsync proxy = new AmazonSQSAsyncShim().bind(orig);
        // return proxy;
    }


    @Bean
    // public AmazonSNSAsync snsClient() {
    public AmazonSNS snsClient() {
        AmazonSNS sns = new AmazonSNSClient(new BasicAWSCredentials(awsAccessKey, awsSecretKey));

        if (awsSnsEndpoint != null) {
            sns.setEndpoint(awsSnsEndpoint);
        }

        return sns;



        // AWSStaticCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider( new BasicAWSCredentials(awsAccessKey, awsSecretKey) );
        // AmazonSNSAsyncClientBuilder	snsClientBuilder = AmazonSNSAsyncClientBuilder.standard().withCredentials(awsCredentialsProvider);

        // if (awsSnsEndpoint != null) {
        //     snsClientBuilder.setEndpointConfiguration(new EndpointConfiguration(awsSnsEndpoint, awsRegion));
        // } else {
        //     snsClientBuilder.setRegion(awsRegion);
        // }

        // return snsClientBuilder.build();
    }

    @Bean
    public AmazonS3 s3Client() {
        S3ClientOptions opts = new S3ClientOptions().withPathStyleAccess(true);
        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));

        s3.setS3ClientOptions(opts);

        if (awsS3Endpoint != null) {
            s3.setEndpoint(awsS3Endpoint);
        }

        return s3;



        // AWSStaticCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider( new BasicAWSCredentials(awsAccessKey, awsSecretKey) );
        // AmazonS3ClientBuilder	s3ClientBuilder = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).withPathStyleAccessEnabled(true);

        // if (awsS3Endpoint != null) {
        //     s3ClientBuilder.setEndpointConfiguration(new EndpointConfiguration(awsS3Endpoint, awsRegion));
        // } else {
        //     s3ClientBuilder.setRegion(awsRegion);
        // }

        // return s3ClientBuilder.build();
    }


    @Bean LibCommProcessor addMarcLocationProcessor() {
        LibCommProcessor outerProcessor = new LibCommProcessor();
        AddMarcLocationProcessor innerProcessor = new AddMarcLocationProcessor();
        innerProcessor.setMarcBaseUrl("https://s3.amazonaws.com/" + marcBucket + "." + sqsEnvironment + "./");
        outerProcessor.setProcessor(innerProcessor);
        return outerProcessor;
    }

    // <!-- <!-\- Custom S3 marshaller to place body on S3 -\-> -->
    // <!-- <bean id="cloudbody" class="edu.harvard.libcomm.pipeline.MessageBodyS3Marshaller"> -->
    // <!--     <constructor-arg value="20000" /> <!-\- If message size in bytes is greater than this, save body to S3 -\-> -->
    // <!--     <constructor-arg value="${librarycloud.s3.cache_bucket}.${librarycloud.sqs.environment}" /> -->
    // <!-- </bean> -->

    @Bean
    DataFormat cloudbody() {
        DataFormat cloudbody = new MessageBodyS3Marshaller(20000, "${librarycloud.s3.cache_bucket}.${librarycloud.sqs.environment}");
        return cloudbody;
    }
}
