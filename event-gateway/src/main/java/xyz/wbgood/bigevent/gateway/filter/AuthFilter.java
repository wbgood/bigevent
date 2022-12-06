package xyz.wbgood.bigevent.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import xyz.wbgood.bigevent.common.utils.JwtUtil;

import java.util.List;
import java.util.Map;

@Data
@Component
// 读取配置文件
@ConfigurationProperties(prefix = "jwt")
public class AuthFilter implements GlobalFilter, Ordered {

    //鉴权排除的接口
    private List<String> excludedUrls;
    //jwt校验密钥
    private String secret;


    //进行鉴权
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取token，在请求头的Authorization属性中
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        //获取当前请求的url
        String url = exchange.getRequest().getURI().getPath();
        //有些接口不需要鉴权，但是希望能够获取用户登录的信息，可以进行用户行为分析等功能
        //判断，是否是不需要鉴权的接口，不需要鉴权的接扣，鉴权失败也放行
        if (excludedUrls.contains(url)) {
            //不需要鉴权接口直接放行
            return chain.filter(exchange);
        }

        ServerHttpResponse response = exchange.getResponse();
        //判断token是否为空，就是非法请求
        // code 444 为此项目token鉴权失败专用
        if (StringUtils.isBlank(token)) {
            Map<String, Object> responseData = Maps.newHashMap();
            responseData.put("code", 444);
            responseData.put("msg", "非法请求，token为空");

            return responseError(response, responseData);
        }

        //对所有的接口都进行鉴权
        JwtUtil.VerifyResult verifyResult = JwtUtil.verifyJwt(token, secret);
        if (verifyResult.isValidate()) {
            //鉴权成功，获取authId并设置到请求头中
            exchange.getRequest().mutate().headers(h -> {
                h.add("authId", verifyResult.getAuthId());
            });

            //返回结果，放行，转发
            return chain.filter(exchange);
        }




        //鉴权失败，直接返回失败信息
        Map<String, Object> responseData = Maps.newHashMap();
        responseData.put("code", 444);
        responseData.put("msg", verifyResult.getMsg());

        return responseError(response, responseData);
    }

    //封装返回信息
    private Mono<Void> responseError(ServerHttpResponse response, Map<String, Object> responseData) {
        //将信息转为JSON
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data = new byte[0];
        try {
            data = objectMapper.writeValueAsBytes(responseData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //输出错误信息到页面
        DataBuffer buffer = response.bufferFactory().wrap(data);
        //设置状态码
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //设置响应头
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


    //过滤器执行的优先级
    @Override
    public int getOrder() {
        return 10000;
    }
}