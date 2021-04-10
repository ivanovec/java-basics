package simple.automation;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("pseudo")
public class AuthorizationTests extends BaseTestWithAuthorization{

    @Test
    public void testLogin(){
        System.out.println("check success login");
    }
}
