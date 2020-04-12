package com.laowan.rabbitmq.producer.producer;

import com.laowan.rabbitmq.producer.enums.ExchangeEnum;
import com.laowan.rabbitmq.producer.enums.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class TmpProducer {
    @Autowired
    private RabbitTemplate tmpRabbitTemplate;

    public String sendAndReceive(String request) throws TimeoutException {
        log.info("请求报文：{}" , request);
        //请求结果
        String result = null;
        //设置消息唯一id
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //直接发送message对象
        MessageProperties messageProperties = new MessageProperties();
        //过期时间10秒
        messageProperties.setExpiration("10000");
        messageProperties.setCorrelationId(correlationId.getId());
        Message message = new Message(request.getBytes(), messageProperties);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("tmp模式下rpc请求耗时");
        Message response = tmpRabbitTemplate.sendAndReceive(ExchangeEnum.TMP_EXCHANGE.getValue(), QueueEnum.TMP_REQUEST.getRoutingKey(), message, correlationId);

        stopWatch.stop();
        log.info(stopWatch.getLastTaskName()+"：" + stopWatch.getTotalTimeMillis());

        if (response != null) {
            result = new String(response.getBody());
            log.info("请求成功，返回的结果为：{}" , result);
        }else{
            log.error("请求超时");
            //为了方便jmeter测试，这里抛出异常
            throw  new TimeoutException("请求超时");
        }
        return result;
    }
}
