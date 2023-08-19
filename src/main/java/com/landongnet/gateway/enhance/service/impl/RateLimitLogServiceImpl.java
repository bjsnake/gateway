package com.landongnet.gateway.enhance.service.impl;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.github.snake.rock.common.constants.StringPool;
import com.landongnet.gateway.enhance.entity.RateLimitLog;
import com.landongnet.gateway.enhance.mapper.RateLimitLogMapper;
import com.landongnet.gateway.enhance.service.RateLimitLogService;
import com.landongnet.gateway.enhance.utils.AddressUtil;
import com.landongnet.gateway.enhance.utils.PageableExecutionUtil;
import com.github.snake.rock.common.model.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author jc
 */
@Service
public class RateLimitLogServiceImpl implements RateLimitLogService {

  private RateLimitLogMapper rateLimitLogMapper;
  private ReactiveMongoTemplate template;

  @Autowired(required = false)
  public void setRateLimitLogMapper(RateLimitLogMapper rateLimitLogMapper) {
    this.rateLimitLogMapper = rateLimitLogMapper;
  }

  @Autowired(required = false)
  public void setTemplate(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @Override
  public Mono<RateLimitLog> create(RateLimitLog rateLimitLog) {
    rateLimitLog.setCreateTime(DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
    rateLimitLog.setLocation(AddressUtil.getCityInfo(rateLimitLog.getIp()));
    return rateLimitLogMapper.insert(rateLimitLog);
  }

  @Override
  public Flux<RateLimitLog> delete(String ids) {
    String[] idArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(ids, StringPool.COMMA);
    return rateLimitLogMapper.deleteByIdIn(Arrays.asList(idArray));
  }

  @Override
  public Flux<RateLimitLog> findPages(QueryRequest request, RateLimitLog rateLimitLog) {
    Query query = getQuery(rateLimitLog);
    return PageableExecutionUtil.getPages(query, request, RateLimitLog.class, template);
  }

  @Override
  public Mono<Long> findCount(RateLimitLog rateLimitLog) {
    Query query = getQuery(rateLimitLog);
    return template.count(query, RateLimitLog.class);
  }

  private Query getQuery(RateLimitLog rateLimitLog) {
    Query query = new Query();
    Criteria criteria = new Criteria();

    if (StringUtils.isNotBlank(rateLimitLog.getIp())) {
      criteria.and("ip").is(rateLimitLog.getIp());
    }
    if (StringUtils.isNotBlank(rateLimitLog.getRequestMethod())) {
      criteria.and("requestMethod").is(rateLimitLog.getRequestMethod());
    }
    if (StringUtils.isNotBlank(rateLimitLog.getRequestUri())) {
      criteria.and("requestUri").is(rateLimitLog.getRequestUri());
    }
    if (StringUtils.isNotBlank(rateLimitLog.getCreateTimeFrom())
      && StringUtils.isNotBlank(rateLimitLog.getCreateTimeTo())) {
      criteria.andOperator(
        Criteria.where("createTime").gt(rateLimitLog.getCreateTimeFrom()),
        Criteria.where("createTime").lt(rateLimitLog.getCreateTimeTo())
      );
    }
    query.addCriteria(criteria);
    return query;
  }
}
