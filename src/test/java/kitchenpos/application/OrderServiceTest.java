package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.InMemoryMenuRepository;
import kitchenpos.domain.InMemoryOrderRepository;
import kitchenpos.domain.InMemoryOrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderType;
import kitchenpos.domain.Product;
import kitchenpos.infra.KitchenridersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블")
class OrderServiceTest {

  private OrderRepository orderRepository;
  private MenuRepository menuRepository;
  private OrderTableRepository orderTableRepository;
  private KitchenridersClient kitchenridersClient;

  private OrderService orderService;

  private MenuGroup 추천메뉴;
  private Product 강정치킨;
  private OrderTable 테이블_1번;
  private Menu menu;

  private MenuProduct menuProduct;

  private OrderLineItem orderLineItem;

  private Order orderEatIn;
  private Order orderTakeOut;
  private Order orderDelivery;

  @BeforeEach
  void setUp() {
    orderRepository = new InMemoryOrderRepository();
    menuRepository = new InMemoryMenuRepository();
    orderTableRepository = new InMemoryOrderTableRepository();
    kitchenridersClient = new KitchenridersClient();

    orderService = new OrderService(orderRepository, menuRepository, orderTableRepository, kitchenridersClient);

    추천메뉴 = new MenuGroup();
    추천메뉴.setId(UUID.randomUUID());
    추천메뉴.setName("추천메뉴");

    강정치킨 = new Product();
    강정치킨.setId(UUID.randomUUID());
    강정치킨.setName("강정치킨");
    강정치킨.setPrice(BigDecimal.valueOf(17000));

    테이블_1번 = new OrderTable();
    테이블_1번.setId(UUID.randomUUID());
    테이블_1번.setName("1번");
    테이블_1번.setOccupied(true);
    orderTableRepository.save(테이블_1번);

    menuProduct = new MenuProduct();
    menuProduct.setQuantity(2);
    menuProduct.setProduct(강정치킨);

    menu = new Menu();
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(19000));
    menu.setDisplayed(true);
    menu.setMenuProducts(List.of(menuProduct));
    menu.setMenuGroup(추천메뉴);
    menu.setMenuGroupId(추천메뉴.getId());
    menuRepository.save(menu);

    orderLineItem = new OrderLineItem();
    orderLineItem.setMenu(menu);
    orderLineItem.setMenuId(menu.getId());
    orderLineItem.setPrice(BigDecimal.valueOf(19000));
    orderLineItem.setQuantity(2);

    orderEatIn = new Order();
    orderEatIn.setType(OrderType.EAT_IN);
    orderEatIn.setOrderLineItems(List.of(orderLineItem));
    orderEatIn.setOrderTable(테이블_1번);
    orderEatIn.setOrderTableId(테이블_1번.getId());
    orderEatIn.setStatus(OrderStatus.WAITING);

    orderTakeOut = new Order();
    orderTakeOut.setType(OrderType.TAKEOUT);
    orderTakeOut.setOrderLineItems(List.of(orderLineItem));
    orderTakeOut.setStatus(OrderStatus.WAITING);

