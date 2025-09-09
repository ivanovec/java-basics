package simple.automation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ClassGenerator {

    public static void main(String[] args) {
        String filename = "HugeSwitchExplosion.java";
        int methodCount = 10_000;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("public class HugeSwitchExplosion {\n\n");

            // main method
            writer.write("    public static void main(String[] args) {\n");
            writer.write("        long sum = 0;\n");
            writer.write("        for (int i = 0; i < 1_000_000; i++) {\n");
            writer.write("            sum += dispatch(i % " + methodCount + ");\n");
            writer.write("        }\n");
            writer.write("        System.out.println(sum);\n");
            writer.write("    }\n\n");

            // dispatch method
            writer.write("    public static int dispatch(int x) {\n");
            writer.write("        switch (x) {\n");
            for (int i = 0; i < methodCount; i++) {
                writer.write("            case " + i + ": return f" + i + "();\n");
            }
            writer.write("            default: return -1;\n");
            writer.write("        }\n");
            writer.write("    }\n\n");

            // f0 .. f9999
            for (int i = 0; i < methodCount; i++) {
                writer.write("    public static int f" + i + "() { return " + i + "; }\n");
            }

            writer.write("}\n");

            System.out.println("Generated: " + filename);
        } catch (IOException e) {
            System.err.println("Failed to write file: " + e.getMessage());
        }
    }
}
