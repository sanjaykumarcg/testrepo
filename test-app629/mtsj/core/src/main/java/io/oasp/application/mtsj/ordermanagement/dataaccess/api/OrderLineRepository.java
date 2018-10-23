package io.oasp.application.mtsj.ordermanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderLineSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface OrderLineRepository extends DefaultRepository<OrderLineEntity> {

  default Page<OrderLineEntity> findOrderLines(OrderLineSearchCriteriaTo criteria) {

    OrderLineEntity alias = newDslAlias();
    JPAQuery<OrderLineEntity> query = newDslQuery(alias);

    Long order = criteria.getOrderId();
    if (order != null && alias.getOrder() != null) {
      query.where(Alias.$(alias.getOrder().getId()).eq(order));
    }
    Long dish = criteria.getDishId();
    if (dish != null && alias.getDish() != null) {
      query.where(Alias.$(alias.getDish().getId()).eq(dish));
    }
    Integer amount = criteria.getAmount();
    if (amount != null) {
      query.where(Alias.$(alias.getAmount()).eq(amount));
    }
    String comment = criteria.getComment();
    if ((comment != null) && !comment.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getComment()), comment, criteria.getCommentOption());
    }

    addOrderBy(query, alias, criteria.getPageable().getSort());

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, false);
  }

  /**
   * Add sorting to the given query on the given alias
   *
   * @param query to add sorting to
   * @param alias to retrieve columns from for sorting
   * @param sort specification of sorting
   */
  public default void addOrderBy(JPAQuery<OrderLineEntity> query, OrderLineEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "dish":
            if (next.isAscending()) {
              query.orderBy($(alias.getDishId()).asc());
            } else {
              query.orderBy($(alias.getDishId()).desc());
            }
            break;
          case "amount":
            if (next.isAscending()) {
              query.orderBy($(alias.getAmount()).asc());
            } else {
              query.orderBy($(alias.getAmount()).desc());
            }
            break;
          case "comment":
            if (next.isAscending()) {
              query.orderBy($(alias.getComment()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getComment()).toLowerCase().desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
