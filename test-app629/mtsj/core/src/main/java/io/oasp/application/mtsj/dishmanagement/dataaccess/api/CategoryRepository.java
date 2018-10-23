package io.oasp.application.mtsj.dishmanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.dishmanagement.logic.api.to.CategorySearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface CategoryRepository extends DefaultRepository<CategoryEntity> {

  default Page<CategoryEntity> findCategorys(CategorySearchCriteriaTo criteria) {

    CategoryEntity alias = newDslAlias();
    JPAQuery<CategoryEntity> query = newDslQuery(alias);

    String name = criteria.getName();
    if ((name != null) && !name.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getName()), name, criteria.getNameOption());
    }
    String description = criteria.getDescription();
    if ((description != null) && !description.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getDescription()), description, criteria.getDescriptionOption());
    }
    int showOrder = criteria.getShowOrder();
    query.where(Alias.$(alias.getShowOrder()).eq(showOrder));

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
  public default void addOrderBy(JPAQuery<CategoryEntity> query, CategoryEntity alias, Sort sort) {

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
          case "showOrder":
            if (next.isAscending()) {
              query.orderBy($(alias.getShowOrder()).asc());
            } else {
              query.orderBy($(alias.getShowOrder()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
