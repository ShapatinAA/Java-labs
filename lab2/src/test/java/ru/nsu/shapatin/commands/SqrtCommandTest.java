package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SqrtCommandTest {

    @Test
    public void testExecute_CalculatesSquareRootAndAddsResultToStack() throws CommandException {
        // Arrange
        SqrtCommand sqrtCommand = new SqrtCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(9.0);

        // Act
        sqrtCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(3.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForNegativeValue() {
        // Arrange
        SqrtCommand sqrtCommand = new SqrtCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(-4.0);

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            sqrtCommand.execute(context, args);
        });
    }
}