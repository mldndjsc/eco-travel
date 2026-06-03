package com.ecotravel.avantura.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String AVANTURE_EVENTS_EXCHANGE = "avanture.events.exchange";
    public static final String VODICI_EVENTS_EXCHANGE = "vodici.events.exchange";
    public static final String AVANTURE_GUIDE_EVENTS_QUEUE = "avanture.guide-events.queue";
    public static final String ADVENTURE_CREATED_ROUTING_KEY = "adventure.created";
    public static final String ADVENTURE_DELETED_ROUTING_KEY = "adventure.deleted";
    public static final String GUIDE_CREATED_ROUTING_KEY = "guide.created";
    public static final String GUIDE_DELETED_ROUTING_KEY = "guide.deleted";

    @Bean
    public Queue avantureGuideEventsQueue() {
        return new Queue(AVANTURE_GUIDE_EVENTS_QUEUE, true);
    }

    @Bean
    public TopicExchange avantureEventsExchange() {
        return new TopicExchange(AVANTURE_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange vodiciEventsExchange() {
        return new TopicExchange(VODICI_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public Binding guideCreatedBinding(
            @Qualifier("avantureGuideEventsQueue") Queue avantureGuideEventsQueue,
            @Qualifier("vodiciEventsExchange") TopicExchange vodiciEventsExchange) {
        return BindingBuilder
                .bind(avantureGuideEventsQueue)
                .to(vodiciEventsExchange)
                .with(GUIDE_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding guideDeletedBinding(
            @Qualifier("avantureGuideEventsQueue") Queue avantureGuideEventsQueue,
            @Qualifier("vodiciEventsExchange") TopicExchange vodiciEventsExchange) {
        return BindingBuilder
                .bind(avantureGuideEventsQueue)
                .to(vodiciEventsExchange)
                .with(GUIDE_DELETED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationRunner initializeQueues(RabbitAdmin rabbitAdmin) {
        return args -> {
            rabbitAdmin.initialize();
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setObservationEnabled(true);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setObservationEnabled(true);
        return factory;
    }
}
