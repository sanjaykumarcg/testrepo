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

import io.oasp.application.mtsj.dishmanagement.logic.api.to.IngredientSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface IngredientRepository extends DefaultRepository<IngredientEntity> {

  default Page<IngredientEntity> findIngredients(IngredientSearchCriteriaTo criteria) {

    IngredientEntity alias = newDslAlias();
    JPAQuery<IngredientEntity> query = newDslQuery(alias);

    String name = criteria.getName();
    if ((name != null) && !name.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getName()), name, criteria.getNameOption());
    }
    String description = criteria.getDescription();
    if ((description != null) && !description.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getDescription()), description, criteria.getDescriptionOption());
    }

    BigDecimal price = criteria.getPrice();
    if (price != null) {
      query.where(Alias.$(alias.getPrice()).eq(price));
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
  public default void addOrderBy(JPAQuery<IngredientEntity> query, IngredientEntity alias, Sort sort) {

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
