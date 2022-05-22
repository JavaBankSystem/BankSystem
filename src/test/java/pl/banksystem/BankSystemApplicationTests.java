package pl.banksystem;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

class BankSystemApplicationTests extends RestAssured {

    @Test
    void contextLoads() {
    }
    @Test
    @DisplayName("Just the request... :P")
    void entryPointTest() {
        RestAssured
                .given().baseUri("http://localhost:8080").log().all()
                .when().get("/")
                .then().log().all();
    }

}
