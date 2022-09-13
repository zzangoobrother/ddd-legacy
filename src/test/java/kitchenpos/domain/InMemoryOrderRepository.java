package kitchenpos.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryOrderRepository implements OrderRepository {

  private Map<UUID, Order> orders = new HashMap<>();

  @Override
  public Order save(Order order) {
    orders.put(order.getId(), order);
    return order;
  }

  @Override
  public Optional<Order> findById(UUID orderId) {
    return Optional.ofNullable(orders.get(orderId));
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(orders.values());
  }

  @Override
  public boolean existsByOrderTableAndStatusNot(OrderTable orderTable, OrderStatus status) {
    return orders.values()
        .stream()
        .filter(order -> order.getOrderTable().equals(orderTable))
        .anyMatch(order -> order.getStatus() == status);
  }
}
