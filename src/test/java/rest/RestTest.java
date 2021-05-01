package rest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.pojos.UserRequest;
import rest.pojos.CreateUserResponse;
import rest.pojos.UserPojo;
import utils.RestWrapper;
import utils.UserGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class RestTest {

    private static RestWrapper api;

    @BeforeAll
    public static void prepareClient(){
        api = RestWrapper.loginAs("eve.holt@reqres.in", "cityslicka");
    }

    @Test
    public void getUsers(){
        assertThat(api.user.getUsers()).extracting(UserPojo::getEmail).contains("george.bluth@reqres.in");
    }

    @Test public void createUser(){
        UserRequest rq = UserGenerator.getSimpleUser();
        CreateUserResponse rs = api.user.createUser(rq);

        assertThat(rs)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(rq.getName());

    }
}
