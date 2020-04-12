package com.laowan.rabbitmq.producer.enums;

import lombok.Getter;

/**
 * @program: rabbitmq
 * @description: 交换器常量
 * @author: wanli
 * @create: 2019-06-13 17:36
 **/
@Getter
public enum ExchangeEnum {

    DIRECT_EXCHANGE("direct"),

    FIXED_EXCHANGE("fixed"),

    TMP_EXCHANGE("tmp");


    private String value;

    ExchangeEnum(String value) {
        this.value = value;
    }
}
