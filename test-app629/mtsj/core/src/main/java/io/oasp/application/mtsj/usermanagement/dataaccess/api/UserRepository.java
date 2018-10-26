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

import io.oasp.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface UserRepository extends DefaultRepository<UserEntity> {

  default Page<UserEntity> findUsers(UserSearchCriteriaTo criteria) {

    UserEntity alias = newDslAlias();
    JPAQuery<UserEntity> query = newDslQuery(alias);

    String username = criteria.getUsername();
    if ((username != null) && !username.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getUsername()), username, criteria.getUsernameOption());
    }
    String password = criteria.getPassword();
    if ((password != null) && !password.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getPassword()), password, criteria.getPasswordOption());
    }
    String email = criteria.getEmail();
    if ((email != null) && !email.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getEmail()), email, criteria.getEmailOption());
    }
    Long userRole = criteria.getUserRoleId();
    if (userRole != null && alias.getUserRole() != null) {
      query.where(Alias.$(alias.getUserRole().getId()).eq(userRole));
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
  public default void addOrderBy(JPAQuery<UserEntity> query, UserEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "username":
            if (next.isAscending()) {
              query.orderBy($(alias.getUsername()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getUsername()).toLowerCase().desc());
            }
            break;
          case "password":
            if (next.isAscending()) {
              query.orderBy($(alias.getPassword()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getPassword()).toLowerCase().desc());
            }
            break;
          case "email":
            if (next.isAscending()) {
              query.orderBy($(alias.getEmail()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getEmail()).toLowerCase().desc());
            }
            break;
          case "userRole":
            if (next.isAscending()) {
              query.orderBy($(alias.getUserRoleId()).asc());
            } else {
              query.orderBy($(alias.getUserRoleId()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
