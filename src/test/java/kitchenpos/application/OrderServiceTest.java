package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.domain.InMemoryMenuRepository;
import kitchenpos.domain.InMemoryOrderRepository;
import kitchenpos.domain.InMemoryOrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderType;
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

  private Menu menu;

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

    menu = MenuFixture.createMenu();
    Menu saveMenu = menuRepository.save(menu);

    OrderTable saveOrderTable = orderTableRepository.save(OrderTableFixture.createOrderTable());

    orderEatIn = OrderFixture.createOrderEatIn();
    orderEatIn.getOrderLineItems().get(0).setMenuId(saveMenu.getId());
    orderEatIn.setOrderTableId(saveOrderTable.getId());

    orderTakeOut = OrderFixture.createOrderTakeOut();
    orderTakeOut.getOrderLineItems().get(0).setMenuId(saveMenu.getId());

    orderDelivery = OrderFixture.createOrderDelivery();
    orderDelivery.getOrderLineItems().get(0).setMenuId(saveMenu.getId());
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