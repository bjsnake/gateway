package com.landongnet.gateway.enhance.utils;

import com.github.snake.rock.common.model.QueryRequest;
import com.github.snake.rock.common.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

/**
 * @author snake
 */
public abstract class PageableExecutionUtil {

  public static <ROCK> Flux<ROCK> getPages(Query query, QueryRequest request, Class<ROCK> clazz,
                                           ReactiveMongoTemplate template) {
    Sort sort = Sort.by("id").descending();
    if (StringUtils.isNotBlank(request.getField()) && StringUtils.isNotBlank(request.getOrder())) {
      sort = SortUtil.SortCons.ORDER_ASC.equals(request.getOrder()) ?
        Sort.by(request.getField()).ascending() :
        Sort.by(request.getField()).descending();
    }
    Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);
    return template.find(query.with(pageable), clazz);
  }
}