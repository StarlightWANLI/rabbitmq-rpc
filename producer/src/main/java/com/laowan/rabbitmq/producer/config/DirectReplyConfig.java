package com.laowan.rabbitmq.producer.config;

import com.laowan.rabbitmq.producer.enums.ExchangeEnum;
import com.laowan.rabbitmq.producer.enums.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: rpc-parent
 * @description: direct   rpc请求模式
 * @author: laowan
 * @create: 2020-04-09 18:05
 **/
@Configuration
@Slf4j
public class DirectReplyConfig {
    /**
     * 注意bean的名称是由方法名决定的，所以不能重复
     * @return
     */
    @Bean
    public Queue directRequest() {
        return new Queue(QueueEnum.DIRECT_REQUEST.getName(), true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(ExchangeEnum.DIRECT_EXCHANGE.getValue());
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directRequest()).to(directExchange()).with(QueueEnum.DIRECT_REQUEST.getRoutingKey());
    }


    /**
     * 当进行多个主题队列消费时，最好对每个单独定义RabbitTemplate，以便将各自的参数分别控制
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate directRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        //这一步非常关键
        template.setUseTemporaryReplyQueues(false);
        template.setReplyAddress("amq.rabbitmq.reply-to");
       // template.expectedQueueNames();
        template.setUserCorrelationId(true);

        //设置请求超时时间为10s
        template.setReplyTimeout(10000);
        return template;
    }

}