    orderDelivery = new Order();
    orderDelivery.setType(OrderType.DELIVERY);
    orderDelivery.setOrderLineItems(List.of(orderLineItem));
    orderDelivery.setDeliveryAddress("경기도 남양주시");
    orderDelivery.setStatus(OrderStatus.WAITING);
  }

  @DisplayName("주문 매장식사 등록")
  @Test
  void createOrderEatIn() {
    Order result = orderService.create(orderEatIn);

    assertThat(result.getType()).isEqualTo(OrderType.EAT_IN);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.WAITING);
  }

  @DisplayName("주문 포장 등록")
  @Test
  void createOrderTakeOut() {
    Order result = orderService.create(orderTakeOut);

    assertThat(result.getType()).isEqualTo(OrderType.TAKEOUT);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.WAITING);
  }

  @DisplayName("주문 배달 등록")
  @Test
  void createOrderDelivery() {
    Order result = orderService.create(orderTakeOut);

    assertThat(result.getType()).isEqualTo(OrderType.TAKEOUT);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.WAITING);
  }

  @DisplayName("주문 타입 null 에러")
  @Test
  void orderTypeNull() {
    orderTakeOut.setType(null);

    assertThatThrownBy(() -> orderService.create(orderTakeOut)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 아이템 null 에러")
  @Test
  void orderLineItemNull() {
    orderTakeOut.setOrderLineItems(null);

    assertThatThrownBy(() -> orderService.create(orderTakeOut)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("노출되지 않은 메뉴는 선택하면 에러")
  @Test
  void menuDisplayHideSelect() {
    menu.setDisplayed(false);

    assertThatThrownBy(() -> orderService.create(orderTakeOut)).isInstanceOf(IllegalStateException.class);
  }

  @DisplayName("주문 배달 수락")
  @Test
  void orderDeliveryAccept() {
    Order saveOrder = orderService.create(orderDelivery);

    Order result = orderService.accept(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.DELIVERY);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
  }

  @DisplayName("주문 매장식사 수락")
  @Test
  void orderEatInAccept() {
    Order saveOrder = orderService.create(orderEatIn);

    Order result = orderService.accept(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.EAT_IN);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
  }

  @DisplayName("주문 포장 수락")
  @Test
  void orderTakeOutAccept() {
    Order saveOrder = orderService.create(orderTakeOut);

    Order result = orderService.accept(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.TAKEOUT);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
  }

  @DisplayName("주문 배달 서빙")
  @Test
  void orderDeliveryServe() {
    Order saveOrder = orderService.create(orderDelivery);

    saveOrder.setStatus(OrderStatus.ACCEPTED);
    Order result = orderService.serve(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.DELIVERY);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.SERVED);
  }

  @DisplayName("주문 매장식사 서빙")
  @Test
  void orderEatInServe() {
    Order saveOrder = orderService.create(orderEatIn);

    saveOrder.setStatus(OrderStatus.ACCEPTED);
    Order result = orderService.serve(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.EAT_IN);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.SERVED);
  }

  @DisplayName("주문 포장 서빙")
  @Test
  void orderTakeOutServe() {
    Order saveOrder = orderService.create(orderTakeOut);

    saveOrder.setStatus(OrderStatus.ACCEPTED);
    Order result = orderService.serve(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.TAKEOUT);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.SERVED);
  }

  @DisplayName("주문 배달 배달 시작")
  @Test
  void orderDeliveryStart() {
    Order saveOrder = orderService.create(orderDelivery);

    saveOrder.setStatus(OrderStatus.SERVED);
    Order result = orderService.startDelivery(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.DELIVERY);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.DELIVERING);
  }

  @DisplayName("주문 배달 배달 완료")
  @Test
  void orderDeliveryComplete() {
    Order saveOrder = orderService.create(orderDelivery);

    saveOrder.setStatus(OrderStatus.DELIVERING);
    Order result = orderService.completeDelivery(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.DELIVERY);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.DELIVERED);
  }

  @DisplayName("주문 완료(배달)")
  @Test
  void orderCompleteDelivery() {
    Order saveOrder = orderService.create(orderDelivery);

    saveOrder.setStatus(OrderStatus.DELIVERED);
    Order result = orderService.complete(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.DELIVERY);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.COMPLETED);
  }

  @DisplayName("주문 완료(매장식사)")
  @Test
  void orderCompleteEatIn() {
    Order saveOrder = orderService.create(orderEatIn);

    saveOrder.setStatus(OrderStatus.SERVED);
    Order result = orderService.complete(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.EAT_IN);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.COMPLETED);
  }

  @DisplayName("주문 완료(포장)")
  @Test
  void orderCompleteTakeOut() {
    Order saveOrder = orderService.create(orderTakeOut);

    saveOrder.setStatus(OrderStatus.SERVED);
    Order result = orderService.complete(saveOrder.getId());

    assertThat(result.getType()).isEqualTo(OrderType.TAKEOUT);
    assertThat(result.getStatus()).isEqualTo(OrderStatus.COMPLETED);
  }
}