package com.xuecheng.search;

import com.xuecheng.search.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xxm
 * @create 2019-02-23 9:46
 */

/**
 * SpringBoot 默认支持两种技术来和ES交互;
 * 1、Jest (默认不生效)
 *      需要导入jest工具包:io.searchbox.client.JestClient
 * 2、SpringData ElasticSearch
 *      1)、Client  节点信息:clusterNodes;clusterName
 *      2)、ElasticsearchTemplate
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.search")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.search"})//扫描本项目下的所有类
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common下的所有类
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
