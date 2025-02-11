package simple.automation;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String test = "123";
        List<String> strings = Arrays.asList("два", "один", "три");

        Map<String, Integer> digestMap = new HashMap<>();
        digestMap.put("десять", 10);
        digestMap.put("два", 2);
        digestMap.put("три", 3);

        strings.forEach(str -> System.out.println(digestMap.get(str)));

    }


    private <T> String objectToString(T object){
        return String.valueOf(object);
    }

}
