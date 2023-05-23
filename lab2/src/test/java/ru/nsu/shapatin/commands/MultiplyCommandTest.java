package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MultiplyCommandTest {

    @Test
    public void testExecute_PerformsMultiplicationAndAddsResultToStack() throws CommandException {
        // Arrange
        MultiplyCommand multiplyCommand = new MultiplyCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(5.0);
        context.stack.push(7.0);

        // Act
        multiplyCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(35.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForInsufficientStackValues() {
        // Arrange
        MultiplyCommand multiplyCommand = new MultiplyCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            multiplyCommand.execute(context, args);
        });
    }
}