package simple.automation;

import java.util.*;

public class Main1 {

    public static void main(String[] args) {
        String x = "123";
        String y = "124";
        String test = "123";
        test = "123";
        test = y + x;
        List<String> strings = Arrays.asList("два", "один", "три");
        String ddffd = "34";
        Map<String, Integer> digestMap = new HashMap<>();
        digestMap.put("десять", 10);
        digestMap.put("два", 2);
        digestMap.put("три", 3);

        strings.forEach(str -> System.out.println(digestMap.get(str)));
        while (true){}

    }


    private <T> String objectToString(T object) {
        return String.valueOf(object);
    }

    public void newStubMethod() {
        // TODO: implement
    }

}
