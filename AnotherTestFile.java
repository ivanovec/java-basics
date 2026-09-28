public class AnotherTestFile {
    private String message;

    public AnotherTestFile(String message) {
        this.message = message;
    }

    public void printMessage() {
        System.out.println(message);
    }

    public static void main(String[] args) {
        AnotherTestFile test = new AnotherTestFile("test");
        test.printMessage();
    }
}
