package xyz.wbgood.bigevent.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 注册登录服务
 * 用户权限相关服务
 * mapper 持久层
 * service 业务层
 * controller 控制层
 * entity model domain 实体层
 */
//springboot 启动类注解
@SpringBootApplication
//开启feign远程调用
@EnableFeignClients
//将此服务注册到注册中心
@EnableDiscoveryClient
// 扫描所有持久层 省去每mapper层使用@mapper注解
@MapperScan("xyz.wbgood.bigevent.auth.mapper")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("---------------------auth服务启动--------------------------");
    }
}
