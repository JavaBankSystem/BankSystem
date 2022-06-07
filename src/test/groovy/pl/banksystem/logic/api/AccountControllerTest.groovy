package pl.banksystem.logic.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.http.Header
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.banksystem.BankSystemApplication
import pl.banksystem.logic.account.Account
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType
import pl.banksystem.logic.api.providers.AccountProvider
import pl.banksystem.logic.api.providers.TransactionProvider
import pl.banksystem.logic.domain.AppUser
import spock.lang.Shared
import spock.lang.Specification

import static io.restassured.RestAssured.given

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = BankSystemApplication.class
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AccountControllerTest extends Specification {
    @Shared
    def requestSpec =
            new RequestSpecBuilder()
                    .setBaseUri("http://localhost:8080")
                    .build()

    def "just test request"() {
        given:
        def request = given(requestSpec)

        expect:
        def response = request.log().all()
                .when().get("/")
                .then().log().all();
    }

    def "test if endpoint /api/v1/account/all works"() {

        given:
        def request = given(requestSpec)
        when:
        def response = request.log().all().when().get("/api/v1/account/all")
        then: "status should be 204 because there is no content yet"
        response.then().statusCode(204)
    }

    def "add Account test"() {

        given:
        def request = given(requestSpec)
        def account = AccountProvider.account1()
        when:
        def response = request.log().all()
                .given().body(account).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/account/save")
        then:
        response.then().statusCode(200)
    }

    def "add bad Account test"() {

        given:
        def request = given(requestSpec)
        def account = "{asdasd}"
        when:
        def response = request.log().all()
                .given().body(account).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/account/save")
        then:
        response.then().statusCode(400)
    }

    def "get account by accountId"() {

        given:
        def request = given(requestSpec)
        def addedAccounts = addSomeAccounts()

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("id", addedAccounts.get(0).getAccountID())
                .when().get("/api/v1/account/{id}")

        then:
        response.then().statusCode(200)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    def "get account by clientId"() {

        given:
        def request = given(requestSpec)
        addSomeAccounts()

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("id", "1")
                .when().get("/api/v1/account/client/{id}")

        then:
        response.then().statusCode(200)
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    def "make transaction"() {

        given:
        def request = given(requestSpec)
        def addedAccounts = addSomeAccounts()
        addSomeTransaction()
        def token = getTokenHeader()
        def newStatus = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(TransactionStatusType.DONE)
        request.log().all()
                .given().header(token)
                .pathParam("id", "2")
                .body(newStatus).contentType(ContentType.JSON)
                .when().put("/api/v1/transaction/{id}/updateStatus")
        request = given(requestSpec)
        when:
        def response = request.log().all()
                .given().header(token)
                .pathParams(Map.of("accountID", "2", "transactionID", "2"))
                .when().put("/api/v1/account/{accountID}/{transactionID}")

        then:
        response.then().statusCode(200)
        response.getBody().asString() == "Current Operation: Deposit , Balance after transaction: 2000.0"
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    def "make transaction - accountID not exist in DataBase"() {

        given:
        def request = given(requestSpec)
        addSomeAccounts()
        addSomeTransaction()
        def token = getTokenHeader()
        def newStatus = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(TransactionStatusType.DONE)
        request.log().all()
                .given().header(token)
                .pathParam("id", "2")
                .body(newStatus).contentType(ContentType.JSON)
                .when().put("/api/v1/transaction/{id}/updateStatus")
        request = given(requestSpec)
        when:
        def response = request.log().all()
                .given().header(token)
                .pathParams(Map.of("accountID", "2222", "transactionID", "2"))
                .when().put("/api/v1/account/{accountID}/{transactionID}")

        then:
        response.then().statusCode(404)
        response.getBody().asString() == "Given AccountID: 2222 not exist in DataBase"
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    def "make transaction - transactionID not exist in DataBase"() {

        given:
        def request = given(requestSpec)
        addSomeAccounts()
        addSomeTransaction()
        def token = getTokenHeader()
        def newStatus = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(TransactionStatusType.DONE)
        request.log().all()
                .given().header(token)
                .pathParam("id", "2")
                .body(newStatus).contentType(ContentType.JSON)
                .when().put("/api/v1/transaction/{id}/updateStatus")
        request = given(requestSpec)
        when:
        def response = request.log().all()
                .given().header(token)
                .pathParams(Map.of("accountID", "2", "transactionID", "2222"))
                .when().put("/api/v1/account/{accountID}/{transactionID}")

        then:
        response.then().statusCode(404)
        response.getBody().asString() == "Given Transaction ID: 2222 not exist in DataBase"
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    def "make transaction - The transaction does not apply to the account number"() {

        given:
        def request = given(requestSpec)
        addSomeAccounts()
        addSomeTransaction()
        def token = getTokenHeader()
        def newStatus = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(TransactionStatusType.DONE)
        request.log().all()
                .given().header(token)
                .pathParam("id", "2")
                .body(newStatus).contentType(ContentType.JSON)
                .when().put("/api/v1/transaction/{id}/updateStatus")
        request = given(requestSpec)
        when:
        def response = request.log().all()
                .given().header(token)
                .pathParams(Map.of("accountID", "1", "transactionID", "2"))
                .when().put("/api/v1/account/{accountID}/{transactionID}")

        then:
        response.then().statusCode(404)
        response.getBody().asString() == "The transaction 2 does not apply to the account number: 1"
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    def "make transaction - The transaction cannot be recorded because of status"() {

        given:
        def request = given(requestSpec)
        addSomeAccounts()
        addSomeTransaction()
        def token = getTokenHeader()
        when:
        def response = request.log().all()
                .given().header(token)
                .pathParams(Map.of("accountID", "2", "transactionID", "2"))
                .when().put("/api/v1/account/{accountID}/{transactionID}")

        then:
        response.then().statusCode(406)
        response.getBody().asString() == "The transaction cannot be recorded. Current status: PENDING"
    }

    private void addSomeTransaction() {
        def request = given(requestSpec)
        def transaction1 = TransactionProvider.transaction1;
        request.log().all()
                .given().body(transaction1).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/transaction/save").then().statusCode(200)
        def transaction2 = TransactionProvider.transaction2
        request.log().all()
                .given().body(transaction2).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/transaction/save").then().statusCode(200)

    }

    private Header getTokenHeader() {
        def token = given(requestSpec)
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "root")
                .formParam("password", "root")
                .when()
                .post("/api/v1/login").getBody().as(Map.class).get("access_token").toString()
        return new Header("Authorization", token)
    }
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    def "test"() {
        def request = given(requestSpec)
        given:
        addUsers()
        def user1 = request.when().get("/api/v1/users").getBody().as(AppUser[].class)[1]
        def user2 = request.when().get("/api/v1/users").getBody().as(AppUser[].class)[2]
        def accounts = addSomeAccounts(user1, user2)
        accounts.get(0)
    }

    List<Account> addSomeAccounts(AppUser user1, AppUser user2) {
        def request = given(requestSpec)
        def account1 = new Account(new AppUser("chuj","chuj"), 2000)
        request.log().all()
                .given().body(account1).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/account/save").then().statusCode(200)
        def account2 = new Account(user2, 3000)
        request.log().all()
                .given().body(account2).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/account/save").then().statusCode(200)
        return request.log().all().when().get("/api/v1/account/all").getBody().as(Account[].class);
    }

    private void addUsers() {
        def request = given(requestSpec)
        request.given()
                .body(new AppUser("user1", "password")).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/users").then().statusCode(201)
        request.given()
                .body(new AppUser("user2", "password")).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/users").getBody().as(AppUser.class)
    }
}
