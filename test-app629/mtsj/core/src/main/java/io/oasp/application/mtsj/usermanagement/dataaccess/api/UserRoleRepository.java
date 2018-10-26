package io.oasp.application.mtsj.usermanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface UserRoleRepository extends DefaultRepository<UserRoleEntity> {

  default Page<UserRoleEntity> findUserRoles(UserRoleSearchCriteriaTo criteria) {

    UserRoleEntity alias = newDslAlias();
    JPAQuery<UserRoleEntity> query = newDslQuery(alias);

    String name = criteria.getName();
    if ((name != null) && !name.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getName()), name, criteria.getNameOption());
    }
    Boolean active = criteria.getActive();
    if (active != null) {
      query.where(Alias.$(alias.getActive()).eq(active));
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
  public default void addOrderBy(JPAQuery<UserRoleEntity> query, UserRoleEntity alias, Sort sort) {

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
          case "active":
            if (next.isAscending()) {
              query.orderBy($(alias.getActive()).asc());
            } else {
              query.orderBy($(alias.getActive()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
