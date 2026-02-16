package simple.automation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandExecutor1 {

    public ExecutionResult execute(String command) throws Exception1 {
        return execute(command, null, 30000);
    }

    public ExecutionResult execute(String command, String workingDirectory, long timeoutMillis) throws Exception1 {
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("sh", "-c", command);
        }

        if (workingDirectory != null) {
            processBuilder.directory(new java.io.File(workingDirectory));
        }

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        List<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }

        boolean finished = process.waitFor(timeoutMillis, TimeUnit.MILLISECONDS);

        if (!finished) {
            process.destroyForcibly();
            throw new Exception1("Command execution timed out after " + timeoutMillis + "ms");
        }

        int exitCode = process.exitValue();
        return new ExecutionResult(exitCode, output);
    }

    public static class ExecutionResult {
        private final int exitCode;
        private final List<String> output;

        public ExecutionResult(int exitCode, List<String> output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        public int getExitCode() {
            return exitCode;
        }

        public List<String> getOutput() {
            return output;
        }

        public boolean isSuccess() {
            return exitCode == 0;
        }

        public String getOutputAsString() {
            return String.join("\n", output);
        }

        public void newStubMethod() {
            // TODO: implement
        }
    }

    public void newStubMethod() {
        // TODO: implement
    }
}
