package io.oasp.application.mtsj.ordermanagement.logic.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.oasp.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import io.oasp.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import io.oasp.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import io.oasp.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import io.oasp.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import io.oasp.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import io.oasp.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestSearchCriteriaTo;
import io.oasp.application.mtsj.dishmanagement.common.api.Ingredient;
import io.oasp.application.mtsj.dishmanagement.dataaccess.api.IngredientEntity;
import io.oasp.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishCto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishEto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import io.oasp.application.mtsj.general.common.api.constants.Roles;
import io.oasp.application.mtsj.general.logic.base.AbstractComponentFacade;
import io.oasp.application.mtsj.mailservice.Mail;
import io.oasp.application.mtsj.ordermanagement.common.api.exception.CancelNotAllowedException;
import io.oasp.application.mtsj.ordermanagement.common.api.exception.NoBookingException;
import io.oasp.application.mtsj.ordermanagement.common.api.exception.NoInviteException;
import io.oasp.application.mtsj.ordermanagement.common.api.exception.OrderAlreadyExistException;
import io.oasp.application.mtsj.ordermanagement.common.api.exception.WrongTokenException;
import io.oasp.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;
import io.oasp.application.mtsj.ordermanagement.dataaccess.api.OrderLineEntity;
import io.oasp.application.mtsj.ordermanagement.dataaccess.api.OrderLineRepository;
import io.oasp.application.mtsj.ordermanagement.dataaccess.api.OrderRepository;
import io.oasp.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderLineSearchCriteriaTo;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;

/**
 * Implementation of component interface of ordermanagement
 */
@Named
@Transactional
public class OrdermanagementImpl extends AbstractComponentFacade implements Ordermanagement {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(OrdermanagementImpl.class);

  /**
   * @see #getOrderDao()
   */
  @Inject
  private OrderRepository orderDao;

  /**
   * @see #getOrderLineDao()
   */
  @Inject
  private OrderLineRepository orderLineDao;

  @Inject
  private Bookingmanagement bookingManagement;

  @Inject
  private Dishmanagement dishManagement;

  @Inject
  private Mail mailService;

  @Value("${client.port}")
  private int clientPort;

  @Value("${server.servlet.context-path}")
  private String serverContextPath;

  @Value("${mythaistar.hourslimitcancellation}")
  private int hoursLimit;

  /**
   * The constructor.
   */
  public OrdermanagementImpl() {

    super();
  }

  @Override
  public OrderCto findOrder(Long id) {

    LOG.debug("Get Order with id {} from database.", id);
    OrderEntity entity = getOrderDao().find(id);
    OrderCto cto = new OrderCto();
    cto.setBooking(getBeanMapper().map(entity.getBooking(), BookingEto.class));
    cto.setHost(getBeanMapper().map(entity.getHost(), BookingEto.class));
    cto.setOrderLines(getBeanMapper().mapList(entity.getOrderLines(), OrderLineCto.class));
    cto.setOrder(getBeanMapper().map(entity, OrderEto.class));
    cto.setInvitedGuest(getBeanMapper().map(entity.getInvitedGuest(), InvitedGuestEto.class));
    return cto;
  }

