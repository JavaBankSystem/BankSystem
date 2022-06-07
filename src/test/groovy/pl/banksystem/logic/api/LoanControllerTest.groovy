package pl.banksystem.logic.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.http.Header
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.banksystem.BankSystemApplication
import pl.banksystem.logic.account.loans.Loan
import pl.banksystem.logic.account.loans.NewLoanForm
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType
import pl.banksystem.logic.api.providers.TransactionProvider
import spock.lang.Shared
import spock.lang.Specification

import static io.restassured.RestAssured.given

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = BankSystemApplication.class
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LoanControllerTest extends Specification {

    @Shared
    def requestSpec =
            new RequestSpecBuilder()
                    .setBaseUri("http://localhost:8080/api/v1")
                    .build()

    @Shared
    def ow = new ObjectMapper().writer().withDefaultPrettyPrinter()

    def "just test request"() {
        given:
        def request = given(requestSpec)

        expect:
        def response = request.log().all().when().get("/")
                .then().log().all();
    }

    def "add Loan test"() {
        given:
        def loan = new NewLoanForm(0, 0.08, 0.05, 12, 120000)
        def request = given(requestSpec)
        when:
        def response = request.log().all()
                .given().body(ow.writeValueAsString(loan)).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/loans")
        then:
        response.then().statusCode(200)
    }


    def "add bad Transaction"() {

        given:
        def request = given(requestSpec)
        def transaction = "{asdad}"

        when:
        def response = request.log().all()
                .given().body(transaction).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/transaction/save")

        then:
        response.then().statusCode(400)
    }
    def "get transaction by id"(){

        given:
        def request = given(requestSpec)
        addSomeTransaction()

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("id","2")
                .when().get("/api/v1/transaction/{id}")

        then:
        response.then().statusCode(200)
    }
    def "get transaction by accountId"(){

        given:
        def request = given(requestSpec)
        addSomeTransaction()

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("accountID","1")
                .when().get("/api/v1/transaction/account/{accountID}")

        then:
        response.then().statusCode(200)
    }

    def "get status history"(){

        given:
        def request = given(requestSpec)
        addSomeTransaction()

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("id","1")
                .when().get("/api/v1/transaction/{id}/status")

        then:
        response.then().statusCode(200)
    }


    def "update status correctly"(){
        given:
        def request = given(requestSpec)
        def newStatus = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(TransactionStatusType.DONE)
        addSomeTransaction()

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("id","2")
                .body(newStatus).contentType(ContentType.JSON)
                .when().put("/api/v1/transaction/{id}/updateStatus")

        then:
        response.then().statusCode(200)
        response.getBody().asString() == "Transaction 2 status updated to: DONE"
    }
    def "update status - transactionID not exist"(){
        given:
        def request = given(requestSpec)
        def newStatus = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(TransactionStatusType.DONE)
        addSomeTransaction()

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("id","100000")
                .body(newStatus).contentType(ContentType.JSON)
                .when().put("/api/v1/transaction/{id}/updateStatus")

        then:
        response.then().statusCode(404)
        response.getBody().asString() == "Given Transaction ID: 100000 not exist in DataBase"
    }

    def "change status problem"(){
        given:
        def request = given(requestSpec)
        def transaction1 = TransactionProvider.transaction3;
        addTransaction()
        def newStatus = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(TransactionStatusType.DONE)

        when:
        def response = request.log().all()
                .given().header(getTokenHeader())
                .pathParam("id","1")
                .body(newStatus).contentType(ContentType.JSON)
                .when().put("/api/v1/transaction/{id}/updateStatus")

        then:
        response.then().statusCode(409)
        response.getBody().asString() == "Change status problem!! Last status: CANCELED, new TransactionStatusType: DONE"
    }


    private void addSomeTransaction(){
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
    private void addTransaction(){
        def request = given(requestSpec)
        def transaction1 = TransactionProvider.transaction3;
        request.log().all()
                .given().body(transaction1).contentType(ContentType.JSON).header(getTokenHeader())
                .when().post("/api/v1/transaction/save").then().statusCode(200)

    }
    private Header getTokenHeader(){
        def token = given(requestSpec)
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "root")
                .formParam("password", "root")
                .when()
                .post("/login").getBody().as(Map.class).get("access_token").toString()
        return new Header("Authorization", token)
    }
}