package com.laowan.rabbitmq.producer.config;

import com.laowan.rabbitmq.producer.enums.ExchangeEnum;
import com.laowan.rabbitmq.producer.enums.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @program: rpc-parent
 * @description: Fixed   rpc请求模式
 * @author: wanli
 * @create: 2020-04-09 18:05
 **/
@Configuration
@Slf4j
public class FixedReplyConfig {
    @Bean
    public Queue fixedRequest() {
        return new Queue(QueueEnum.FIXED_REQUEST.getName(), true);
    }

    @Bean
    public DirectExchange fixedExchange() {
        return new DirectExchange(ExchangeEnum.FIXED_EXCHANGE.getValue());
    }

    @Bean
    public Binding fixedBinding() {
        return BindingBuilder.bind(fixedRequest()).to(fixedExchange()).with(QueueEnum.FIXED_REQUEST.getRoutingKey());
    }

    /**
     * 注意，固定模式指定的应答队列  exclusive排他属性设置为true，且能自动删除
     * @return
     */
    @Bean
    public Queue fixedResponseQueue() {
        return new Queue(QueueEnum.FIXED_RESPONSE.getName(),false,true,true,new HashMap<>());
    }


    @Bean
    public RabbitTemplate fixedRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        //设置固定的Reply 地址
        template.setUseTemporaryReplyQueues(false);
        template.setReplyAddress(QueueEnum.FIXED_RESPONSE.getName());
        template.expectedQueueNames();
        template.setUserCorrelationId(true);

        //设置请求超时时间为10s
        template.setReplyTimeout(10000);
        return template;
    }


    @Bean
    public SimpleMessageListenerContainer fixedListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //这一步非常重要，固定队列模式要，一定要主动设置  SimpleMessageListenerContainer监听容器，监听应答队列
        container.setQueueNames(QueueEnum.FIXED_RESPONSE.getName());
        container.setMessageListener(fixedRabbitTemplate(connectionFactory));
        container.setConcurrentConsumers(100);
        container.setConcurrentConsumers(100);
        container.setPrefetchCount(250);
        return container;
    }

}
