package ru.nsu.shapatin.IO;

import ru.nsu.shapatin.commands.Command;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.factory.CommandFactory;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class FileCommandReader {
    private static Logger logger = Logger.getLogger(FileCommandReader.class.getName());
    private CommandFactory commandFactory;

    public FileCommandReader(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void processFile(String fileName, Context context) throws Exception {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    // Skip empty lines and comments
                    continue;
                }
                String[] parts = line.split(" ", 2);
                String commandName = parts[0];
                String argument = parts.length > 1 ? parts[1] : null;

                Command command = commandFactory.getCommand(commandName);
                command.execute(context, argument != null ? Arrays.asList(argument.split(" ")) : Collections.emptyList());
            }
        } catch (FileNotFoundException e) {
            logger.severe("File not found: " + fileName);
            throw new ClassNotFoundException("File not found", e);
        } catch (Exception e) {
            logger.severe("Error processing command.");
            throw new Exception("Error processing command.", e);
        }
    }
}