package com.yunus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaoyunfeng
 */
@SpringBootApplication
@RequestMapping
@RestController
public class BootStrap {
    public static void main(String[] args) {
        SpringApplication.run(BootStrap.class, args);
    }

    @GetMapping("/hello")
    public String say(String name) {
        return "hello " + name;
    }
}
