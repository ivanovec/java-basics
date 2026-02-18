package simple.automation;

public class Person {
    private String name;
    private int age;
    private String job;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(  String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void doSomething() {
        // TODO: implement this method
        throw new UnsupportedOperationException("Not implemented yet2");
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
