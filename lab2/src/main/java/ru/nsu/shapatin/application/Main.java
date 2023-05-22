package ru.nsu.shapatin.application;


import ru.nsu.shapatin.IO.FileCommandReader;
import ru.nsu.shapatin.IO.ResultWriter;
import ru.nsu.shapatin.IO.StandardInputCommandReader;
import ru.nsu.shapatin.factory.CommandFactory;
import ru.nsu.shapatin.context.Context;

import java.io.*;
import java.nio.file.*;
import java.util.logging.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger(FileCommandReader.class.getName());
        FileHandler fh;
        final String projectPath = new File("").getAbsolutePath();
        final Path resourcesPath = Paths.get(projectPath, "src", "main", "resources");
        final String logFilePath = Paths.get(resourcesPath.toString(), "CalculatorLog.log").toString();
        try {
            fh = new FileHandler(logFilePath, true); // The second argument 'true' indicates the file will be appended to, rather than overwritten
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            Logger rootLogger = Logger.getLogger("");
            rootLogger.addHandler(fh);
        } catch (SecurityException | IOException e) {
            logger.severe("Failed to initialize logger FileHandler.");
            System.err.println("Failed to initialize logger FileHandler: " + e.getMessage());
            logger.severe("Exit the program.");
            return;
        }
        CommandFactory commandFactory;
        try {
             commandFactory = new CommandFactory();
             Context context = new Context();
            if (args.length > 0) {
                // Create the file command reader
                FileCommandReader fileCommandReader = new FileCommandReader(commandFactory);

                // Process the command file
                try {
                    fileCommandReader.processFile(args[0], context);
                } catch (Exception e) {
                    System.err.println("Failed to process file: " + e.getMessage());
                    logger.severe("Exit the program.");
                    return;
                }
            } else {
                // Create the console command reader
                StandardInputCommandReader standardInputCommandReader = new StandardInputCommandReader(commandFactory);

                try {
                    standardInputCommandReader.processStandardInput(context);
                } catch (Exception e) {
                    System.err.println("Failed to process file: " + e.getMessage());
                    logger.severe("Exit the program.");
                    return;
                }
            }
            ResultWriter resultWriter = new ResultWriter();
            final String resultFilePath = Paths.get(resourcesPath.toString(), "/result.txt").toString();
            resultWriter.writeResult(context, resultFilePath);
        } catch (Exception e) {
            logger.severe("Failed to make commandFactory instance.");
            System.err.println("Failed to make commandFactory instance: " + e.getMessage());
            logger.severe("Exit the program.");
            return;
        }

        logger.info("Exit the program.");
    }
}