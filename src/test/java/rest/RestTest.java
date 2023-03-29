package rest;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import rest.pojos.UserRequest;
import rest.pojos.CreateUserResponse;
import rest.pojos.UserPojoFull;
import utils.RestWrapper;
import utils.UserGenerator;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.isA;

@DisplayName("Тесты работы с API клиента")
@Feature("Api for users")
@Tag("rest")
public class RestTest {

    private static RestWrapper api;

    @BeforeAll
    public static void prepareClient(){
        api = RestWrapper.loginAs("eve.holt@reqres.in", "cityslicka");
    }

    @Test
    @DisplayName("Получение пользователей")
    @Story("List all users")
    public void getUsers(){
        RestResponse<List<UserPojoFull>> usersResponse = api.user.getUsers();
        usersResponse.validate("UsersTemplate.json");
        assertThat(usersResponse.extract()).extracting(UserPojoFull::getEmail).contains("george.bluth@reqres.in");
    }

    @DisplayName("Создание пользователя")
    @Story("Create user")
    @Description("Проверяет отправку запроса на создлание пользователя и что в системе создался юзер с указанным именем")
    @Test public void createUser(){
        UserRequest rq = UserGenerator.getSimpleUser();
        CreateUserResponse rs = api.user.createUser(rq);

        assertThat(rs)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(rq.getName());
    }
}
