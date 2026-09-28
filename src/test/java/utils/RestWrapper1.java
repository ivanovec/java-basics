package utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import rest.pojos.UserLogin1;
import utils.services.OrderService1;
import utils.services.UserService1;

import static io.restassured.RestAssured.given;

public class RestWrapper1 {
    private static final String BASE_URL = "https://reqres.in/api";
    private Cookies cookies;

    public UserService1 user;
    public OrderService1 order;

    private RestWrapper1(Cookies cookies) {
        this.cookies = cookies;

        user = new UserService1(cookies);
        order = new OrderService1(cookies);
    }

    public static RestWrapper1 loginAs(String login, String password) {
        Cookies cookies = given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .basePath("/login")
                .body(new UserLogin1(login, password))
                .post()
                .getDetailedCookies();

        return new RestWrapper1(cookies);
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
