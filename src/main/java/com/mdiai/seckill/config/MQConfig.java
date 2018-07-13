package com.mdiai.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/12  11:05
 * @Description MQ配置
 */
@Configuration
public class MQConfig {


    public static final String SECKILL_QUEUE = "seckill-queue";
    public static final String DIRECT_QUEUE = "direct-queue";
    public static final String TOPIC_QUEUE = "topic-queue";
    public static final String HEADERS_QUEUE = "headers-queue";
    public static final String FANOUT_QUEUE = "fanout-queue";
    public static final String HEADERS_EXCHANGE = "headers-exchange";
    public static final String FANOUT_EXCHANGE = "fanout-exchange";
    public static final String TOPIC_EXCHANGE = "topic-exchange";
    public static final String ROUTING_KEY = "topic.*";




    @Bean
    public Queue seckillQueue(){
        return new Queue(SECKILL_QUEUE,true);
    }

    /**
     * Direct 模式
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(DIRECT_QUEUE,true);
    }


    /**
     * Topic 模式
     * @return
     */
    @Bean
    public Queue topicQueue(){
        return new Queue(TOPIC_QUEUE,true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding(){
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(ROUTING_KEY);
    }



    /**
     * Fanout 广播模式
     * @return
     */
    @Bean
    public Queue fanoutQueue(){
        return new Queue(FANOUT_QUEUE,true);
    }


    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding(){
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBindingOne(){
        return BindingBuilder.bind(topicQueue()).to(fanoutExchange());
    }


    /**
     * Headers 模式
     * @return
     */
    @Bean
    public Queue headerQueue(){
        return new Queue(HEADERS_QUEUE,true);
    }


    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }


    @Bean
    public Binding HeadersBinding(){
        Map<String,Object> headers = new HashMap<>(2);
        headers.put("h1","v1");
        headers.put("h2","v2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(headers).match();
    }


}
