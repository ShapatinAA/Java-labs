package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.util.ArrayList;
import java.util.List;

public class AddCommandTest {

    @Test
    public void testExecute_AddsTwoValuesToStack() throws CommandException {
        // Arrange
        AddCommand addCommand = new AddCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(5.0);
        context.stack.push(7.0);

        // Act
        addCommand.execute(context, args);
        Double result = context.stack.pop();

        // Assert
        Assertions.assertEquals(12.0, result, 0.001);
    }

    @Test
    public void testExecute_ThrowsExceptionForInsufficientStackValues() {
        // Arrange
        AddCommand addCommand = new AddCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            addCommand.execute(context, args);
        });
    }
}
