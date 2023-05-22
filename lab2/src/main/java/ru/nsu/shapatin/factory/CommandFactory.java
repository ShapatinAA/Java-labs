package ru.nsu.shapatin.factory;

import ru.nsu.shapatin.commands.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class CommandFactory {
    private static Logger logger = Logger.getLogger(CommandFactory.class.getName());
    private Map<String, Class<? extends Command>> commandMap;

    public CommandFactory() throws Exception {
        commandMap = new HashMap<>();

        // Загрузка команд из файла конфигурации
        try (InputStream input = CommandFactory.class.getResourceAsStream("/commands.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            for (String commandName : prop.stringPropertyNames()) {
                String className = prop.getProperty(commandName);
                try {
                    Class<? extends Command> commandClass = Class.forName(className).asSubclass(Command.class);
                    commandMap.put(commandName.toUpperCase(), commandClass);
                    logger.info("Loaded command " + commandName + " with class " + className);
                } catch (ClassNotFoundException | ClassCastException e) {
                    logger.severe("Failed to load command class: " + e.getMessage());
                    throw new Exception("Failed to load command class: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.severe("Failed to load command configuration: " + e.getMessage());
            throw new Exception("Failed to load command configuration: " + e.getMessage(), e);
        }
    }

    public Command getCommand(String commandName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<? extends Command> commandClass = commandMap.get(commandName);
        if (commandClass != null) {
            try {
                logger.info("Getting " + commandName + " new instance.");
                return commandClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.severe("Exception occurred in getting new Instance of " + commandName + " command");
                throw e; // Rethrow the exception so it can be handled upstream
            }
        } else {
            logger.warning("Unknown command: " + commandName);
            throw new ClassNotFoundException("Command " + commandName + " not found");
        }
    }
}