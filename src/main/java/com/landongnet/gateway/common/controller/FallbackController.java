package com.landongnet.gateway.common.controller;

import com.github.snake.rock.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author snake
 * @description
 * @since 2023/8/19 00:41
 */
@Slf4j
@RestController
public class FallbackController {

    @RequestMapping("/fallback/{name}")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<R> systemFallback(@PathVariable String name) {
        String response = "服务访问超时，请稍后再试";
        log.error("{}，目标微服务：{}", response, name);
        return Mono.just(R.builder().code(503).message(response).build());
    }

}
