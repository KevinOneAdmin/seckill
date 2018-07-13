package com.mdiai.seckill.rabbitmq;

import com.mdiai.seckill.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mdiai.seckill.config.MQConfig.*;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/12  11:02
 * @Description 消息发送者
 */
@Service
public class MQSender {
    private static final Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;


    public void send(Object message){
        String msg = JsonUtils.beanToString(message);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>> send message: "+msg);
        amqpTemplate.convertAndSend(DIRECT_QUEUE,msg);
    }

    public void sendTopic(Object message){
        String msg = JsonUtils.beanToString(message);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>> send topic message: "+msg);
        amqpTemplate.convertAndSend(TOPIC_EXCHANGE,"topic.one",msg);
    }


    public void sendFanout(Object message){
        String msg = JsonUtils.beanToString(message);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>> send fanout message: "+msg);
        amqpTemplate.convertAndSend(FANOUT_EXCHANGE,"",msg);
    }

    public void sendHeaders(Object message){
        String msg = JsonUtils.beanToString(message);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>> send headers message: "+msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("h1","v1");
        properties.setHeader("h2","v2");
        Message o_msg = new Message(msg.getBytes(),properties);
        amqpTemplate.convertAndSend(HEADERS_EXCHANGE,"",o_msg);
    }

    public void sendSeckillMessage(SeckillMessage message) {
        String msg = JsonUtils.beanToString(message);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>> send seckill message: "+msg);
        amqpTemplate.convertAndSend(SECKILL_QUEUE,msg);
    }
}
