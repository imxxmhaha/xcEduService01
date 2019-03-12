package com.xuecheng.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.rabbitmq.config.RabbitMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xxm
 * @create 2019-03-12 22:34
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String msg, Message message, Channel channel){
        System.out.println("receive message is:"+msg);
    }
}
