package com.mdiai.seckill.rabbitmq;

import com.mdiai.seckill.common.utils.JsonUtils;
import com.mdiai.seckill.config.MQConfig;
import com.mdiai.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/12  11:04
 * @Description 消息接收者
 */
@Service
public class MQRecelver {
    private static final Logger logger = LoggerFactory.getLogger(MQRecelver.class);

    @Autowired
    private SeckillService seckillService;

    @RabbitListener(queues = MQConfig.DIRECT_QUEUE)
    public void receive(String message){
        logger.info(">>>>>>>>>>>>>>>>>>>>> receive message: "+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE)
    public void receiveTopic(String message){
        logger.info(">>>>>>>>>>>>>>>>>>>>> receive topic message: "+message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE)
    public void receiveFanout(String message){
        logger.info(">>>>>>>>>>>>>>>>>>>>> receive fanout message: "+message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveHeaders(byte[] message){
        logger.info(">>>>>>>>>>>>>>>>>>>>> receive headers message: "+new String(message));
    }


    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiveSeckillMessage(String message){
        logger.info(">>>>>>>>>>>>>>>>>>>>> receive seckill message: "+message);
        SeckillMessage msg = JsonUtils.stringToBean(message, SeckillMessage.class);
        seckillService.asynchronousOrder(msg);
    }

}
