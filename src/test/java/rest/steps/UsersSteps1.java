package rest.steps;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import rest.pojos.CreateUserResponse1;
import rest.pojos.UserPojoFull1;
import rest.pojos.UserRequest1;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UsersSteps1 {
    private static final RequestSpecification REQ_SPEC =
            new RequestSpecBuilder()
                    .addFilter(new AllureRestAssured())
                    .setBaseUri("https://reqres.in/api")
                    .setBasePath("/users")
                    .setContentType(ContentType.JSON)
                    .build();

    private CreateUserResponse1 user;

    public CreateUserResponse1 createUser(UserRequest1 rq) {
        user = given().spec(REQ_SPEC).body(rq).post().as(CreateUserResponse1.class);
        return user;
    }

    public UserPojoFull1 getUser() {
        return given().spec(REQ_SPEC).get("/" + user.getId()).as(UserPojoFull1.class);
    }

    public static List<UserPojoFull1> getUsers() {
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserPojoFull1.class);
    }

    public static UserPojoFull1 getUser(int id) {
        return given().spec(REQ_SPEC).get("/" + id).as(UserPojoFull1.class);
    }
}
