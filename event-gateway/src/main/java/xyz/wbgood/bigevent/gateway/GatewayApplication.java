package xyz.wbgood.bigevent.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//springboot 启动类注解
@SpringBootApplication
@Slf4j
//将此服务注册到注册中心
@EnableDiscoveryClient
// 开启feign远程调用
@EnableFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        log.info("--------------------------------------------gateway网关启动了");
    }
}
