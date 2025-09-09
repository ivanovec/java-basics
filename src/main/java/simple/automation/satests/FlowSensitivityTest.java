package simple.automation.satests;

public class FlowSensitivityTest {
    public void flowSensitivityTest(){
        int x = 42;
        if(x > 10) x = 0;
        if(x != 0) System.out.println(10/x);
        int y = 10/x;
    }
}
