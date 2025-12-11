package com.mudosa.musinsa.chat.broker.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqChatConfig {

  public static final String CHAT_EXCHANGE = "chat.fanout.exchange";
  public static final String CHAT_QUEUE = "chat.message.q";

  @Bean
  public FanoutExchange chatFanoutExchange() {
    return new FanoutExchange(CHAT_EXCHANGE, true, false);
  }

  @Bean
  public Queue chatMessageQueue() {
    return new Queue(CHAT_QUEUE, true);
  }

  @Bean
  public Binding chatQueueBinding(FanoutExchange chatFanoutExchange, Queue chatMessageQueue) {
    return BindingBuilder.bind(chatMessageQueue)
        .to(chatFanoutExchange);
  }
}
