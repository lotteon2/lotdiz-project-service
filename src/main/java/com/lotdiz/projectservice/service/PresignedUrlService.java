package com.lotdiz.projectservice.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresignedUrlService {
  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String getPresignedUrl(String prefix, String fileName) {
    String onlyOneFileName = onlyOneFileName(fileName);

    if (!prefix.equals("")) {
      onlyOneFileName = prefix + "/" + onlyOneFileName;
    }
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        getGeneratePreSignedUrlRequest(bucket, onlyOneFileName);

    return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
  }

  private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(
      String bucket, String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getPreSignedUrlExpiration());
    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
    return generatePresignedUrlRequest;
  }

  private Date getPreSignedUrlExpiration() {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 2;
    expiration.setTime(expTimeMillis);
    log.info(expiration.toString());
    return expiration;
  }

  private String onlyOneFileName(String filename) {
    return UUID.randomUUID().toString() + filename;
  }
}
