package com.bingo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    /**
     * @description 自动注册使用了@ServerEndpoint注解声明的Websocket endpoint。
     * 要注意，如果使用独立的servlet容器，而不是直接使用springboot的内置容器，就不要注入
     * ServerEndpointExporter，因为它将由容器自己提供和管理
     */
    @Bean
    ServerEndpointExporter serverEndpointExporter(){
        return  new ServerEndpointExporter();
    }
}
