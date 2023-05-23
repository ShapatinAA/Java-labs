package ru.nsu.shapatin.commands;

import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

import java.util.*;
import java.util.logging.Logger;

public class DivideCommand implements Command {
    private static Logger logger = Logger.getLogger(AddCommand.class.getName());
    @Override
    public void execute(Context context, List<String> args) throws CommandException {
        if (context.stack.size() < 2) {
            logger.warning("Failed to perform division: Not enough values on the stack for the operation.");
            throw new CommandException("Not enough values on the stack for the operation");
        }
        Double val1 = context.stack.pop();
        Double val2 = context.stack.pop();
        if (val1 == 0) {
            logger.warning("Failed to perform division: Cannot divide by zero.");
            throw new CommandException("Cannot divide by zero");
        }
        Double result = val2 / val1;
        context.stack.push(result);
        logger.info("Performed division: " + val2 + " / " + val1 + " = " + result);
    }
}