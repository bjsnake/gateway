package com.landongnet.gateway.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author snake
 * @description
 * @since 2023/8/19 00:41
 */
@RestController
public class IndexController {

    @RequestMapping("/")
    public Mono<String> index() {
        return Mono.just("You are welcome to use the gateway service, if you have any questions, please contact 【370696614@qq.com】");
    }
}
