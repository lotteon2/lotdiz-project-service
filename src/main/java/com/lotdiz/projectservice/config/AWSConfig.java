package com.lotdiz.projectservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AWSConfig {

  @Value("${cloud.aws.credentials.ACCESS_KEY_ID}")
  private String accessKeyId;

  @Value("${cloud.aws.credentials.SECRET_ACCESS_KEY}")
  private String secretAccessKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Primary
  @Bean
  public AmazonSQSAsync amazonSQSAsync() {
    return AmazonSQSAsyncClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKeyId, secretAccessKey)))
        .build();
  }

  @Primary
  @Bean
  public AmazonSNSAsync amazonSNSAsync() {
    return AmazonSNSAsyncClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(getBasicAWSCredentials()))
        .build();
  }

  @NotNull
  private BasicAWSCredentials getBasicAWSCredentials() {
    return new BasicAWSCredentials(accessKeyId, secretAccessKey);
  }

  @Bean
  public NotificationMessagingTemplate notificationMessagingTemplate() {
    return new NotificationMessagingTemplate(amazonSNSAsync());
  }

  @Bean
  public AmazonS3 amazonS3Client() {
    return AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(getBasicAWSCredentials()))
        .build();
  }
}
