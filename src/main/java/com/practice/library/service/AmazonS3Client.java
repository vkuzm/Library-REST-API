package com.practice.library.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class AmazonS3Client {

  private AmazonS3 amazonS3;

  @Value("${amazonProperties.accessKey}")
  private String accessKey;

  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  @Value("${amazonS3Properties.bucketName}")
  private String bucketName;

  @Value("${amazonS3Properties.endpointUrl}")
  private String endpointUrl;

  @PostConstruct
  private void init() {
    final AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

    this.amazonS3 = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.EU_NORTH_1)
        .build();
  }

  public String uploadFile(MultipartFile multipartFile) {
    String fileURL = "";
    try {
      File file = convertMultipartFileToFile(multipartFile);
      if (file != null) {
        String fileName = multipartFile.getOriginalFilename();
        fileURL = endpointUrl + "/" + bucketName + "/" + fileName;
        uploadFileToBucket(fileName, file);
        file.delete();
      }
    } catch (Exception e) {
      log.error("File exception", e);
    }
    return fileURL;
  }

  public String getFile(String fileName) {
    if (fileName != null && !fileName.isEmpty()) {
      S3Object object = this.amazonS3.getObject(bucketName, fileName);
      if (object.getKey().length() > 0) {
        return object.getKey();
      }
    }
    return "";
  }

  public void deleteFileFromBucket(String fileName) {
    this.amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
  }

  private void uploadFileToBucket(String fileName, File file) {
    this.amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
        .withCannedAcl(CannedAccessControlList.PublicRead));
  }

  private File convertMultipartFileToFile(MultipartFile file) throws IOException {
    File convertedFile = null;

    if (file.getOriginalFilename() != null && file.getOriginalFilename().length() > 0) {
      convertedFile = new File(file.getOriginalFilename());
      try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
        fos.write(file.getBytes());
      }
    }
    return convertedFile;
  }
}