package com.xuecheng.rabbitmq;

import com.xuecheng.rabbitmq.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xxm
 * @create 2019-03-12 22:26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitTemplateTest {

    //使用RabbitTemplate发送消息
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendEmail(){
        String message = "send email message to user";
        /**
         * 参数
         * 1.交换机名称
         * 2.routingKey
         * 3.消息内容
         */
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
    }
}
