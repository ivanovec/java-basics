package simple.automation.satests;

public class PathSensitivityReduntTest {
    public static void pathSensitivityReduntTest() {
        int x = 5;
        if( x >0 || x < 10) {
            System.out.println("Always true");
        }
        x=x;
    }
}
