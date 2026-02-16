package utils.services;

import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import rest.pojos.CreateUserResponse1;
import rest.pojos.UserPojoFull1;
import rest.pojos.UserRequest1;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderService1 extends RestService1 {

    @Override
    protected String getBasePath() {
        return "/orders";
    }

    public OrderService1(Cookies cookies) {
        super(cookies);
    }

    public List<UserPojoFull1> getOrders() {
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserPojoFull1.class);
    }

    @Step("Создание пользователя {rq.name}")
    public CreateUserResponse1 createUser(UserRequest1 rq) {
        return given().spec(REQ_SPEC).body(rq).post().as(CreateUserResponse1.class);
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
