package io.oasp.application.mtsj.ordermanagement.service.impl.rest;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;

import io.oasp.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderLineSearchCriteriaTo;
import io.oasp.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;
import io.oasp.application.mtsj.ordermanagement.service.api.rest.OrdermanagementRestService;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Ordermanagement}.
 */
@Named("OrdermanagementRestService")
public class OrdermanagementRestServiceImpl implements OrdermanagementRestService {

  @Inject
  private Ordermanagement ordermanagement;

  @Override
  public OrderCto getOrder(long id) {

    return this.ordermanagement.findOrder(id);
  }

  @Override
  public OrderEto saveOrder(OrderCto order) {

    return this.ordermanagement.saveOrder(order);
  }

  @Override
  public boolean deleteOrder(long id) {

    return this.ordermanagement.deleteOrder(id);
  }

  @Override
  public void cancelOrder(long id) {

    this.ordermanagement.deleteOrder(id);

  }

  @Override
  public Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrdersByPost(searchCriteriaTo);
  }

  @Override
  public OrderLineEto getOrderLine(long id) {

    return this.ordermanagement.findOrderLine(id);
  }

  @Override
  public OrderLineEto saveOrderLine(OrderLineEto orderline) {

    return this.ordermanagement.saveOrderLine(orderline);
  }

  @Override
  public void deleteOrderLine(long id) {

    this.ordermanagement.deleteOrderLine(id);
  }

  @Override
  public Page<OrderLineCto> findOrderLinesByPost(OrderLineSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrderLineCtos(searchCriteriaTo);
  }

}
