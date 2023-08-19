package com.landongnet.gateway.enhance.service.impl;

import com.github.snake.rock.common.utils.JsonUtils;
import com.github.snake.rock.redis.service.ICacheService;
import com.landongnet.gateway.enhance.entity.BlackList;
import com.landongnet.gateway.enhance.entity.RateLimitRule;
import com.landongnet.gateway.enhance.service.RouteEnhanceCacheService;
import com.landongnet.gateway.enhance.utils.RouteEnhanceCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Set;

/**
 * @author jc
 */
@Slf4j
@Service
public class RouteEnhanceCacheServiceImpl implements RouteEnhanceCacheService {

  private ICacheService cacheService;

  @Autowired(required = false)
  public void setRedisService(ICacheService cacheService) {
    this.cacheService = cacheService;
  }

  @Override
  public void saveAllBlackList(Flux<BlackList> blackList) {
    blackList.subscribe(b -> {
      String key = StringUtils.isNotBlank(b.getIp()) ?
        RouteEnhanceCacheUtil.getBlackListCacheKey(b.getIp()) :
        RouteEnhanceCacheUtil.getBlackListCacheKey();
      String value = JsonUtils.objectCovertToJson(b);
      cacheService.listPush(key, value);
    });
    log.info("Cache blacklist into redis >>>");
  }

  @Override
  public void saveBlackList(BlackList blackList) {
    String key = StringUtils.isNotBlank(blackList.getIp()) ?
      RouteEnhanceCacheUtil.getBlackListCacheKey(blackList.getIp()) :
      RouteEnhanceCacheUtil.getBlackListCacheKey();
    cacheService.listPush(key, JsonUtils.objectCovertToJson(blackList));
  }

  @Override
  public Set<Object> getBlackList(String ip) {
    String key = RouteEnhanceCacheUtil.getBlackListCacheKey(ip);
    return (Set<Object>) cacheService.getList(key);

  }

  @Override
  public Set<Object> getBlackList() {
    String key = RouteEnhanceCacheUtil.getBlackListCacheKey();
    return (Set<Object>) cacheService.getList(key);
  }

  @Override
  public void removeBlackList(BlackList blackList) {
    String key = StringUtils.isNotBlank(blackList.getIp()) ?
      RouteEnhanceCacheUtil.getBlackListCacheKey(blackList.getIp()) :
      RouteEnhanceCacheUtil.getBlackListCacheKey();
    cacheService.delete(key,JsonUtils.objectCovertToJson(blackList));
  }

  @Override
  public void saveAllRateLimitRules(Flux<RateLimitRule> rateLimitRules) {
    rateLimitRules.subscribe(r -> {
      String key = RouteEnhanceCacheUtil.getRateLimitCacheKey(r.getRequestUri(), r.getRequestMethod());
      String value = JsonUtils.objectCovertToJson(r);
      cacheService.set(key, value);
    });
    log.info("Cache rate limit rules into redis >>>");
  }

  @Override
  public void saveRateLimitRule(RateLimitRule rateLimitRule) {
    String key = RouteEnhanceCacheUtil.getRateLimitCacheKey(rateLimitRule.getRequestUri(), rateLimitRule.getRequestMethod());
    cacheService.set(key, JsonUtils.objectCovertToJson(rateLimitRule));
  }


  @Override
  public Object getRateLimitRule(String uri, String method) {
    String key = RouteEnhanceCacheUtil.getRateLimitCacheKey(uri, method);
    return cacheService.get(key);
  }

  @Override
  public int getCurrentRequestCount(String uri, String ip) {
    String key = RouteEnhanceCacheUtil.getRateLimitCountKey(uri, ip);
    return cacheService.hasKey(key) ? (int) cacheService.get(key) : 0;
  }

  @Override
  public void removeRateLimitRule(RateLimitRule rateLimitRule) {
    String key = RouteEnhanceCacheUtil.getRateLimitCacheKey(rateLimitRule.getRequestUri(), rateLimitRule.getRequestMethod());
    cacheService.delete(key);
  }

  @Override
  public void setCurrentRequestCount(String uri, String ip, Long time) {
    String key = RouteEnhanceCacheUtil.getRateLimitCountKey(uri, ip);
    cacheService.set(key, 1, time);
  }

  @Override
  public void incrCurrentRequestCount(String uri, String ip) {
    String key = RouteEnhanceCacheUtil.getRateLimitCountKey(uri, ip);
    cacheService.increment(key, 1L);
  }
}
