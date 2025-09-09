package simple.automation.satests;

public class FinallyBlockTest {
    public static void finallyBlockTest() {
        String value = null;
        try {
            System.out.println("try");
        } finally {
            System.out.println(value.toString());
        }
    }
}
