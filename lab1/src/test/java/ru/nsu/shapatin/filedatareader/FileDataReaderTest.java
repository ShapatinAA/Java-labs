package ru.nsu.shapatin.filedatareader;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class FileDataReaderTest {
    private final FileDataReader fileDataReader = new FileDataReader();

    private final String projectPath = new File("").getAbsolutePath();
    private final Path resourcesPath = Paths.get(projectPath, "src", "test", "resources");

    @Test
    void readWords_ValidFile_ReturnsWordsList() throws IOException {
        // Arrange
        String content = "This is a sample input file.";

        Path testInputPath = Files.createTempFile(resourcesPath, "input", ".txt");
        Files.writeString(testInputPath, content);

        // Act
        List<String> actualWords = fileDataReader.readWords(testInputPath.toString());

        // Assert
        List<String> expectedWords = List.of("This", "is", "a", "sample", "input", "file");
        assertIterableEquals(expectedWords, actualWords);

        // Clean up
        Files.deleteIfExists(testInputPath);
    }

    @Test
    void readWords_FileNotFound_ThrowsIOException() {
        // Arrange
        String nonExistentFilename = Paths.get(resourcesPath.toString(), "nonexistent.txt").toString();

        // Act and Assert
        assertThrows(IOException.class, () -> fileDataReader.readWords(nonExistentFilename));
    }
}
