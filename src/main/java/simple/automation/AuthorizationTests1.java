package simple.automation;

import org.junit.jupiter.api.*;

@Tag("pseudo")
public class AuthorizationTests1 extends BaseTestWithAuthorization1 {

    @Test1
    public void testLogin() {
        System.out.println("check success login");
    }
}
