package io.oasp.application.mtsj.dishmanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.math.BigDecimal;
import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface DishRepository extends DefaultRepository<DishEntity> {
  default Page<DishEntity> findDishs(DishSearchCriteriaTo criteria) {

    DishEntity alias = newDslAlias();
    JPAQuery<DishEntity> query = newDslQuery(alias);

    String searchBy = criteria.getSearchBy();
    if (searchBy != null) {
      query.where(Alias.$(alias.getName()).contains(searchBy).or(Alias.$(alias.getDescription()).contains(searchBy)));
    }

    BigDecimal price = criteria.getMaxPrice();
    if (price != null) {
      query.where(Alias.$(alias.getPrice()).lt(price));
    }
    if (criteria.getPageable() != null)
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
  public default void addOrderBy(JPAQuery<DishEntity> query, DishEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "name":
            if (next.isAscending()) {
              query.orderBy($(alias.getName()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getName()).toLowerCase().desc());
            }
            break;
          case "description":
            if (next.isAscending()) {
              query.orderBy($(alias.getDescription()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getDescription()).toLowerCase().desc());
            }
            break;
          case "price":
            if (next.isAscending()) {
              query.orderBy($(alias.getPrice()).asc());
            } else {
              query.orderBy($(alias.getPrice()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
