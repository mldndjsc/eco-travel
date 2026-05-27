package com.ecotravel.maintenance_service.config;
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
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MAINTENANCE_EXCHANGE = "maintenance.events.exchange";
    public static final String MAINTENANCE_FEEDBACK_EXCHANGE = "maintenance.feedback.exchange";
    public static final String MAINTENANCE_BOOKING_QUEUE = "maintenance.booking-events.queue";
    public static final String RESERVATIONS_MAINTENANCE_FEEDBACK_QUEUE = "rezervacije.maintenance-feedback.queue";
    public static final String BOOKING_CREATED_MAINTENANCE_ROUTING_KEY = "reservation.booking.created";
    public static final String MAINTENANCE_WORK_ORDER_CREATED_FEEDBACK_ROUTING_KEY = "maintenance.work-order.created.feedback";

    @Bean
    public Queue maintenanceBookingQueue() {
        return new Queue(MAINTENANCE_BOOKING_QUEUE, true);
    }

    @Bean
    public Queue reservationsMaintenanceFeedbackQueue() {
        return new Queue(RESERVATIONS_MAINTENANCE_FEEDBACK_QUEUE, true);
    }

    @Bean
    public TopicExchange maintenanceExchange() {
        return new TopicExchange(MAINTENANCE_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange maintenanceFeedbackExchange() {
        return new TopicExchange(MAINTENANCE_FEEDBACK_EXCHANGE, true, false);
    }

    @Bean
    public Binding maintenanceBookingBinding(Queue maintenanceBookingQueue, TopicExchange maintenanceExchange) {
        return BindingBuilder
                .bind(maintenanceBookingQueue)
                .to(maintenanceExchange)
                .with(BOOKING_CREATED_MAINTENANCE_ROUTING_KEY);
    }

    @Bean
    public Binding maintenanceWorkOrderCreatedFeedbackBinding(
            Queue reservationsMaintenanceFeedbackQueue,
            TopicExchange maintenanceFeedbackExchange
    ) {
        return BindingBuilder
                .bind(reservationsMaintenanceFeedbackQueue)
                .to(maintenanceFeedbackExchange)
                .with(MAINTENANCE_WORK_ORDER_CREATED_FEEDBACK_ROUTING_KEY);
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
            System.out.println(">>> RABBITMQ MAINTENANCE REDOVI SU DEKLARISANI I SPREMNI!");
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setObservationEnabled(true);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setObservationEnabled(true);
        return factory;
    }
}