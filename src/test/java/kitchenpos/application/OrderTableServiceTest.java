package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.InMemoryOrderRepository;
import kitchenpos.domain.InMemoryOrderTableRepository;
import java.util.Optional;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블")
class OrderTableServiceTest {

  private OrderTableRepository orderTableRepository;
  private OrderRepository orderRepository;

  private OrderTableService orderTableService;

  private OrderTable orderTable;

  @BeforeEach
  void setUp() {
    orderTableRepository = new InMemoryOrderTableRepository();
    orderRepository = new InMemoryOrderRepository();

    orderTableService = new OrderTableService(orderTableRepository, orderRepository);

    orderTable = OrderTableFixture.createOrderTable();
  }

  @DisplayName("주문 테이블 등록")
  @Test
  void createOrderTable() {
    OrderTable result = orderTableService.create(orderTable);

    assertThat(result.getName()).isEqualTo("1번");
  }

  @DisplayName("주문 테이블 이름 null 등록 에러")
  @Test
  void createOrderTableNameNull() {
    orderTable.setName(null);

    assertThatThrownBy(() -> orderTableService.create(orderTable)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 테이블 이름 빈값 등록 에러")
  @Test
  void createOrderTableNameEmpty() {
    orderTable.setName("");

    assertThatThrownBy(() -> orderTableService.create(orderTable)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 테이블 착석으로 변경")
  @Test
  void chageOrderTableSit() {
    OrderTable saveOrderTable = orderTableService.create(orderTable);

    orderTable.setOccupied(false);

    OrderTable result = orderTableService.sit(saveOrderTable.getId());

    assertThat(result.isOccupied()).isEqualTo(true);
  }

  @DisplayName("주문 테이블 손님수 변경")
  @Test
  void chageOrderTableNumberOfGusts() {
    OrderTable saveOrderTable = orderTableService.create(orderTable);

    saveOrderTable.setOccupied(true);

    OrderTable chageOrderTable = new OrderTable();
    chageOrderTable.setNumberOfGuests(2);

    OrderTable result = orderTableService.changeNumberOfGuests(saveOrderTable.getId(), chageOrderTable);

    assertThat(result.getNumberOfGuests()).isEqualTo(2);
  }

  @DisplayName("주문 테이블 손님수가 음수이면 에러")
  @Test
  void chageOrderTableNumberOfGuestsNegative() {
    OrderTable saveOrderTable = orderTableService.create(orderTable);

    saveOrderTable.setOccupied(true);

    OrderTable chageOrderTable = new OrderTable();
    chageOrderTable.setNumberOfGuests(-1);

    assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(saveOrderTable.getId(), chageOrderTable)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 테이블 빈 테이블에서 손님수 변경시 에러")
  @Test
  void chageOrderTableNumberOfGuestsNotSit() {
    OrderTable saveOrderTable = orderTableService.create(orderTable);

    OrderTable chageOrderTable = new OrderTable();
    chageOrderTable.setNumberOfGuests(2);

    assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(saveOrderTable.getId(), chageOrderTable)).isInstanceOf(IllegalStateException.class);
  }
}