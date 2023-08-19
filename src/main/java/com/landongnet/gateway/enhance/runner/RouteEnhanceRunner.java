package com.landongnet.gateway.enhance.runner;

import com.landongnet.gateway.enhance.service.BlackListService;
import com.landongnet.gateway.enhance.service.RateLimitRuleService;
import com.landongnet.gateway.enhance.service.RouteEnhanceCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @author snake
 * @description
 * @since 2023/8/19 00:08
 */
@Slf4j
@RequiredArgsConstructor
public class RouteEnhanceRunner implements ApplicationRunner {

    private final RouteEnhanceCacheService cacheService;
    private final BlackListService blackListService;
    private final RateLimitRuleService rateLimitRuleService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("已开启网关增强功能：请求日志、黑名单&限流。");
        cacheService.saveAllBlackList(blackListService.findAll());
        cacheService.saveAllRateLimitRules(rateLimitRuleService.findAll());
    }
}
