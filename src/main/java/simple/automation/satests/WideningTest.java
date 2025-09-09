package simple.automation.satests;

public class WideningTest {
    public static void wideningTest(){
        int[] arr = new int[8];

        for (int i = 0; i < 10; i++) {
            arr[i] = i;
        }
    }

    public static void wideningTest2(String[] args){
        int x = 0;

        if(args.length == 1) x = 60;
        System.out.println(10/x);

        if(args.length == 2) x = 6;
        System.out.println(10/x);
    }

    public static void testWhile(){
        int x = 0;
        while (x < 10){
            x++;
        }
        System.out.println(10/x);

        while (x < 100){
            x++;
        }
        System.out.println(10/x);
    }
}
