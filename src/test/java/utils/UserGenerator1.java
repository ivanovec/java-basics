package utils;

import rest.pojos.UserRequest1;

public class UserGenerator1 {

    public static UserRequest1 getSimpleUser() {
        return UserRequest1.builder()
                .name("simple")
                .job("automation")
                .build();
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
