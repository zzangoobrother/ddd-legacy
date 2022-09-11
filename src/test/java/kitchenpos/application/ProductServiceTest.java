package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.FakeProfanityClient;
import kitchenpos.domain.InMemoryMenuRepository;
import kitchenpos.domain.InMemoryProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.ProfanityClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품")
class ProductServiceTest {

  private ProductRepository productRepository;

  private MenuRepository menuRepository;

  private ProfanityClient profanityClient;

  private ProductService productService;

  private Product product;

  @BeforeEach
  void setUp() {
    productRepository = new InMemoryProductRepository();
    menuRepository = new InMemoryMenuRepository();
    profanityClient = new FakeProfanityClient();

    productService = new ProductService(productRepository, menuRepository, profanityClient);

    product = new Product();
    product.setName("강정치킨");
    product.setPrice(BigDecimal.valueOf(17000));
  }

  @DisplayName("상품 등록")
  @Test
  void createProductTest() {
    Product result = productService.create(product);

    assertThat(result.getName()).isEqualTo("강정치킨");
    assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(17000));
  }

  @DisplayName("상품 이름 null 등록 에러")
  @Test
  void createProductNameNull() {
    product.setName(null);

    assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("상품 가격 음수 등록 에러")
  @Test
  void createProductPriceNegative() {
    product.setPrice(BigDecimal.valueOf(-1));

    assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("상품 가격을 수정한다.")
  @Test
  void updatePrice() {
    Product saveProduct = productService.create(product);

    Product chageProduct = new Product();
    chageProduct.setPrice(BigDecimal.valueOf(20000));

    Product result = productService.changePrice(saveProduct.getId(), chageProduct);

    assertThat(result.getName()).isEqualTo("강정치킨");
    assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(20000));
  }

  @DisplayName("상품 가격 음수 변경 에러")
  @Test
  void updatePriceNegative() {
    Product chageProduct = new Product();
    chageProduct.setPrice(BigDecimal.valueOf(-1));

    assertThatThrownBy(() -> productService.changePrice(product.getId(), chageProduct)).isInstanceOf(IllegalArgumentException.class);
  }
}