package ru.nsu.shapatin.commands;

import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

import java.util.*;
import java.util.logging.Logger;

public class SubtractCommand implements Command {
    private static Logger logger = Logger.getLogger(AddCommand.class.getName());
    @Override
    public void execute(Context context, List<String> args) throws CommandException {
        if (context.stack.size() < 2) {
            logger.warning("Failed to perform subtraction: Not enough values on the stack for the operation.");
            throw new CommandException("Not enough values on the stack for the operation");
        }
        Double val1 = context.stack.pop();
        Double val2 = context.stack.pop();
        Double result = val2 - val1;
        context.stack.push(result);
        logger.info("Performed subtraction: " + val2 + " - " + val1 + " = " + result);
    }
}