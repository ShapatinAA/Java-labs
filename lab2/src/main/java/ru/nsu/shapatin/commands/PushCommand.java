package ru.nsu.shapatin.commands;

import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

import java.util.*;
import java.util.logging.Logger;

public class PushCommand implements Command {
    private static Logger logger = Logger.getLogger(AddCommand.class.getName());
    @Override
    public void execute(Context context, List<String> args) throws CommandException {
        if (args.isEmpty()) {
            logger.severe("Failed to perform PUSH command: No arguments found for PUSH command.");
            throw new CommandException("No arguments found for PUSH command");
        }
        String arg = args.get(0);
        try {
            double value = Double.parseDouble(arg);
            context.stack.push(value);
            logger.info("Performed PUSH command: " + value +  " is now on stack.");
        } catch (NumberFormatException e) {
            Double value = context.parameters.get(arg);
            if (value != null) {
                context.stack.push(value);
                logger.info("Performed PUSH command of variable " + arg + " (" + value + ") onto the stack");
            } else {
                logger.severe("Failed to perform PUSH command: Invalid number format for PUSH command.");
                throw new CommandException("Invalid number format for PUSH command", e);
            }
        }
    }
}
