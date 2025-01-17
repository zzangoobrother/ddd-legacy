package kitchenpos.acceptance;

import static kitchenpos.acceptance.BaseRestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class MenuGroupSteps {

  public static ExtractableResponse<Response> createMenuGroup(String name) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name);

    return given()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/api/menu-groups")
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> getMenuGroups() {
    return given()
        .when().get("/api/menu-groups")
        .then().log().all().extract();
  }
}
