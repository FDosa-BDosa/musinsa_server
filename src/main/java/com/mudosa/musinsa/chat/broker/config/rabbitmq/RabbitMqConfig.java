package com.mudosa.musinsa.chat.broker.config.rabbitmq;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  @Value("${spring.rabbitmq.host:localhost}")
  private String rabbitHost;


  // ----------------------------------------
  // 2) Connection Factory
  // ----------------------------------------

  @Bean
  public CachingConnectionFactory connectionFactory() {
    CachingConnectionFactory factory = new CachingConnectionFactory();
    factory.setHost(rabbitHost);
    factory.setPort(5672);
    factory.setUsername("guest");
    factory.setPassword("guest");

    factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);

    return factory;
  }

  // ----------------------------------------
  // 3) Message Converter (Jackson)
  // ----------------------------------------

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  // ----------------------------------------
  // 4) Producer (RabbitTemplate)
  // ----------------------------------------

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                       MessageConverter messageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter);
    return template;
  }

  // ----------------------------------------
  // 5) Consumer Listener Container
  // ----------------------------------------

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory,
      MessageConverter messageConverter
  ) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(messageConverter);
    // 동시 컨슈머 개수
    factory.setConcurrentConsumers(8);
    factory.setMaxConcurrentConsumers(16);

    // QoS
    factory.setPrefetchCount(20);

    // 예외 발생 시 재큐 안 하고 바로 reject (DLX 쓰면 거기로 보냄)
    factory.setDefaultRequeueRejected(false);

    factory.setMissingQueuesFatal(false);
    return factory;
  }
}
