package com.satecho.agrosafe.platform.apiservice.shared.infrastructure.messaging.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for AgroSafe event bus.
 * Sets up topic exchange, queues, and bindings for inter-module communication.
 */
@Configuration
public class RabbitMQConfig {

    public static final String AGROSAFE_EXCHANGE = "agrosafe.exchange";
    public static final String DLX_EXCHANGE = "agrosafe.dlx";
    public static final String DLQ_QUEUE = "agrosafe.dlq";

    // IAM Context
    public static final String IAM_QUEUE = "iam.events";
    public static final String IAM_ROUTING_KEY = "iam.*";

    // IoT Device Management Context
    public static final String IOT_QUEUE = "iot.events";
    public static final String IOT_ROUTING_KEY = "iot.*";

    // Soil Monitoring Context
    public static final String SOIL_QUEUE = "soil.events";
    public static final String SOIL_ROUTING_KEY = "soil.*";

    // Irrigation Context
    public static final String IRRIGATION_QUEUE = "irrigation.events";
    public static final String IRRIGATION_ROUTING_KEY = "irrigation.*";

    // Security Context
    public static final String SECURITY_QUEUE = "security.events";
    public static final String SECURITY_ROUTING_KEY = "security.*";

    // Communication Context
    public static final String COMMUNICATION_QUEUE = "communication.events";
    public static final String COMMUNICATION_ROUTING_KEY = "communication.*";

    // Subscriptions Context
    public static final String SUBSCRIPTIONS_QUEUE = "subscriptions.events";
    public static final String SUBSCRIPTIONS_ROUTING_KEY = "subscriptions.*";

    // Onboarding Context
    public static final String ONBOARDING_QUEUE = "onboarding.events";
    public static final String ONBOARDING_ROUTING_KEY = "onboarding.*";

    // Advisory Context
    public static final String ADVISORY_QUEUE = "advisory.events";
    public static final String ADVISORY_ROUTING_KEY = "advisory.*";

    // Business Intelligence Context
    public static final String BI_QUEUE = "bi.events";
    public static final String BI_ROUTING_KEY = "bi.*";

    // Cross-context events
    public static final String SUBSCRIPTION_EVENTS_ROUTING_KEY = "subscription.*";
    public static final String USER_ACTIVITY_ROUTING_KEY = "user.*";
    public static final String DEVICE_EVENTS_ROUTING_KEY = "device.*";

    @Bean
    public TopicExchange agroSafeExchange() {
        return new TopicExchange(AGROSAFE_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(DLX_EXCHANGE, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_QUEUE, true);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(dlxExchange())
                .with("#");
    }

    // IAM Context
    @Bean
    public Queue iamQueue() {
        return QueueBuilder.durable(IAM_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding iamBinding(Queue iamQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(iamQueue)
                .to(agroSafeExchange)
                .with(IAM_ROUTING_KEY);
    }

    // IoT Device Management Context
    @Bean
    public Queue iotQueue() {
        return QueueBuilder.durable(IOT_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding iotBinding(Queue iotQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(iotQueue)
                .to(agroSafeExchange)
                .with(IOT_ROUTING_KEY);
    }

    // Soil Monitoring Context
    @Bean
    public Queue soilQueue() {
        return QueueBuilder.durable(SOIL_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding soilBinding(Queue soilQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(soilQueue)
                .to(agroSafeExchange)
                .with(SOIL_ROUTING_KEY);
    }

    // Irrigation Context
    @Bean
    public Queue irrigationQueue() {
        return QueueBuilder.durable(IRRIGATION_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding irrigationBinding(Queue irrigationQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(irrigationQueue)
                .to(agroSafeExchange)
                .with(IRRIGATION_ROUTING_KEY);
    }

    // Security Context
    @Bean
    public Queue securityQueue() {
        return QueueBuilder.durable(SECURITY_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding securityBinding(Queue securityQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(securityQueue)
                .to(agroSafeExchange)
                .with(SECURITY_ROUTING_KEY);
    }

    // Communication Context
    @Bean
    public Queue communicationQueue() {
        return QueueBuilder.durable(COMMUNICATION_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding communicationBinding(Queue communicationQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(communicationQueue)
                .to(agroSafeExchange)
                .with(COMMUNICATION_ROUTING_KEY);
    }

    // Subscriptions Context
    @Bean
    public Queue subscriptionsQueue() {
        return QueueBuilder.durable(SUBSCRIPTIONS_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding subscriptionsBinding(Queue subscriptionsQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(subscriptionsQueue)
                .to(agroSafeExchange)
                .with(SUBSCRIPTIONS_ROUTING_KEY);
    }

    // Onboarding Context
    @Bean
    public Queue onboardingQueue() {
        return QueueBuilder.durable(ONBOARDING_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding onboardingBinding(Queue onboardingQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(onboardingQueue)
                .to(agroSafeExchange)
                .with(ONBOARDING_ROUTING_KEY);
    }

    // Advisory Context
    @Bean
    public Queue advisoryQueue() {
        return QueueBuilder.durable(ADVISORY_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding advisoryBinding(Queue advisoryQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(advisoryQueue)
                .to(agroSafeExchange)
                .with(ADVISORY_ROUTING_KEY);
    }

    // Business Intelligence Context
    @Bean
    public Queue biQueue() {
        return QueueBuilder.durable(BI_QUEUE)
                .deadLetterExchange(DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Binding biBinding(Queue biQueue, TopicExchange agroSafeExchange) {
        return BindingBuilder.bind(biQueue)
                .to(agroSafeExchange)
                .with(BI_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

}
