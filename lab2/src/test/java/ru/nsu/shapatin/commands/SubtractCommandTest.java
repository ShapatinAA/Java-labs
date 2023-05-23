package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SubtractCommandTest {

    @Test
    public void testExecute_PerformsSubtractionAndAddsResultToStack() throws CommandException {
        // Arrange
        SubtractCommand subtractCommand = new SubtractCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(7.0);
        context.stack.push(3.0);

        // Act
        subtractCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(4.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForInsufficientStackValues() {
        // Arrange
        SubtractCommand subtractCommand = new SubtractCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            subtractCommand.execute(context, args);
        });
    }
}