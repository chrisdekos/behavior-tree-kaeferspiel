package edu.kit.kastel.view.util;

import edu.kit.kastel.view.InvalidArgumentException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class for reading input files that describe boards and trees.
 * @author ujsap
 */
public final class FilesReader {

    /** Private constructor to prevent creating an instance of this object. */
    private FilesReader() {
        // Utility class
    }

    /**
     * Reads all lines from the given input file.
     * @param path the path to the input file
     * @return a list of lines from the file
     * @throws InvalidArgumentException if the file cannot be read or the path is invalid
     */
    public static List<String> readInputFile(String path) throws InvalidArgumentException {
        try {
            Path file = Paths.get(path);
            return Files.readAllLines(file);
        } catch (IOException e) {
            throw new InvalidArgumentException("file format is not correct");
        } catch (InvalidPathException e) {
            throw new InvalidArgumentException("could not find path");
        }
    }
}
