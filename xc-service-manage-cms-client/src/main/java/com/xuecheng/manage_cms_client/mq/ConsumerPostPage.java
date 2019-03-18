package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听MQ,接受页面发布消息
 * @author xxm
 * @create 2019-03-13 21:23
 */
@Component
@Slf4j
public class ConsumerPostPage {

    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg){
        log.info("接受到消息msg:{}",msg);
        // 解析消息
        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String) map.get("pageId");
        if(!StringUtils.isEmpty(pageId)){
            //调用service方法将页面从FridFs中下载到服务器
            pageService.savePageToServerPath(pageId);
        }
    }
}
