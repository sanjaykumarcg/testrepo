package io.oasp.application.mtsj.bookingmanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import java.sql.Timestamp;
import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import io.oasp.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;

/**
 * @author vapadwal
 *
 */
public interface BookingRepository extends DefaultRepository<BookingEntity> {

  @Query("SELECT booking FROM BookingEntity booking" //
      + " WHERE booking.bookingToken = :token")
  BookingEntity findByToken(@Param("token") String token);

  default Page<BookingEntity> findBookings(BookingSearchCriteriaTo criteria) {

    BookingEntity alias = newDslAlias();
    JPAQuery<BookingEntity> query = newDslQuery(alias);

    String name = criteria.getName();
    if ((name != null) && !name.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getName()), name, criteria.getNameOption());
    }
    String bookingToken = criteria.getBookingToken();
    if (bookingToken != null && !bookingToken.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getBookingToken()), bookingToken, criteria.getBookingTokenOption());
    }
    String comment = criteria.getComment();
    if (comment != null && !comment.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getComment()), comment, criteria.getCommentOption());
    }
    Timestamp bookingDate = criteria.getBookingDate();
    if (bookingDate != null) {
      query.where(Alias.$(alias.getBookingDate()).eq(bookingDate));
      /*
       * QueryUtil.get().whereString(query, $(alias.getBookingDate().toString()), bookingDate.toString(),
       * criteria.getCommentOption());
       */

    }
    Timestamp expirationDate = criteria.getExpirationDate();
    if (expirationDate != null) {
      query.where(Alias.$(alias.getExpirationDate()).eq(expirationDate));
    }
    Timestamp creationDate = criteria.getCreationDate();
    if (creationDate != null) {
      query.where(Alias.$(alias.getCreationDate()).eq(creationDate));
    }
    String email = criteria.getEmail();
    if (email != null && !email.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getEmail()), email, criteria.getEmailOption());

    }
    Boolean canceled = criteria.getCanceled();
    if (canceled != null) {
      query.where(Alias.$(alias.getCanceled()).eq(canceled));
    }
    BookingType bookingType = criteria.getBookingType();
    if (bookingType != null) {
      query.where(Alias.$(alias.getBookingType()).eq(bookingType));
    }
    Long table = criteria.getTableId();
    if (table != null && alias.getTable() != null) {
      query.where(Alias.$(alias.getTable().getId()).eq(table));
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
  public default void addOrderBy(JPAQuery<BookingEntity> query, BookingEntity alias, Sort sort) {

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
          case "bookingToken":
            if (next.isAscending()) {
              query.orderBy($(alias.getBookingToken()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getBookingToken()).toLowerCase().desc());
            }
            break;
          case "comment":
            if (next.isAscending()) {
              query.orderBy($(alias.getComment()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getComment()).toLowerCase().desc());
            }
            break;
          case "bookingDate":
            if (next.isAscending()) {
              query.orderBy($(alias.getBookingDate()).asc());
            } else {
              query.orderBy($(alias.getBookingDate()).desc());
            }
            break;
          case "expirationDate":
            if (next.isAscending()) {
              query.orderBy($(alias.getExpirationDate()).asc());
            } else {
              query.orderBy($(alias.getExpirationDate()).desc());
            }
            break;
          case "creationDate":
            if (next.isAscending()) {
              query.orderBy($(alias.getCreationDate()).asc());
            } else {
              query.orderBy($(alias.getCreationDate()).desc());
            }
            break;
          case "email":
            if (next.isAscending()) {
              query.orderBy($(alias.getEmail()).toLowerCase().asc());
            } else {
              query.orderBy($(alias.getEmail()).toLowerCase().desc());
            }
            break;
          case "canceled":
            if (next.isAscending()) {
              query.orderBy($(alias.getCanceled()).asc());
            } else {
              query.orderBy($(alias.getCanceled()).desc());
            }
            break;
          case "bookingType":
            if (next.isAscending()) {
              query.orderBy($(alias.getBookingType()).asc());
            } else {
              query.orderBy($(alias.getBookingType()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }
}
