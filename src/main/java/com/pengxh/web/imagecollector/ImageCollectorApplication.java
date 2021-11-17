package com.pengxh.web.imagecollector;

import com.pengxh.web.imagecollector.socket.BootNettyServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * @author a203
 */
@Slf4j
@EnableAsync
@EnableScheduling
@MapperScan("com.pengxh.web.imagecollector.dao")
@SpringBootApplication
public class ImageCollectorApplication implements CommandLineRunner {

    @Resource
    private BootNettyServer bootNettyServer;

    public static void main(String[] args) {
        SpringApplication.run(ImageCollectorApplication.class, args);
        log.info("ImageCollectorApplication is Success!");
    }

    @Async
    @Override
    public void run(String... args) {
        /**
         * 使用异步注解方式启动netty服务端服务
         */
        bootNettyServer.bind();
    }
}
