package utils;

import pojos.UserRequest;

public class UserGenerator {

    public static UserRequest getSimpleUser(){
        return UserRequest.builder()
                .name("simple")
                .job("automation")
                .build();
    }
}
