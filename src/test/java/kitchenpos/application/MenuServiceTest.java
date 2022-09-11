package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.FakeProfanityClient;
import kitchenpos.domain.InMemoryMenuGroupRepository;
import kitchenpos.domain.InMemoryMenuRepository;
import kitchenpos.domain.InMemoryProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.ProfanityClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹")
class MenuServiceTest {

  private MenuRepository menuRepository;
  private MenuGroupRepository menuGroupRepository;
  private ProductRepository productRepository;
  private ProfanityClient profanityClient;

  private MenuService menuService;

  private Product product;
  private MenuGroup menuGroup;
  private MenuProduct menuProduct;
  private Menu menu;

  @BeforeEach
  void setUp() {
    menuRepository = new InMemoryMenuRepository();
    menuGroupRepository = new InMemoryMenuGroupRepository();
    productRepository = new InMemoryProductRepository();
    profanityClient = new FakeProfanityClient();
    menuService = new MenuService(menuRepository, menuGroupRepository, productRepository, profanityClient);

    product = new Product();
    product.setId(UUID.randomUUID());
    product.setName("강정치킨");
    product.setPrice(BigDecimal.valueOf(17000));
    productRepository.save(product);

    menuGroup = new MenuGroup();
    menuGroup.setId(UUID.randomUUID());
    menuGroup.setName("추천메뉴");
    menuGroupRepository.save(menuGroup);

    menuProduct = new MenuProduct();
    menuProduct.setQuantity(2);
    menuProduct.setProductId(product.getId());

    menu = new Menu();
    menu.setId(UUID.randomUUID());
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(19000));
    menu.setDisplayed(true);
    menu.setMenuProducts(List.of(menuProduct));
    menu.setMenuGroupId(menuGroup.getId());
  }

  @DisplayName("메뉴 등록")
  @Test
  void createMenu() {
    Menu result = menuService.create(menu);

    assertThat(result.getName()).isEqualTo("후라이드+후라이드");
  }

  @DisplayName("메뉴 가격 null 등록 에러")
  @Test
  void createMenuPriceNull() {
    menu.setPrice(null);

    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 가격 음수 등록 에러")
  @Test
  void createMenuPriceNegative() {
    menu.setPrice(BigDecimal.valueOf(-1));

    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 메뉴상품 null 등록 에러")
  @Test
  void createMenuMenuProductNull() {
    menu.setMenuProducts(null);

    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 메뉴상품 빈값 등록 에러")
  @Test
  void createMenuMenuProductEmpty() {
    menu.setMenuProducts(List.of());

    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴상품 중 수량이 음수이면 에러")
  @Test
  void menuProductQuantityNegative() {
    menuProduct.setQuantity(-1);
    menu.setMenuProducts(List.of(menuProduct));

    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 가격이 메뉴상품의 총합보다 크면 에러")
  @Test
  void menuPriceMenuProductTotalPriceCompare() {
    menu.setPrice(BigDecimal.valueOf(50000));

    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 이름이 null이면 에러")
  @Test
  void menuNameNull() {
    menu.setName(null);

    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 가격 변경")
  @Test
  void chageMenuPrice() {
    Menu saveMenu = menuService.create(menu);

    Menu chageMenu = new Menu();
    chageMenu.setPrice(BigDecimal.valueOf(20000));

    Menu result = menuService.changePrice(saveMenu.getId(), chageMenu);

    assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(20000));
  }

  @DisplayName("메뉴 가격 음수 수정일 경우 에러")
  @Test
  void chageMenuPriceNegative() {
    Menu chageMenu = new Menu();
    chageMenu.setPrice(BigDecimal.valueOf(-1));

    assertThatThrownBy(() -> menuService.changePrice(menu.getId(), chageMenu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 가격 메뉴상품의 총합보다 크게 수정일 경우 에러")
  @Test
  void chageMenuPriceMenuProduceTotalCompare() {
    Menu saveMenu = menuService.create(menu);

    Menu chageMenu = new Menu();
    chageMenu.setPrice(BigDecimal.valueOf(50000));

    assertThatThrownBy(() -> menuService.changePrice(saveMenu.getId(), chageMenu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 노출로 변경")
  @Test
  void chageDisplay() {
    Menu saveMenu = menuService.create(menu);

    menu.setDisplayed(false);

    Menu result = menuService.display(saveMenu.getId());

    assertThat(result.isDisplayed()).isEqualTo(true);
  }

  @DisplayName("메뉴 숨김으로 변경")
  @Test
  void chageDisplayHide() {
    Menu saveMenu = menuService.create(menu);

    menu.setDisplayed(true);

    Menu result = menuService.hide(saveMenu.getId());

    assertThat(result.isDisplayed()).isEqualTo(false);
  }
}