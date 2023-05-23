package ru.nsu.shapatin.commands;

import org.junit.jupiter.api.*;
import ru.nsu.shapatin.context.Context;
import ru.nsu.shapatin.exceptions.CommandException;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PrintCommandTest {

    @Test
    public void testExecute_PrintsTopElement() throws CommandException {
        // Arrange
        PrintCommand printCommand = new PrintCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();
        context.stack.push(10.0);
        context.stack.push(5.0);

        // Redirect standard output to capture printed message
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        // Act
        printCommand.execute(context, args);
        String printedOutput = outputStream.toString().trim();

        // Restore standard output
        System.out.flush();
        System.setOut(originalOut);

        // Assert
        Assertions.assertEquals("5.0", printedOutput);
    }

    @Test
    public void testExecute_ThrowsExceptionForEmptyStack() {
        // Arrange
        PrintCommand printCommand = new PrintCommand();
        Context context = new Context();
        List<String> args = new ArrayList<>();

        // Act and Assert
        Assertions.assertThrows(CommandException.class, () -> {
            printCommand.execute(context, args);
        });
    }
}