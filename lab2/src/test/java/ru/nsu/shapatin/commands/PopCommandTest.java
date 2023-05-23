package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;
import ru.nsu.shapatin.exceptions.EmptyStackException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PopCommandTest {

    @Test
    public void testExecute_PopsValueFromStack() throws CommandException {
        // Arrange
        PopCommand popCommand = new PopCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(10.0);
        context.stack.push(5.0);

        // Act
        popCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(10.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForEmptyStack() {
        // Arrange
        PopCommand popCommand = new PopCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(EmptyStackException.class, () -> {
            popCommand.execute(context, args);
        });
    }
}