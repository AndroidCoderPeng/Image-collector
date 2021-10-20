package com.pengxh.web.imagecollector;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author a203
 */
@Slf4j
@EnableScheduling
@MapperScan("com.pengxh.web.imagecollector.dao")
@SpringBootApplication
public class ImageCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageCollectorApplication.class, args);
        log.info("ImageCollectorApplication is Success!");
    }
}
