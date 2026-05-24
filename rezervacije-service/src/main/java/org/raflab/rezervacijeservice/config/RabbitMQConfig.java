package org.raflab.rezervacijeservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.raflab.rezervacijeservice.messaging.event.ReservationPaymentRecordedFeedbackEvent;
import org.raflab.rezervacijeservice.messaging.event.TransactionCreatedEvent;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String RESERVATIONS_FINANCE_EVENTS_QUEUE = "rezervacije.finance-events.queue";
    public static final String FINANCE_RESERVATION_FEEDBACK_QUEUE = "finansije.reservation-feedback.queue";
    public static final String EXCHANGE = "hotel.events.exchange";
    public static final String FEEDBACK_EXCHANGE = "hotel.feedback.exchange";
    public static final String TRANSACTION_CREATED_ROUTING_KEY = "finance.transaction.created";
    public static final String PAYMENT_RECORDED_FEEDBACK_ROUTING_KEY = "reservation.payment-recorded.feedback";

    @Bean
    public Queue reservationsFinanceEventsQueue() {
        return new Queue(RESERVATIONS_FINANCE_EVENTS_QUEUE, true);
    }

    @Bean
    public Queue financeReservationFeedbackQueue() {
        return new Queue(FINANCE_RESERVATION_FEEDBACK_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange feedbackExchange() {
        return new TopicExchange(FEEDBACK_EXCHANGE, true, false);
    }

    @Bean
    public Binding transactionCreatedBinding(
            @Qualifier("reservationsFinanceEventsQueue") Queue reservationsFinanceEventsQueue,
            @Qualifier("exchange") TopicExchange exchange) {
        return BindingBuilder.bind(reservationsFinanceEventsQueue)
                .to(exchange)
                .with(TRANSACTION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentRecordedFeedbackBinding(
            @Qualifier("financeReservationFeedbackQueue") Queue financeReservationFeedbackQueue,
            @Qualifier("feedbackExchange") TopicExchange feedbackExchange) {
        return BindingBuilder.bind(financeReservationFeedbackQueue)
                .to(feedbackExchange)
                .with(PAYMENT_RECORDED_FEEDBACK_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(Map.of(
                "TransactionCreatedEvent", TransactionCreatedEvent.class,
                "org.raflab.finansijeservice.messaging.event.TransactionCreatedEvent", TransactionCreatedEvent.class,
                "ReservationPaymentRecordedFeedbackEvent", ReservationPaymentRecordedFeedbackEvent.class
        ));
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    // Kreiranje exchange, queues and binding prilikom pokretanja aplikacije
    @Bean
    public ApplicationRunner initializeQueues(RabbitAdmin rabbitAdmin) {
        return args -> {
            rabbitAdmin.initialize();
            System.out.println(">>> RABBITMQ REDOVI SU DEKLARISANI I SPREMNI!");
        };
    }

    //vezbe 11

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