  @RolesAllowed(Roles.WAITER)
  public Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo criteria) {

    return findOrderCtos(criteria);
  }

  @Override
  public Page<OrderCto> findOrderCtos(OrderSearchCriteriaTo criteria) {

    // criteria.limitMaximumPageSize(MAXIMUM_HIT_LIMIT);
    List<OrderCto> ctos = new ArrayList<>();
    Page<OrderEntity> orders = getOrderDao().findOrders(criteria);
    for (OrderEntity order : orders.getContent()) {
      OrderCto cto = new OrderCto();
      cto.setBooking(getBeanMapper().map(order.getBooking(), BookingEto.class));
      cto.setHost(getBeanMapper().map(order.getHost(), BookingEto.class));
      cto.setInvitedGuest(getBeanMapper().map(order.getInvitedGuest(), InvitedGuestEto.class));
      cto.setOrder(getBeanMapper().map(order, OrderEto.class));
      cto.setOrderLines(getBeanMapper().mapList(order.getOrderLines(), OrderLineCto.class));
      List<OrderLineCto> orderLinesCto = new ArrayList<>();
      for (OrderLineEntity orderLine : order.getOrderLines()) {
        OrderLineCto orderLineCto = new OrderLineCto();
        orderLineCto.setDish(getBeanMapper().map(orderLine.getDish(), DishEto.class));
        orderLineCto.setExtras(getBeanMapper().mapList(orderLine.getExtras(), IngredientEto.class));
        orderLineCto.setOrderLine(getBeanMapper().map(orderLine, OrderLineEto.class));
        orderLinesCto.add(orderLineCto);
      }
      cto.setOrderLines(orderLinesCto);
      ctos.add(cto);
    }

    Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), ctos.size());
    Page<OrderCto> pagListTo = new PageImpl<>(ctos, pagResultTo, pagResultTo.getPageSize());
    return pagListTo;
  }

  // @Override//TODO need to wrtie interface method
  public List<OrderCto> findOrders(Long idBooking) {

    // criteria.limitMaximumPageSize(MAXIMUM_HIT_LIMIT);
    List<OrderCto> ctos = new ArrayList<>();
    List<OrderEntity> orders = getOrderDao().findOrders(idBooking);
    for (OrderEntity order : orders) {
      OrderCto cto = new OrderCto();
      cto.setBooking(getBeanMapper().map(order.getBooking(), BookingEto.class));
      cto.setHost(getBeanMapper().map(order.getHost(), BookingEto.class));
      cto.setInvitedGuest(getBeanMapper().map(order.getInvitedGuest(), InvitedGuestEto.class));
      cto.setOrder(getBeanMapper().map(order, OrderEto.class));
      cto.setOrderLines(getBeanMapper().mapList(order.getOrderLines(), OrderLineCto.class));
      List<OrderLineCto> orderLinesCto = new ArrayList<>();
      for (OrderLineEntity orderLine : order.getOrderLines()) {
        OrderLineCto orderLineCto = new OrderLineCto();
        orderLineCto.setDish(getBeanMapper().map(orderLine.getDish(), DishEto.class));
        orderLineCto.setExtras(getBeanMapper().mapList(orderLine.getExtras(), IngredientEto.class));
        orderLineCto.setOrderLine(getBeanMapper().map(orderLine, OrderLineEto.class));
        orderLinesCto.add(orderLineCto);
      }
      cto.setOrderLines(orderLinesCto);
      ctos.add(cto);
    }

    // Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), ctos.size());
    // Page<OrderCto> pagListTo = new PageImpl<>(ctos, pagResultTo, pagResultTo.getPageSize());
    return ctos;
  }

  @Override
  public boolean deleteOrder(Long orderId) {

    OrderEntity order = getOrderDao().find(orderId);

    if (!cancellationAllowed(order)) {
      throw new CancelNotAllowedException();
    }
    OrderLineSearchCriteriaTo criteria = new OrderLineSearchCriteriaTo();
    criteria.setOrderId(order.getId());
    List<OrderLineEntity> orderLines = getOrderLineDao().findOrderLines(criteria).getContent();

    for (OrderLineEntity orderLine : orderLines) {
      getOrderLineDao().deleteById(orderLine.getId());
    }
    getOrderDao().delete(order);
    LOG.debug("The order with id '{}' has been deleted.", orderId);

    return true;
  }

  @Override
  public OrderEto saveOrder(OrderCto order) {

    Objects.requireNonNull(order, "order");
    List<OrderLineCto> linesCto = order.getOrderLines();
    List<OrderLineEntity> orderLineEntities = new ArrayList<>();
    for (OrderLineCto lineCto : linesCto) {
      OrderLineEntity orderLineEntity = getBeanMapper().map(lineCto, OrderLineEntity.class);
      orderLineEntity.setExtras(getBeanMapper().mapList(lineCto.getExtras(), IngredientEntity.class));
      orderLineEntity.setDishId(lineCto.getOrderLine().getDishId());
      orderLineEntity.setAmount(lineCto.getOrderLine().getAmount());
      orderLineEntity.setComment(lineCto.getOrderLine().getComment());
      orderLineEntities.add(orderLineEntity);
    }

    OrderEntity orderEntity = getBeanMapper().map(order, OrderEntity.class);
    String token = orderEntity.getBooking().getBookingToken();
    // initialize, validate orderEntity here if necessary
    orderEntity = getValidatedOrder(orderEntity.getBooking().getBookingToken(), orderEntity);
    orderEntity.setOrderLines(orderLineEntities);
    OrderEntity resultOrderEntity = getOrderDao().save(orderEntity);
    LOG.debug("Order with id '{}' has been created.", resultOrderEntity.getId());

    for (OrderLineEntity orderLineEntity : orderLineEntities) {
      orderLineEntity.setOrderId(resultOrderEntity.getId());
      OrderLineEntity resultOrderLine = getOrderLineDao().save(orderLineEntity);
      LOG.info("OrderLine with id '{}' has been created.", resultOrderLine.getId());
    }

    sendOrderConfirmationEmail(token, resultOrderEntity);

    return getBeanMapper().map(resultOrderEntity, OrderEto.class);
  }

  /**
   * Returns the field 'orderDao'.
   *
   * @return the {@link OrderDao} instance.
   */
  public OrderRepository getOrderDao() {

    return this.orderDao;
  }

  @Override
  public OrderLineEto findOrderLine(Long id) {

    LOG.debug("Get OrderLine with id {} from database.", id);
    return getBeanMapper().map(getOrderLineDao().find(id), OrderLineEto.class);
  }

  @Override
  public Page<OrderLineCto> findOrderLineCtos(OrderLineSearchCriteriaTo criteria) {

    // criteria.limitMaximumPageSize(MAXIMUM_HIT_LIMIT);
    Page<OrderLineEntity> orderlines = getOrderLineDao().findOrderLines(criteria);
    List<OrderLineCto> orderLinesCto = new ArrayList<>();
    for (OrderLineEntity orderline : orderlines.getContent()) {
      OrderLineCto orderLineCto = new OrderLineCto();
      orderLineCto.setOrderLine(getBeanMapper().map(this.orderLineDao.find(orderline.getId()), OrderLineEto.class));
      orderLineCto.setExtras(getBeanMapper().mapList(orderline.getExtras(), IngredientEto.class));
      orderLinesCto.add(orderLineCto);
    }

    Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), orderLinesCto.size());
    Page<OrderLineCto> pagListTo = new PageImpl<>(orderLinesCto, pagResultTo, pagResultTo.getPageSize());
    return pagListTo;
  }

  @Override
  public boolean deleteOrderLine(Long orderLineId) {

    OrderLineEntity orderLine = getOrderLineDao().find(orderLineId);
    getOrderLineDao().delete(orderLine);
    LOG.debug("The orderLine with id '{}' has been deleted.", orderLineId);
    return true;
  }

  @Override
  public OrderLineEto saveOrderLine(OrderLineEto orderLine) {

    Objects.requireNonNull(orderLine, "orderLine");
    OrderLineEntity orderLineEntity = getBeanMapper().map(orderLine, OrderLineEntity.class);

    // initialize, validate orderLineEntity here if necessary
    OrderLineEntity resultEntity = getOrderLineDao().save(orderLineEntity);
    LOG.debug("OrderLine with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, OrderLineEto.class);
  }

  /**
   * Returns the field 'orderLineDao'.
   *
   * @return the {@link OrderLineDao} instance.
   */
  public OrderLineRepository getOrderLineDao() {

    return this.orderLineDao;
  }

  private OrderEntity getValidatedOrder(String token, OrderEntity orderEntity) {

    // BOOKING VALIDATION
    if (getOrderType(token) == BookingType.COMMON) {
      BookingCto booking = getBookingbyToken(token);
      if (booking == null) {
        throw new NoBookingException();
      }
      List<OrderCto> currentOrders = getBookingOrders(booking.getBooking().getId());
      if (!currentOrders.isEmpty()) {
        throw new OrderAlreadyExistException();
      }
      orderEntity.setBookingId(booking.getBooking().getId());

      // GUEST VALIDATION
    } else if (getOrderType(token) == BookingType.INVITED) {

      InvitedGuestEto guest = getInvitedGuest(token);
      if (guest == null) {
        throw new NoInviteException();
      }
      List<OrderCto> currentGuestOrders = getInvitedGuestOrders(guest.getId());
      if (!currentGuestOrders.isEmpty()) {
        throw new OrderAlreadyExistException();
      }
      orderEntity.setBookingId(guest.getBookingId());
      orderEntity.setInvitedGuestId(guest.getId());
    }

    return orderEntity;
  }

  private BookingType getOrderType(String token) throws WrongTokenException {

    if (token.startsWith("CB_")) {
      return BookingType.COMMON;
    } else if (token.startsWith("GB_")) {
      return BookingType.INVITED;
    } else {
      throw new WrongTokenException();
    }
  }

  private BookingCto getBooking(String token) {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    criteria.setBookingToken(token);
    Page<BookingCto> booking = this.bookingManagement.findBookingCtos(criteria);
    return booking.getContent().isEmpty() ? null : booking.getContent().get(0);
  }

  private BookingCto getBookingbyToken(String token) {

    return this.bookingManagement.findByToken(token);
  }

  private List<OrderCto> getBookingOrders(Long idBooking) {

    // OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    // criteria.setBookingId(idBooking);

    return findOrders(idBooking);
  }

  private InvitedGuestEto getInvitedGuest(String token) {

    InvitedGuestSearchCriteriaTo criteria = new InvitedGuestSearchCriteriaTo();
    criteria.setGuestToken(token);
    Page<InvitedGuestEto> guest = this.bookingManagement.findInvitedGuestEtos(criteria);
    return guest.getContent().isEmpty() ? null : guest.getContent().get(0);
  }

  private List<OrderCto> getInvitedGuestOrders(Long idInvitedGuest) {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    criteria.setInvitedGuestId(idInvitedGuest);
    return findOrderCtos(criteria).getContent();
  }

  private void sendOrderConfirmationEmail(String token, OrderEntity order) {

    Objects.requireNonNull(token, "token");
    Objects.requireNonNull(order, "order");
    try {
      String emailTo = getBookingOrGuestEmail(token);
      StringBuilder mailContent = new StringBuilder();

      mailContent.append("MY THAI STAR").append("\n");
      mailContent.append("Hi ").append(emailTo).append("\n");
      mailContent.append("Your order has been created.").append("\n");
      mailContent.append(getContentFormatedWithCost(order)).append("\n");
      mailContent.append("\n").append("Link to cancel order: ");
      String link = "http://localhost:" + this.clientPort + "/booking/cancelOrder/" + order.getId();
      mailContent.append(link);
      this.mailService.sendMail(emailTo, "Order confirmation", mailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }
  }

  private String getContentFormatedWithCost(OrderEntity order) {

    // OrderLineSearchCriteriaTo criteria = new OrderLineSearchCriteriaTo();
    // criteria.setOrderId(order.getId());
    List<OrderLineEntity> orderLines = this.orderLineDao.findOrderLines(order.getId());

    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    BigDecimal finalPrice = BigDecimal.ZERO;
    for (OrderLineEntity orderLine : orderLines) {
      DishCto dishCto = this.dishManagement.findDish(orderLine.getDishId());
      List<IngredientEto> extras = dishCto.getExtras();
      Set<IngredientEto> set = new HashSet<>();
      set.addAll(extras);
      extras.clear();
      extras.addAll(set);
      // dish name
      BigDecimal linePrice = BigDecimal.ZERO;
      sb.append(dishCto.getDish().getName()).append(", x").append(orderLine.getAmount());
      // dish cost
      BigDecimal dishCost = dishCto.getDish().getPrice().multiply(new BigDecimal(orderLine.getAmount()));
      linePrice = dishCost;
      // dish selected extras
      sb.append(". Extras: ");
      for (Ingredient extra : extras) {
        for (Ingredient selectedExtra : orderLine.getExtras()) {
          if (extra.getId().equals(selectedExtra.getId())) {
            sb.append(extra.getName()).append(",");
            linePrice = linePrice.add(extra.getPrice());
            break;
          }
        }
      }

      // dish cost
      sb.append(" ==>").append(". Dish cost: ").append(linePrice.toString());
      sb.append("\n");
      // increase the finalPrice of the order
      finalPrice = finalPrice.add(linePrice);
    }

    return sb.append("Total Order cost: ").append(finalPrice.toString()).toString();
  }

  private String getBookingOrGuestEmail(String token) {

    // Get the Host email
    if (getOrderType(token) == BookingType.COMMON) {
      BookingCto booking = getBookingbyToken(token);
      if (booking == null) {
        throw new NoBookingException();
      }
      return booking.getBooking().getEmail();

      // Get the Guest email
    } else if (getOrderType(token) == BookingType.INVITED) {

      InvitedGuestEto guest = getInvitedGuest(token);
      if (guest == null) {
        throw new NoInviteException();
      }
      return guest.getEmail();
    } else {
      return null;
    }
  }

  private boolean cancellationAllowed(OrderEntity order) {

    BookingCto booking = this.bookingManagement.findBooking(order.getBookingId());
    Timestamp bookingTime = booking.getBooking().getBookingDate();
    Long bookingTimeMillis = bookingTime.getTime();
    Long cancellationLimit = bookingTimeMillis - (3600000 * this.hoursLimit);
    Long now = Timestamp.from(Instant.now()).getTime();

    return (now > cancellationLimit) ? false : true;
  }
}
