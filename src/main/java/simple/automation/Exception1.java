package simple.automation;

public class Exception1 extends Throwable {

    String message;

    public void print() {
        System.out.println(message);
    }

    public void print(String message) {
        this.message = message;
        print();
    }

    static class InternalException extends RuntimeException {
        public InternalException(String message) {
            super(message);
        }

        public void newStubMethod() {
            // TODO: implement
        }
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
