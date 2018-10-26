package io.oasp.application.mtsj.general.common.api.to;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.json.common.base.AbstractJsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class extends {@link AbstractJsonDeserializer} to deserialize {@link Pageable} objects from JSON
 *
 */
public class PageableJsonDeserializer extends AbstractJsonDeserializer<Pageable> {
  /**
   * Tries to deserialize a {@link Pageable} object from JSON and creates a new {@link PageRequest}.
   */
  @Override
  protected Pageable deserializeNode(JsonNode node) {

    Sort sort = null;
    int pageNumber = 1;
    int pageSize = 1;
    if (node.get("pageNumber") != null)
      pageNumber = node.get("pageNumber").asInt();
    if (node.get("pageSize") != null)
      pageSize = node.get("pageSize").asInt();
    if (isSortValid(node)) {
      JsonNode sortNode = node.get("sort");
      Iterator<JsonNode> iterator = sortNode.iterator();
      List<Order> sortingOrders = new ArrayList<>();
      while (iterator.hasNext()) {
        JsonNode next = iterator.next();
        String property = getOptionalValue(next, "property", String.class, null);
        String direction = getRequiredValue(next, "direction", String.class);
        if (property != null)
          sortingOrders.add(new Order(Direction.valueOf(direction), property));
      }
      if (sortingOrders != null && !sortingOrders.isEmpty())
        sort = Sort.by(sortingOrders);
    }
    return PageRequest.of(pageNumber, pageSize, sort);
  }

  /**
   * Checks whether the sort node is present (client may set a {@link Pageable} without a defined sort), is not null
   * (client may set a null sort) and it is an array
   *
   * @param node sort JsonNode to check whether it is valid
   * @return true if JsonNode is a valid sort
   */
  private boolean isSortValid(JsonNode node) {

    return node.get("sort") != null && !node.get("sort").isNull() && node.get("sort").isArray();
  }
}
