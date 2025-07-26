package com.arete.korbly.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSS3Config {
    Dotenv dotenv = Dotenv
            .configure()
            .load();

    private final String awsAccessKey = dotenv.get("AWS_ACCESS_KEY");
    private final String awsSecretKey = dotenv.get("AWS_SECRET_KEY");

    @Bean
    public AmazonS3 getAmazonS3Client(){
        AWSCredentials credentials = new BasicAWSCredentials(
                awsAccessKey,
                awsSecretKey
        );
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}
