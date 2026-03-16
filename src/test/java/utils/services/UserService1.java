package utils.services;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import rest.RestResponse1;
import rest.pojos.UserRequest1;
import rest.pojos.CreateUserResponse1;
import rest.pojos.UserPojoFull1;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserService1 extends RestService1 {

    @Override
    protected String getBasePath() {
        return "/users";
    }

    public UserService1(Cookies cookies) {
        super(cookies);
    }

    @Step("Создание пользователя {rq.name}")
    public CreateUserResponse1 createUser(UserRequest1 rq) {
        return given().spec(REQ_SPEC).body(rq).post().as(CreateUserResponse1.class);
    }

    @Step("Получение списка пользователей")
    @Attachment
    public RestResponse1<List<UserPojoFull1>> getUsers() {
        return new RestResponse1<>(
                given().spec(REQ_SPEC).get(),
                resp -> resp.body().jsonPath().getList("data", UserPojoFull1.class));
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
