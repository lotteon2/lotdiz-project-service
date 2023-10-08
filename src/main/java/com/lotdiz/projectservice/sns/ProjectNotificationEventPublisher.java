package com.lotdiz.projectservice.sns;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectNotificationEventPublisher {

  private final NotificationMessagingTemplate notificationMessagingTemplate;

  public void send(String destinationName, String message) {
    notificationMessagingTemplate.sendNotification(destinationName, message, null);
  }
}
