package ru.nsu.shapatin.IO;

import ru.nsu.shapatin.commands.Command;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.factory.CommandFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.*;

public class StandardInputCommandReader {
    private static Logger logger = Logger.getLogger(StandardInputCommandReader.class.getName());
    private CommandFactory commandFactory;

    public StandardInputCommandReader(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void processStandardInput(Context context) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
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
                logger.info("Begin execution of commands from stdin.");
                command.execute(context, argument != null ? Arrays.asList(argument.split(" ")) : Collections.emptyList());
            }
        } catch (Exception e) {
            logger.severe("Error processing command.");
            throw new Exception("Error processing command.", e);
        }
    }
}