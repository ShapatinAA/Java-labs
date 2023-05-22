package ru.nsu.shapatin.commands;

import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

import java.util.*;
import java.util.logging.Logger;

import static java.lang.Math.sqrt;

public class SqrtCommand implements Command {
    private static Logger logger = Logger.getLogger(AddCommand.class.getName());
    @Override
    public void execute(Context context, List<String> args) throws CommandException {

        Double val1 = context.stack.pop();
        if (val1 < 0) {
            logger.warning("Failed to calculate square root: Argument of square root can not be negative.");
            throw new CommandException("Argument of SQRT can not be negative");
        }
        Double result = sqrt(val1);
        context.stack.push(result);
        logger.info("Calculated square root: SQRT(" + val1 + ") = " + result);
    }
}