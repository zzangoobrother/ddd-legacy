package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.MediaType;

public class OrderTableSteps {

  public static ExtractableResponse<Response> createOrderTable(String name) {
    Map<String, String> params = new HashMap<>();
    params.put("name", name);

    return RestAssured
        .given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/api/order-tables")
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> chageOrderTableSit(UUID orderTableId) {
    return RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().put("/api/order-tables/{orderTableId}/sit", orderTableId)
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> chageOrderTableNumberOfGuests(UUID orderTableId, int guestNum) {
    Map<String, Integer> params = new HashMap<>();
    params.put("numberOfGuests", guestNum);

    return RestAssured
        .given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().put("/api/order-tables/{orderTableId}/number-of-guests", orderTableId)
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> getOrderTables() {
    return RestAssured
        .given().log().all()
        .when().get("/api/order-tables")
        .then().log().all().extract();
  }
}
