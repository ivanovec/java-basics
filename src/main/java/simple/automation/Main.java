package simple.automation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        String test = "124";
        List<String> strings = Arrays.asList("два", "один", "три");
        String ddffd = "34";
        Map<String, Integer> digestMap = new HashMap<>();
        digestMap.put("десять", 10);
        digestMap.put("два", 2);
        digestMap.put("три", 3);

        strings.forEach(str -> System.out.println(digestMap.get(str)));
        while (true){}

    }


    private <T> String objectToString(T object){
        return String.valueOf(object);
    }

}
