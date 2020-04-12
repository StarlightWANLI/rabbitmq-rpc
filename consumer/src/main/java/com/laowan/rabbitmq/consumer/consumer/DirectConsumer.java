package com.laowan.rabbitmq.consumer.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.Map;

/**
 * @program: rabbitmq
 * @description: direct消费者
 * @author: wanli
 * @create: 2019-06-13 18:01
 **/
@Component
@RabbitListener(queues = "direct.request")
@Slf4j
public class DirectConsumer {

    @RabbitHandler
    public String onMessage(byte[] message,
                            @Headers Map<String, Object> headers,
                            Channel channel) {
        StopWatch stopWatch = new StopWatch("调用计时");
        stopWatch.start("rpc调用消费者耗时");
        String request = new String(message);
        String response = null;
        log.info("接收到的消息为：" + request);

        //模拟请求耗时3s
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response= this.sayHello(request);
        log.info("返回的消息为：" + response);
        stopWatch.stop();
        log.info(stopWatch.getLastTaskName()+stopWatch.getTotalTimeMillis()+"ms");
        return response;
    }

    public String sayHello(String name){
        return "hello " + name;
    }


}
