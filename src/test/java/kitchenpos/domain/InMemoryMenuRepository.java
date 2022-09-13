package kitchenpos.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryMenuRepository implements MenuRepository {

  private Map<UUID, Menu> menus = new HashMap<>();

  @Override
  public Menu save(Menu menu) {
    menus.put(menu.getId(), menu);
    return menu;
  }

  @Override
  public List<Menu> findAll() {
    return new ArrayList<>(menus.values());
  }

  @Override
  public Optional<Menu> findById(UUID menuId) {
    return Optional.ofNullable(menus.get(menuId));
  }

  @Override
  public List<Menu> findAllByProductId(UUID productId) {
    return menus.values()
        .stream()
        .filter(
            menu -> menu.getMenuProducts()
                .stream()
                .anyMatch(menuProduct -> menuProduct.getProductId().equals(productId))
        )
        .collect(toList());
  }

  @Override
  public List<Menu> findAllByIdIn(List<UUID> ids) {
    return menus.values()
        .stream()
        .filter(menu -> ids.contains(menu.getId()))
        .collect(toList());
  }
}
