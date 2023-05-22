package ru.nsu.shapatin.commands;

import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

import java.util.*;
import java.util.logging.Logger;

public class DefineCommand implements Command {
    private static Logger logger = Logger.getLogger(AddCommand.class.getName());
    @Override
    public void execute(Context context, List<String> args) throws CommandException {
        if (args.size() < 2) {
            logger.warning("Failed to DEFINE command: Not enough arguments for DEFINE command.");
            throw new CommandException("Not enough arguments for DEFINE command");
        }
        try {
            String key = args.get(0);
            double value = Double.parseDouble(args.get(1));
            context.parameters.put(key, value);
            logger.info("Performed DEFINE command: " + key +  " is now bound to " + value);
        } catch (NumberFormatException e) {
            logger.severe("Failed to DEFINE command: Invalid number format for DEFINE command.");
            throw new CommandException("Invalid number format for DEFINE command", e);
        }
    }
}
