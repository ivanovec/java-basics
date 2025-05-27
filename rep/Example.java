package simple.automation;

import java.util.Random;

public class Example {
    public void doSomething() {
        String cmd = getData();
        execute(cmd);
    }

    String getData() {
        return System.console().readLine();
    }

    void execute(String cmd) throws Exception {
        Runtime.getRuntime().exec(cmd);
    }
}

