package steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import pojos.UserRequest;
import pojos.CreateUserResponse;
import pojos.UserPojoFull;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UsersSteps {
    private static final RequestSpecification REQ_SPEC =
            new RequestSpecBuilder()
                    .setBaseUri("https://reqres.in/api")
                    .setBasePath("/users")
                    .setContentType(ContentType.JSON)
                    .build();

    private CreateUserResponse user;
    public CreateUserResponse createUser(UserRequest rq){
        user = given().spec(REQ_SPEC).body(rq).post().as(CreateUserResponse.class);
        return user;
    }

    public UserPojoFull getUser(){
        return  given().spec(REQ_SPEC).get("/" + user.getId()).as(UserPojoFull.class);
    }

    public static List<UserPojoFull> getUsers(){
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserPojoFull.class);
    }

    public static UserPojoFull getUser(int id){
        return  given().spec(REQ_SPEC).get("/" + id).as(UserPojoFull.class);
    }
}
