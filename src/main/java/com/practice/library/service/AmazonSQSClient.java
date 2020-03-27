package com.practice.library.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AmazonSQSClient {

  @Value("${amazonProperties.accessKey}")
  private String accessKey;

  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  private ObjectMapper objectMapper;
  private AmazonSQS amazonSQS;

  @PostConstruct
  private void init() {
    final AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

    this.objectMapper = new ObjectMapper();
    this.amazonSQS = AmazonSQSClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.EU_NORTH_1)
        .build();
  }

  public <T> SendMessageResult sendMessage(String queueChannel, T message) {
    String queueUrl = this.amazonSQS.getQueueUrl(queueChannel).getQueueUrl();
    String messageJson = convertToJson(message);
    log.info("Send message to SQS: " + messageJson);
    return this.amazonSQS.sendMessage(queueUrl, messageJson);
  }

  public <T> Optional<T> getMessage(String queueChannel, Class<T> classType) {
    Optional<T> object = Optional.empty();
    String queueUrl = this.amazonSQS.getQueueUrl(queueChannel).getQueueUrl();
    List<Message> messages = this.amazonSQS.receiveMessage(queueUrl).getMessages();

    log.info("Trying get message from SQS...");
    if (!messages.isEmpty()) {
      Message message = messages.get(0);
      object = convertToObject(message, classType);
      if (object.isPresent()) {
        deleteMessage(queueChannel, message.getReceiptHandle());
        log.info("Receive message from SQS: " + message.getBody());
      }
    }
    return object;
  }

  public void deleteMessage(String queueChannel, String receiptHandle) {
    this.amazonSQS.deleteMessage(queueChannel, receiptHandle);
  }

  private <T> String convertToJson(T message) {
    String jsonObject = "";

    try {
      jsonObject = objectMapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      log.error("ObjectMapper exception", e);
    }
    return jsonObject;
  }

  private <T> Optional<T> convertToObject(Message message, Class<T> classType) {
    Optional<T> object = Optional.empty();

    try {
      object = objectMapper.readerFor(classType).readValue(message.getBody());
    } catch (IOException e) {
      log.error("ObjectMapper exception", e);
    }
    return object;
  }
}
