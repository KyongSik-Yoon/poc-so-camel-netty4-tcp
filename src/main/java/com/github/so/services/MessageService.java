package com.github.so.services;

import java.util.LinkedList;
import java.util.List;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MessageService {
  
  private static final Logger log = LoggerFactory.getLogger(MessageService.class);

  private List<String> messageStore = new LinkedList<>();
  
  @Autowired
  private ProducerTemplate producerTemplate;

  public void sendToTCP(final String message) {
    log.info("[Service] - Sending Message over TCP Channel --> {}", message);
    producerTemplate.sendBody("netty4:tcp://localhost:7000?textline=true&encoding=utf8", message); 
  }

  @Handler
  public void receiveFromTCP(final Exchange exchange) {
    final String messageFromTcp = exchange.getIn().getBody(String.class);
    log.info("[Service] - Message Received from TCP Channel --> {}", messageFromTcp);
    this.messageStore.add(messageFromTcp);
  }

  public List<String> getReceivedMessages() {
    return messageStore;
  }
}
