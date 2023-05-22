package ru.nsu.shapatin.commands;

import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

import java.util.List;
import java.util.logging.Logger;

public class PopCommand implements Command {
    private static Logger logger = Logger.getLogger(AddCommand.class.getName());
    @Override
    public void execute(Context context, List<String> args) throws CommandException {
        if (context.stack.isEmpty()) {
            logger.warning("Failed to perform POP command: Stack is empty.");
            throw new EmptyStackException("Stack is empty");
        }
        Double popped = context.stack.pop();
        logger.info("Performed POP command: " + popped + " is now begone.");
    }
}