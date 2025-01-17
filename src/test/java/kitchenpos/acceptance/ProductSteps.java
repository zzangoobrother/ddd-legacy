package kitchenpos.acceptance;

import static kitchenpos.acceptance.BaseRestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.MediaType;

public class ProductSteps {

  public static ExtractableResponse<Response> createProduct(String name, int price) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name);
    params.put("price", price);

    return given()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/api/products")
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> updateProductPrice(UUID productId, int price) {
    Map<String, Object> params = new HashMap<>();
    params.put("price", price);

    return given()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().put("/api/products/{productId}/price", productId)
        .then().log().all().extract();
  }

  public static ExtractableResponse<Response> getProducts() {
    return given()
        .when().get("/api/products")
        .then().log().all().extract();
  }
}
