package eu.simplecompliance.ublvalidator.schematron;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    private final String filePath;

    public FileHelper(String filePath) {
        this.filePath = filePath;
    }

    public void write(byte[] bytes) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, bytes);
    }

    public byte[] read() throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}
