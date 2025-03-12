package simple.automation;

import java.util.*;

public class Main {

    public static void main(String[] args) {




        class StringWrapper {
            protected String str;

            private StringWrapper() {
            }

            public StringWrapper(String str) {
                this.str = str;
            }

            public String toString() {
                return str;
            }
        }

        class StringWrapperWithStringHashCode extends StringWrapper {
            StringWrapperWithStringHashCode(String str) {
                super(str);
            }
            public int hashCode() {
                return str.hashCode();
            }
        }

        class StringWrapperWithStringEquals extends StringWrapper {
            StringWrapperWithStringEquals(String str) {
                super(str);
            }
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (obj instanceof String) return str.equals(obj);
                return str.equals(obj.toString());
            }
        }

        class StringWrapperWithStringHashCodeAndEquals extends StringWrapper {
            StringWrapperWithStringHashCodeAndEquals(String str) {
                super(str);
            }
            public int hashCode() {
                return str.hashCode();
            }

            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (obj instanceof String) return str.equals(obj);
                return str.equals(obj.toString());
            }
        }

        class StringWrapperWithEqualsByEqualsSymbol extends StringWrapper {
            StringWrapperWithEqualsByEqualsSymbol(String str) {
                super(str);
            }

            public int hashCode() {
                return str.hashCode();
            }

            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (obj instanceof String) return str == obj;
                return str == obj.toString();
            }
        }

        String s1 = "FB";
        String s2 = "Ea";
        String s3 = "1";
        String s4 = new String("1");
        StringWrapper swrap1 = new StringWrapper("FB");
        StringWrapper swrap2 = new StringWrapper("Ea");
        StringWrapper swrap3 = new StringWrapper("1");
        StringWrapper swrap4 = new StringWrapper(new String("1"));
        StringWrapperWithStringHashCode shash1 = new StringWrapperWithStringHashCode("FB");
        StringWrapperWithStringHashCode shash2 = new StringWrapperWithStringHashCode("Ea");
        StringWrapperWithStringHashCode shash3 = new StringWrapperWithStringHashCode("1");
        StringWrapperWithStringHashCode shash4 = new StringWrapperWithStringHashCode(new String("1"));
        StringWrapperWithStringEquals seq1 = new StringWrapperWithStringEquals("FB");
        StringWrapperWithStringEquals seq2 = new StringWrapperWithStringEquals("Ea");
        StringWrapperWithStringEquals seq3 = new StringWrapperWithStringEquals("1");
        StringWrapperWithStringEquals seq4 = new StringWrapperWithStringEquals(new String("1"));
        StringWrapperWithStringHashCodeAndEquals shasheq1 = new StringWrapperWithStringHashCodeAndEquals("FB");
        StringWrapperWithStringHashCodeAndEquals shasheq2 = new StringWrapperWithStringHashCodeAndEquals("Ea");
        StringWrapperWithStringHashCodeAndEquals shasheq3 = new StringWrapperWithStringHashCodeAndEquals("1");
        StringWrapperWithStringHashCodeAndEquals shasheq4 = new StringWrapperWithStringHashCodeAndEquals(new String("1"));

        StringWrapperWithEqualsByEqualsSymbol seqsymb1 = new StringWrapperWithEqualsByEqualsSymbol("FB");
        StringWrapperWithEqualsByEqualsSymbol seqsymb2 = new StringWrapperWithEqualsByEqualsSymbol("Ea");
        StringWrapperWithEqualsByEqualsSymbol seqsymb3 = new StringWrapperWithEqualsByEqualsSymbol("1");
        StringWrapperWithEqualsByEqualsSymbol seqsymb4 = new StringWrapperWithEqualsByEqualsSymbol(new String("1"));

        HashMap<Object, String> map = new HashMap<>();

        map.put(s1, "1");
        map.put(s2, "2");
        map.put(s3, "3");
        map.put(s4, "4");
        map.put(swrap1, "1");
        map.put(swrap2, "2");
        map.put(swrap3, "3");
        map.put(swrap4, "4");
        map.put(seq1, "1");
        map.put(seq2, "2");
        map.put(seq3, "3");
        map.put(seq4, "4");
        map.put(shash1, "1");
        map.put(shash2, "2");
        map.put(shash3, "3");
        map.put(shash4, "4");
        map.put(shasheq1, "1");
        map.put(shasheq2, "2");
        map.put(shasheq3, "3");
        map.put(shasheq4, "4");
        map.put(seqsymb1, "1");
        map.put(seqsymb2, "2");
        map.put(seqsymb3, "3");
        map.put(seqsymb4, "4");



        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));

        map.put(s2, "3");
        map.get(s2);
        int a = 1;
        int b = 5;

        a = a^b;
        b = b^a;
        a = a^b;

        int c = 999999999 & 64;
        char x = 'a';
        String test = "123";
        String test2 = "123";
        System.out.println(test == test2);
        System.out.println(test.equals(test2));
        System.out.println(test == "123");
        System.out.println(test == new String("123"));

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
