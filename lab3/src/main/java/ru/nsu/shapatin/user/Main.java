package ru.nsu.shapatin.user;

import ru.nsu.shapatin.controller.*;
import ru.nsu.shapatin.model.*;

import java.util.*;

public class Main {
        public static void main(String[] args) {
            // Создание Scanner для чтения ввода пользователя
            Scanner scanner = new Scanner(System.in);

            // Запрос у пользователя имени игрока
            System.out.println("Введите имя игрока:");
            String playerName = scanner.nextLine();

            // Запрос размеров игрового поля
            System.out.println("Введите ширину игрового поля:");
            int width = scanner.nextInt();
            System.out.println("Введите высоту игрового поля:");
            int height = scanner.nextInt();

            // Запрос количества мин
            System.out.println("Введите количество мин:");
            int mineCount = scanner.nextInt();

            // Запрос режима отображения
            System.out.println("Введите режим отображения (text/graphic):");
            String mode = scanner.next();

            // Создание и инициализация игры и контроллера
            Game game = new Game(playerName, width, height, mineCount);
            GameController controller = new GameController(game, mode);
            controller.startNewGame();

            // Закрытие Scanner
            scanner.close();

            // Начало новой игры
            controller.startNewGame();
        }
}
