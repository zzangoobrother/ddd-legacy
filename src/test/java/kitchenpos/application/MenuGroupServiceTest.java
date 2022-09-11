package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.InMemoryMenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹")
class MenuGroupServiceTest {

  private MenuGroupRepository menuGroupRepository;

  private MenuGroupService menuGroupService;

  private MenuGroup menuGroup;

  @BeforeEach
  void setUp() {
    menuGroupRepository = new InMemoryMenuGroupRepository();
    menuGroupService = new MenuGroupService(menuGroupRepository);

    menuGroup = new MenuGroup();
    menuGroup.setName("추천메뉴");
  }

  @DisplayName("메뉴 그룹 등록")
  @Test
  void createMenuGroup() {
    MenuGroup result = menuGroupService.create(menuGroup);

    assertThat(result.getName()).isEqualTo("추천메뉴");
  }

  @DisplayName("메뉴 그룹 이름 null 등록 에러")
  @Test
  void createMenuGroupNameNull() {
    menuGroup.setName(null);

    assertThatThrownBy(() -> menuGroupService.create(menuGroup)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 그룹 이름 빈값 등록 에러")
  @Test
  void createMenuGroupNameEnpty() {
    menuGroup.setName("");

    assertThatThrownBy(() -> menuGroupService.create(menuGroup)).isInstanceOf(IllegalArgumentException.class);
  }
}