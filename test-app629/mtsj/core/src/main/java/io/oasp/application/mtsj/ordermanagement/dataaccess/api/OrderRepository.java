package io.oasp.application.mtsj.ordermanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface OrderRepository extends DefaultRepository<OrderEntity> {

  @Query("SELECT orders FROM OrderEntity orders" //
      + " WHERE orders.booking.id = :idBooking")
  List<OrderEntity> findOrders(@Param("idBooking") Long idBooking);

  default Page<OrderEntity> findOrders(OrderSearchCriteriaTo criteria) {

    OrderEntity alias = newDslAlias();
    JPAQuery<OrderEntity> query = newDslQuery(alias);

    Long booking = criteria.getBookingId();
    if (booking != null && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getId()).eq(booking));
    }
    Long invitedGuest = criteria.getInvitedGuestId();
    if (invitedGuest != null && alias.getInvitedGuest() != null) {
      query.where(Alias.$(alias.getInvitedGuest().getId()).eq(invitedGuest));
    }
    String hostToken = criteria.getHostToken();
    if (hostToken != null && alias.getHost() != null) {
      /*
       * QueryUtil.get().whereString(query, $(alias.getBooking().getBookingToken()), hostToken,
       * criteria.getHostTokenOption());
       */
      query.where(Alias.$(alias.getBooking().getBookingToken()).eq(hostToken));
    }

    String email = criteria.getEmail();
    if ((email != null) && alias.getBooking() != null) {
      /*
       * QueryUtil.get().whereString(query, $(alias.getBooking().getEmail().toLowerCase()), email,
       * criteria.getEmailOption());
       */
      query.where(Alias.$(alias.getBooking().getEmail()).toLowerCase().like(email.toLowerCase()));

    }

    String bookingToken = criteria.getBookingToken();
    if ((bookingToken != null) && !bookingToken.isEmpty()) {
      /*
       * QueryUtil.get().whereString(query, $(alias.getBooking().getBookingToken().toLowerCase()), bookingToken,
       * criteria.getBookingTokenOption());
       */
      query.where(Alias.$(alias.getBooking().getBookingToken()).toLowerCase().eq(bookingToken));

    }
    addOrderBy(query, alias, criteria.getPageable().getSort());

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, true);
  }

  /**
   * Add sorting to the given query on the given alias
   *
   * @param query to add sorting to
   * @param alias to retrieve columns from for sorting
   * @param sort specification of sorting
   */
  public default void addOrderBy(JPAQuery<OrderEntity> query, OrderEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "idBooking":
            if (next.isAscending()) {
              query.orderBy($(alias.getBookingId()).asc());
            } else {
              query.orderBy($(alias.getBookingId()).desc());
            }
            break;
          case "idInvitedGuest":
            if (next.isAscending()) {
              query.orderBy($(alias.getInvitedGuestId()).asc());
            } else {
              query.orderBy($(alias.getInvitedGuestId()).desc());
            }
            break;
          case "hostToken":
            if (next.isAscending()) {
              query.orderBy($(alias.getBooking().getBookingToken()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getBooking().getBookingToken()).toLowerCase().desc());
            }
            break;
          case "bookingToken":
            if (next.isAscending()) {
              query.orderBy($(alias.getBooking().getBookingToken()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getBooking().getBookingToken()).toLowerCase().desc());
            }
            break;
          case "email":
            if (next.isAscending()) {
              query.orderBy($(alias.getBooking().getEmail()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getBooking().getEmail()).toLowerCase().desc());
            }
            break;
          case "bookingDate":
            if (next.isAscending()) {
              query.orderBy($(alias.getBooking().getBookingDate()).asc());
            } else {
              query.orderBy($(alias.getBooking().getBookingDate()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
