package com.ecotravel.accommodation_service.config;

import org.raflab.shared.events.WorkOrderCompletedEvent;
import org.raflab.shared.events.WorkOrderCreatedEvent;
import org.raflab.shared.events.WorkOrderDeletedEvent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static java.util.Map.entry;

@Configuration
public class RabbitMQConfig {

    public static final String ACCOMMODATION_MAINTENANCE_QUEUE = "accommodation.maintenance-events.queue";
    public static final String MAINTENANCE_EXCHANGE = "maintenance.events.exchange";
    public static final String MAINTENANCE_ROUTING_KEY_PATTERN = "maintenance.work-order.*";

    @Bean
    public Queue accommodationMaintenanceQueue() {
        return new Queue(ACCOMMODATION_MAINTENANCE_QUEUE, true);
    }

    @Bean
    public TopicExchange maintenanceExchange() {
        return new TopicExchange(MAINTENANCE_EXCHANGE, true, false);
    }

    @Bean
    public Binding accommodationMaintenanceBinding(
            @Qualifier("accommodationMaintenanceQueue") Queue queue,
            @Qualifier("maintenanceExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MAINTENANCE_ROUTING_KEY_PATTERN);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("org.raflab.shared.events");
        classMapper.setIdClassMapping(Map.ofEntries(
                entry("WorkOrderCreatedEvent", WorkOrderCreatedEvent.class),
                entry("org.raflab.shared.events.WorkOrderCreatedEvent", WorkOrderCreatedEvent.class),
                entry("WorkOrderCompletedEvent", WorkOrderCompletedEvent.class),
                entry("org.raflab.shared.events.WorkOrderCompletedEvent", WorkOrderCompletedEvent.class),
                entry("WorkOrderDeletedEvent", WorkOrderDeletedEvent.class),
                entry("org.raflab.shared.events.WorkOrderDeletedEvent", WorkOrderDeletedEvent.class)
        ));
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationRunner initializeQueues(RabbitAdmin rabbitAdmin) {
        return args -> {
            rabbitAdmin.initialize();
            System.out.println(">>> RABBITMQ REDOVI (ACCOMMODATION) SU DEKLARISANI I SPREMNI!");
        };
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}