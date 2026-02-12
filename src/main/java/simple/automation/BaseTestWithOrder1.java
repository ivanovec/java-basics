package simple.automation;

public class BaseTestWithOrder1 extends BaseTestWithAuthorization1 {
    public BaseTestWithOrder1() {
        System.out.println("create order");
        String order = "12345";
    }
}
