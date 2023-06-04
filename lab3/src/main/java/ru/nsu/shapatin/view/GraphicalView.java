package ru.nsu.shapatin.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.shapatin.controller.GameController;
import ru.nsu.shapatin.model.Cell;
import ru.nsu.shapatin.model.Game;
import ru.nsu.shapatin.model.HighScoreEntry;
import ru.nsu.shapatin.model.HighScoreTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class GraphicalView implements View {
    private Game game;
    private GameController gameController;
    private JFrame frame;
    private JPanel gamePanel;
    private JButton newGameButton;
    private JButton highScoresButton;
    private JButton aboutButton;
    private JButton exitButton;
    private static Logger logger = LogManager.getLogger(GraphicalView.class.getName());

    public GraphicalView(Game game, GameController gameController) {
        this.game = game;
        this.gameController = gameController;
        logger.info("New instance of GraphicalView created.");
        setupUI();
    }
    private void setupUI() {
        try {
            logger.info("Setting up UI.");
            frame = new JFrame("Minesweeper");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            gamePanel = new JPanel();
            gamePanel.setLayout(new GridLayout(game.getWidth(), game.getHeight()));

            startNewGame();

            newGameButton = new JButton("New Game");
            newGameButton.addActionListener(e -> gameController.startNewGame());
            logger.info("New Game button created.");

            highScoresButton = new JButton("High Scores");
            highScoresButton.addActionListener(e -> gameController.displayHighScores());
            logger.info("High Scores button created.");

            aboutButton = new JButton("About");
            aboutButton.addActionListener(e -> gameController.displayAbout());
            logger.info("About button created.");

            exitButton = new JButton("Exit");
            exitButton.addActionListener(e -> gameController.exitGame());
            logger.info("Exit button created.");

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 4));
            buttonPanel.add(newGameButton);
            buttonPanel.add(highScoresButton);
            buttonPanel.add(aboutButton);
            buttonPanel.add(exitButton);
            logger.info("All buttons added to the button panel.");

            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
            frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            logger.info("All grid and button panel added to the frame.");

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            logger.fatal("Error in setting up UI");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void startNewGame() {
        try {
            logger.info("Making new graphical game grid layout.");
            gamePanel.removeAll();

            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    JButton cellButton = new JButton();
                    cellButton.setPreferredSize(new Dimension(50, 50));
                    cellButton.putClientProperty("point", new Point(x, y));

                    final int xPos = x;
                    final int yPos = y;
                    cellButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                gameController.cellClicked(new Point(xPos, yPos), MouseEvent.BUTTON1);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                gameController.cellClicked(new Point(xPos, yPos), MouseEvent.BUTTON3);
                            }
                        }
                    });

                    gamePanel.add(cellButton);
                }
            }

            frame.revalidate();
            frame.repaint();
            render();
        } catch (Exception e) {
            logger.fatal("Error in starting new game");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void render() {
        try {
            logger.info("Rendering graphical game layout.");
            for (Component component : gamePanel.getComponents()) {
                if (component instanceof JButton) {
                    JButton cellButton = (JButton) component;
                    Point point = (Point) cellButton.getClientProperty("point");
                    Cell cell = game.getCell(point);
                    updateCellButton(cellButton, cell);
                }
            }
        } catch (Exception e) {
            logger.fatal("Error in rendering game layout");
            throw new RuntimeException(e);
        }
    }
    private void updateCellButton(JButton cellButton, Cell cell) {
        try {
            if (cell.getState() == Cell.State.COVERED) {
                ImageIcon dirtIcon = new ImageIcon("src/main/resources/pictures/dirt.jpeg");
                cellButton.setIcon(dirtIcon);
                cellButton.revalidate();
                cellButton.repaint();
                cellButton.setEnabled(true);
            } else if (cell.getState() == Cell.State.FLAGGED) {
                ImageIcon flagIcon = new ImageIcon("src/main/resources/pictures/flag.jpeg");
                cellButton.setIcon(flagIcon);
                cellButton.revalidate();
                cellButton.repaint();
                cellButton.setEnabled(true);
            } else if (cell.getState() == Cell.State.UNCOVERED) {
                if (cell.getHasMine()) {
                    ImageIcon bombIcon = new ImageIcon("src/main/resources/pictures/bomb.jpeg");
                    cellButton.setIcon(bombIcon);
                    cellButton.revalidate();
                    cellButton.repaint();
                }
                else {
                    int nearbyMines = cell.getNearbyMines();
                    if (nearbyMines > 0) {
                        JLabel textLabel = new JLabel(Integer.toString(nearbyMines));
                        textLabel.setForeground(Color.RED); // Настраиваем цвет текста
                        textLabel.setHorizontalAlignment(SwingConstants.CENTER); // Выравнивание текста по центру
                        cellButton.setLayout(new BorderLayout());
                        cellButton.add(textLabel, BorderLayout.CENTER);
                        cellButton.revalidate();
                        cellButton.repaint();
                    } else {
                        cellButton.setText("");
                    }
                }
                cellButton.setEnabled(false);
            }
        } catch (Exception e) {
            logger.fatal("Error in updating cell button");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void displayHighScores() {
        try {
            logger.info("Displaying HighScores in graphical game.");
            HighScoreTable highScores = game.getHighScoreTable();

            StringBuilder sb = new StringBuilder();
            sb.append("Name\t\tScore\n");

            for (HighScoreEntry entry : highScores.getEntries()) {
                sb.append(entry.getName()).append("\t\t").append(entry.getScore()).append("\n");
            }

            JOptionPane.showMessageDialog(frame, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            logger.fatal("Error in displaying high scores");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void displayAbout() {
        try {
            logger.info("Displaying About in graphical game.");
            String aboutText = "Minesweeper\n\n" +
                    "Minesweeper is a classic puzzle game where the player\n" +
                    "needs to uncover all the cells on the grid without\n" +
                    "triggering any mines.\n\n" +
                    "Developed by: me\n" +
                    "Version: 1.0";

            JOptionPane.showMessageDialog(frame, aboutText, "About Minesweeper", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            logger.fatal("Error in displaying About");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void exitGame() {
        try {
            logger.info("Exiting graphical game.");
            frame.dispose();
        } catch (Exception e) {
            logger.fatal("Error in exiting game");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void displayWin(int score) {
        try {
            logger.info("You won graphical game.");
            String message = "Поздравляем! Вы выиграли!\nВаш счет: " + score;

            Object[] options = {"Новая игра", "Выход"};
            int n = JOptionPane.showOptionDialog(frame,
                    message,
                    "Game is Over!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (n == 0) {
                logger.info("Button 1 chosen - start of new graphical game.");
                gameController.startNewGame();
            } else {
                logger.info("Button 2 chosen - end of graphical game.");
                gameController.exitGame();
            }
        } catch (Exception e) {
            logger.fatal("Error in displaying win message");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void displayLose(int score) {
        try {
            logger.info("You lost graphical game.");
            String message = "Вы проиграли.\nВаш счет: " + score;

            Object[] options = {"Новая игра", "Выход"};
            int n = JOptionPane.showOptionDialog(frame,
                    message,
                    "Game is Over!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (n == 0) {
                logger.info("Button 1 chosen - start of new graphical game.");
                gameController.startNewGame();
            } else {
                logger.info("Button 2 chosen - end of graphical game.");
                gameController.exitGame();
            }
        } catch (Exception e) {
            logger.fatal("Error in displaying lose message");
            throw new RuntimeException(e);
        }
    }
}