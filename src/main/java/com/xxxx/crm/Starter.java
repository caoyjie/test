package com.xxxx.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description:启动类
 *
 * @author Caoyujie
 * @version 2023.1.1
 * @date 2023/08/11 10:45:42
 */
@SpringBootApplication
@MapperScan("com.xxxx.crm.dao")
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}
