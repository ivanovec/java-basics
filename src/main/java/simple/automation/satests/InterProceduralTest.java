package simple.automation.satests;

public class InterProceduralTest {
    public void testInterProcedural(){
        String x = "test";
        System.out.println(x.toLowerCase());

        x = getValue3();
        System.out.println(x.toLowerCase());

        x = getValue2();
        System.out.println(x.toLowerCase());

        x = getValue();
        System.out.println(x.toLowerCase());
    }

    public static String getValue() {
        if(Math.random() > 0.5) return "hello";
        else return null;
    }

    public String getValue2() {
        if(Math.random() > 0.5) return "hello";
        else return null;
    }

    public String getValue3() {
        return getValue();
    }
}
