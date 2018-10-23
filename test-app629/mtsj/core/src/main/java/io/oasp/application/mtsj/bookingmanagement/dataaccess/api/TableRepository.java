package io.oasp.application.mtsj.bookingmanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.bookingmanagement.logic.api.to.TableSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface TableRepository extends DefaultRepository<TableEntity> {

  default Page<TableEntity> findTables(TableSearchCriteriaTo criteria) {

    TableEntity alias = newDslAlias();
    JPAQuery<TableEntity> query = newDslQuery(alias);

    Integer seatsNumber = criteria.getSeatsNumber();
    if (seatsNumber != null) {
      query.where(Alias.$(alias.getSeatsNumber()).eq(seatsNumber));
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
  public default void addOrderBy(JPAQuery<TableEntity> query, TableEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "seatsNumber":
            if (next.isAscending()) {
              query.orderBy($(alias.getSeatsNumber()).asc());
            } else {
              query.orderBy($(alias.getSeatsNumber()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
