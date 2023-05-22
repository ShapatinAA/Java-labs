package ru.nsu.shapatin.commands;

import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

import java.util.*;
import java.util.logging.Logger;

public class PrintCommand implements Command {
    private static Logger logger = Logger.getLogger(AddCommand.class.getName());
    @Override
    public void execute(Context context, List<String> args) throws CommandException {
        if (context.stack.isEmpty()) {
            logger.warning("Failed to perform PRINT command: Stack is empty, can't print top element.");
            throw new CommandException("Stack is empty, can't print top element");
        }
        System.out.println(context.stack.peek());
        logger.info("Performed PRINT command: " + context.stack.peek() + " is now printed to specified file.");
    }
}