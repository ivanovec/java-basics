package utils.services;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;
import rest.pojos.UserRequest;
import rest.pojos.CreateUserResponse;
import rest.pojos.UserPojoFull;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserService extends RestService{

    @Override
    protected String getBasePath() {
        return "/users";
    }

    public UserService(Cookies cookies) {
        super(cookies);
    }

    @Step("Создание пользователя {rq.name}")
    public CreateUserResponse createUser(UserRequest rq){
        return given().spec(REQ_SPEC).body(rq).post().as(CreateUserResponse.class);
    }

    @Step("Получение списка пользователей")
    @Attachment
    public List<UserPojoFull> getUsers(){
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserPojoFull.class);
    }
}
