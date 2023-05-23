package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DivideCommandTest {

    @Test
    public void testExecute_PerformsDivisionAndAddsResultToStack() throws CommandException {
        // Arrange
        DivideCommand divideCommand = new DivideCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(10.0);
        context.stack.push(5.0);

        // Act
        divideCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForInsufficientStackValues() {
        // Arrange
        DivideCommand divideCommand = new DivideCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            divideCommand.execute(context, args);
        });
    }

    @Test
    public void testExecute_ThrowsExceptionForDividingByZero() {
        // Arrange
        DivideCommand divideCommand = new DivideCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(10.0);
        context.stack.push(0.0);

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            divideCommand.execute(context, args);
        });
    }
}