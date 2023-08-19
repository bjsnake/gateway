package com.landongnet.gateway.enhance.configuration;

import com.github.snake.rock.common.constants.CommonCons;
import com.landongnet.gateway.enhance.runner.RouteEnhanceRunner;
import com.landongnet.gateway.enhance.service.BlackListService;
import com.landongnet.gateway.enhance.service.RateLimitRuleService;
import com.landongnet.gateway.enhance.service.RouteEnhanceCacheService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author snake
 * @description
 * @since 2023/8/19 00:17
 */
@EnableAsync
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.landongnet.gateway.enhance.mapper")
@ConditionalOnProperty(name = "gateway.enhance", havingValue = "true")
public class RouteEnhanceConfigure {

    @Bean(CommonCons.ASYNC_POOL)
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("Rock-Gateway-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public ApplicationRunner fastRouteEnhanceRunner(RouteEnhanceCacheService cacheService,
                                                    BlackListService blackListService,
                                                    RateLimitRuleService rateLimitRuleService) {
        return new RouteEnhanceRunner(cacheService, blackListService, rateLimitRuleService);
    }
}
