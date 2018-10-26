package io.oasp.application.mtsj.bookingmanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.sql.Timestamp;
import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface InvitedGuestRepository extends DefaultRepository<InvitedGuestEntity> {

  default Page<InvitedGuestEntity> findInvitedGuests(InvitedGuestSearchCriteriaTo criteria) {

    InvitedGuestEntity alias = newDslAlias();
    JPAQuery<InvitedGuestEntity> query = newDslQuery(alias);

    Long booking = criteria.getBookingId();
    if (booking != null && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getId()).eq(booking));
    }
    String guestToken = criteria.getGuestToken();
    if ((guestToken != null) && !guestToken.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getGuestToken()), guestToken, criteria.getGuestTokenOption());
    }
    String email = criteria.getEmail();
    if ((email != null) && !email.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getEmail()), email, criteria.getEmailOption());
    }
    Boolean accepted = criteria.getAccepted();
    if (accepted != null) {
      query.where(Alias.$(alias.getAccepted()).eq(accepted));
    }
    Timestamp modificationDate = criteria.getModificationDate();
    if (modificationDate != null) {
      query.where(Alias.$(alias.getModificationDate()).eq(modificationDate));
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
  public default void addOrderBy(JPAQuery<InvitedGuestEntity> query, InvitedGuestEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "guestToken":
            if (next.isAscending()) {
              query.orderBy($(alias.getGuestToken()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getGuestToken()).toLowerCase().desc());
            }
            break;
          case "email":
            if (next.isAscending()) {
              query.orderBy($(alias.getEmail()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getEmail()).toLowerCase().desc());
            }
            break;
          case "accepted":
            if (next.isAscending()) {
              query.orderBy($(alias.getAccepted()).asc());
            } else {
              query.orderBy($(alias.getAccepted()).desc());
            }
            break;
          case "modificationDate":
            if (next.isAscending()) {
              query.orderBy($(alias.getModificationDate()).asc());
            } else {
              query.orderBy($(alias.getModificationDate()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
