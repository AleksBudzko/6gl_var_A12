package Project_10GL;

import java.util.HashMap;
import java.util.Scanner;

class ParkingLot {
    private boolean[] parkingSpaces; // Массив парковочных мест (true = занято, false = свободно)

    public ParkingLot(int size) {
        parkingSpaces = new boolean[size]; // Инициализация парковки с заданным количеством мест
    }

    // Метод для приезда машины
    public boolean parkCar(int preferredSpot) {
        System.out.println("Машина пытается припарковаться на месте " + preferredSpot);

        // Проверяем места от заданного до конца
        for (int i = preferredSpot; i < parkingSpaces.length; i++) {
            if (!parkingSpaces[i]) {
                parkingSpaces[i] = true; // Занимаем место
                System.out.println("Машина припарковалась на месте " + i);
                return true;
            }
        }

        System.out.println("Нет свободных мест для парковки!");
        return false; // Нет свободных мест
    }

    // Метод для отъезда машины
    public boolean leaveParking(int spot) {
        if (spot >= 0 && spot < parkingSpaces.length && parkingSpaces[spot]) {
            parkingSpaces[spot] = false; // Освобождаем место
            System.out.println("Машина уехала с места " + spot);
            return true;
        }

        System.out.println("Ошибка: место " + spot + " уже свободно или некорректное.");
        return false; // Некорректное место или уже свободно
    }

    // Вывод состояния парковки
    public void printParkingState() {
        System.out.print("Состояние парковки: ");
        for (int i = 0; i < parkingSpaces.length; i++) {
            System.out.print((parkingSpaces[i] ? "[X]" : "[ ]") + " "); // [X] занято, [ ] свободно
        }
        System.out.println();
    }
}

public class Parking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Пользователь задает количество мест на парковке
        int parkingSize;
        while (true) {
            System.out.print("Введите количество мест на парковке (положительное число): ");
            parkingSize = scanner.nextInt();
            if (parkingSize > 0) break; // Проверка, что введено положительное число
            System.out.println("Ошибка! Количество мест должно быть положительным числом.");
        }

        ParkingLot parkingLot = new ParkingLot(parkingSize);

        // Создаем HashMap для выбора действия
        HashMap<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, () -> {
            parkingLot.printParkingState();
        });
        actions.put(2, () -> {
            System.out.print("Введите место, с которого хотите припарковаться: ");
            int spot = scanner.nextInt();
            parkingLot.parkCar(spot);
        });
        actions.put(3, () -> {
            System.out.print("Введите место, откуда уезжает машина: ");
            int spot = scanner.nextInt();
            parkingLot.leaveParking(spot);
        });

        // Основное меню
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Показать состояние парковки");
            System.out.println("2 - Припарковать машину");
            System.out.println("3 - Убрать машину");
            System.out.println("0 - Выход");

            int choice = scanner.nextInt();
            if (choice == 0) {
                System.out.println("Выход из программы.");
                break;
            }

            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Некорректный выбор, попробуйте снова.");
            }
        }

        scanner.close();
    }
}




