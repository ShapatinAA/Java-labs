package ru.nsu.shapatin.commands;

import java.util.List;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.*;

public interface Command {
    void execute(Context context, List<String> args) throws CommandException;
}