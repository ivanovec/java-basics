package utils.services;

import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import rest.pojos.CreateUserResponse;
import rest.pojos.UserPojoFull;
import rest.pojos.UserRequest;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderService extends RestService{

    @Override
    protected String getBasePath() {
        return "/orders";
    }

    public OrderService(Cookies cookies) {
        super(cookies);
    }

    public List<UserPojoFull> getOrders(){
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserPojoFull.class);
    }

    @Step("Создание пользователя {rq.name}")
    public CreateUserResponse createUser(UserRequest rq){
        return given().spec(REQ_SPEC).body(rq).post().as(CreateUserResponse.class);
    }
}
