package com.laowan.rabbitmq.producer.enums;

import lombok.Getter;

/**
 * @program: rabbitmq
 * @description: 队列枚举
 * @author: laowan
 * @create: 2019-06-13 17:37
 **/
@Getter
public enum QueueEnum {

    //direct模式
    DIRECT_REQUEST("direct.request", "direct"),

    //固定队列应答模式
    FIXED_REQUEST("fixed.request", "fixed"),
    FIXED_RESPONSE("fixed.response", ""),

    //临时模式  消息发送到的队列
    TMP_REQUEST("tmp.request", "tmp")
   ;

    /**
     * 队列名称
     */
    private String name;
    /**
     * 队列路由键
     */
    private String routingKey;

    QueueEnum(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }
}
