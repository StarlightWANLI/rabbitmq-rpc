package com.laowan.rabbitmq.producer;

import com.laowan.rabbitmq.producer.producer.DirectProducer;
import com.laowan.rabbitmq.producer.producer.FixedProducer;
import com.laowan.rabbitmq.producer.producer.TmpProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@SpringBootApplication
@RestController
public class ProducerApplication {

    @Autowired
    DirectProducer directProducer;

    @Autowired
    FixedProducer fixedProducer;

    @Autowired
    TmpProducer tmpProducer;



    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }



    @GetMapping("/direct")
    public String direct(String message) throws Exception {
        return directProducer.sendAndReceive(message);
    }

    @GetMapping("/fixed")
    public String fixed(String message) throws Exception {
        return fixedProducer.sendAndReceive(message);
    }

    @GetMapping("/tmp")
    public String tmp(String message) throws Exception {
        return tmpProducer.sendAndReceive(message);
    }
}
