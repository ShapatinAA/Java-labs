package ru.nsu.shapatin.exceptions;

public class UnknownCommandException extends CommandException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
