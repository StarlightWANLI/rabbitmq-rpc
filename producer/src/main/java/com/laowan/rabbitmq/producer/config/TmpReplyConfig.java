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
 * @description: Temporary应答模式
 * @author: laowan
 * @create: 2020-04-09 18:05
 **/
@Configuration
@Slf4j
public class TmpReplyConfig {
    @Bean
    public Queue tmpRequest() {
        return new Queue(QueueEnum.TMP_REQUEST.getName(), true);
    }

    @Bean
    public DirectExchange tmpExchange() {
        return new DirectExchange(ExchangeEnum.TMP_EXCHANGE.getValue());
    }

    @Bean
    public Binding tmpBinding() {
        return BindingBuilder.bind(tmpRequest()).to(tmpExchange()).with(QueueEnum.TMP_REQUEST.getRoutingKey());
    }
    


    @Bean
    public RabbitTemplate tmpRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        template.setUseTemporaryReplyQueues(true);
        template.setUserCorrelationId(true);

        //设置请求超时时间为10s
        template.setReplyTimeout(10000);

        return template;
    }


}
