package kitchenpos.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {
  private Map<UUID, Product> products = new HashMap<>();

  @Override
  public Product save(Product product) {
    products.put(product.getId(), product);
    return product;
  }

  @Override
  public Optional<Product> findById(UUID productId) {
    return Optional.ofNullable(products.get(productId));
  }

  @Override
  public List<Product> findAll() {
    return new ArrayList<>(products.values());
  }

  @Override
  public List<Product> findAllByIdIn(List<UUID> ids) {
    return products.values()
        .stream()
        .filter(product -> ids.contains(product.getId()))
        .collect(toList());
  }
}
