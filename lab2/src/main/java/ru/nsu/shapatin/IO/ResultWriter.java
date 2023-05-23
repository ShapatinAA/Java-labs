package ru.nsu.shapatin.IO;

import ru.nsu.shapatin.context.Context;

import java.io.*;
import java.util.logging.Logger;

public class ResultWriter {
    private static final Logger logger = Logger.getLogger(ResultWriter.class.getName());

    public void writeResult(Context context, String filename) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
            double result = context.stack.peek();  // get the top element from stack, which is the result
            writer.write("Result: " + result + "\n");
            writer.close();
            logger.info("The result has been written to " + filename);
        } catch (IOException e) {
            logger.severe("An error occurred while writing the result to file: " + filename);
            throw new IOException("An error occurred while writing the result to file: " + e.getMessage());
        }
    }
}