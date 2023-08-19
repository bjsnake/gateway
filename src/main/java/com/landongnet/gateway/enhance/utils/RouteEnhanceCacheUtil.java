package com.landongnet.gateway.enhance.utils;

import com.github.snake.rock.common.constants.CommonCons;

/**
 * @author snake
 */
public abstract class RouteEnhanceCacheUtil {

  private static final String BLACKLIST_CHANGE_KEY_PREFIX = "rock:route:blacklist";
  private static final String RATE_LIMIT_CACHE_KEY_PREFIX = "rock:route:rateLimit";
  private static final String RATE_LIMIT_COUNT_KEY_PREFIX = "rock:route:rateLimit:count";

  public static String getBlackListCacheKey(String ip) {
    if (CommonCons.LOCALHOST.equalsIgnoreCase(ip)) {
      ip = CommonCons.LOCALHOST_IP;
    }
    return String.format("%s%s", BLACKLIST_CHANGE_KEY_PREFIX, ip);
  }

  public static String getBlackListCacheKey() {
    return String.format("%s:all", BLACKLIST_CHANGE_KEY_PREFIX);
  }

  public static String getRateLimitCacheKey(String uri, String method) {
    return String.format("%s%s:%s:", RATE_LIMIT_CACHE_KEY_PREFIX, uri, method);
  }

  public static String getRateLimitCountKey(String uri, String ip) {
    return String.format("%s%s:%s:", RATE_LIMIT_COUNT_KEY_PREFIX, uri, ip);
  }
}
