package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PushCommandTest {

    @Test
    public void testExecute_PushesDoubleValueToStack() throws CommandException {
        // Arrange
        PushCommand pushCommand = new PushCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        args.add("5.0");

        // Act
        pushCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(5.0, result, 0.001);
    }

    @Test
    public void testExecute_PushesParameterValueToStack() throws CommandException {
        // Arrange
        PushCommand pushCommand = new PushCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        args.add("radius");
        context.parameters.put("radius", 10.0);

        // Act
        pushCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(10.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForInvalidNumberFormat() {
        // Arrange
        PushCommand pushCommand = new PushCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        args.add("abc");

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            pushCommand.execute(context, args);
        });
    }

    @Test
    public void testExecute_ThrowsExceptionForUndefinedParameterValue() {
        // Arrange
        PushCommand pushCommand = new PushCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        args.add("radius");

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            pushCommand.execute(context, args);
        });
    }
}