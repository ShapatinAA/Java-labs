package ru.nsu.shapatin.commands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.util.*;

public class DefineCommandTest {

    @Test
    public void testExecute_AddsParameterToContext() throws CommandException {
        // Arrange
        DefineCommand defineCommand = new DefineCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        args.add("radius");
        args.add("10.0");

        // Act
        defineCommand.execute(context, args);
        double result = context.parameters.get("radius");

        // Assert
        Assertions.assertEquals(10.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForInsufficientArguments() {
        // Arrange
        DefineCommand defineCommand = new DefineCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        args.add("key");  // Missing value

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            defineCommand.execute(context, args);
        });
    }

    @Test
    public void testExecute_ThrowsExceptionForInvalidNumberFormat() {
        // Arrange
        DefineCommand defineCommand = new DefineCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        args.add("radius");
        args.add("abc");  // Invalid number format

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            defineCommand.execute(context, args);
        });
    }
}