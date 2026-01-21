package simple.automation;

public class Exception {

    String message;

    public void print() {
        System.out.println(message);
    }

    public void print(String message) {
        this.message = message;
        print();
    }

    static class InternalException extends RuntimeException{
        public InternalException(String message) {
            super(message);
        }
    }
}
