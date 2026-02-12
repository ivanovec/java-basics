package simple.automation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor1 {

    public List<String> readLines(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
    }

    public void writeLines(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, lines);
    }

    public List<Path> findFiles(String directory, String extension) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(extension))
                    .collect(Collectors.toList());
        }
    }

    public long countLines(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.count();
        }
    }

    public boolean deleteFile(String filePath) throws IOException {
        return Files.deleteIfExists(Paths.get(filePath));
    }
}
