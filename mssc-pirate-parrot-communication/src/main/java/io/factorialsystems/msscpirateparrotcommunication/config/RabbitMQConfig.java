package io.factorialsystems.msscpirateparrotcommunication.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.email.name}")
    private String emailQueueName;

    @Value("${rabbitmq.queue.sms.name}")
    private String smsQueueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.sms.key}")
    private String smsRouting;

    @Value("${rabbitmq.routing.email.key}")
    private String emailRouting;

    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueueName, false);
    }

    @Bean
    public Queue smsQueue() {
        return new Queue(smsQueueName, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding emailBinding(TopicExchange exchange) {
        return BindingBuilder
                .bind(emailQueue())
                .to(exchange)
                .with(emailRouting);
    }

    @Bean
    public Binding smsBinding (TopicExchange exchange) {
        return BindingBuilder
                .bind(smsQueue())
                .to(exchange)
                .with(smsRouting);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}
