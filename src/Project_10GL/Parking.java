package Project_10GL;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

class ParkingLot {
    private boolean[] parkingSpaces; // Массив парковочных мест (true = занято, false = свободно)

    public ParkingLot(int size) {
        parkingSpaces = new boolean[size]; // Инициализация парковки с заданным количеством мест
    }

    // Метод для приезда машины
    public boolean parkCar(int preferredSpot) {
        // Проверяем, что место начинается с 1, преобразуем в индекс массива
        int index = preferredSpot - 1;
        System.out.println("Машина пытается припарковаться на месте " + preferredSpot);

        // Проверяем места от заданного до конца
        for (int i = index; i < parkingSpaces.length; i++) {
            if (!parkingSpaces[i]) {
                parkingSpaces[i] = true; // Занимаем место
                System.out.println("Машина припарковалась на месте " + (i + 1));
                return true;
            }
        }

        System.out.println("Нет свободных мест для парковки!");
        return false; // Нет свободных мест
    }

    // Метод для отъезда машины
    public boolean leaveParking(int spot) {
        // Проверяем, что место начинается с 1, преобразуем в индекс массива
        int index = spot - 1;
        if (index >= 0 && index < parkingSpaces.length && parkingSpaces[index]) {
            parkingSpaces[index] = false; // Освобождаем место
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
            System.out.print("Введите количество мест на парковке: ");
            try {
                parkingSize = scanner.nextInt();
                if (parkingSize > 0) break; // Проверка, что введено положительное число
                System.out.println("Некорректный ввод. Количество мест должно быть положительным числом.");
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Введите целое число.");
                scanner.next(); // Сбрасываем неверный ввод
            }
        }

        ParkingLot parkingLot = new ParkingLot(parkingSize);

        // Создаем HashMap для выбора действия
        HashMap<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, () -> parkingLot.printParkingState());
        actions.put(2, () -> {
            System.out.print("Введите место, на которое хотите припарковаться: ");
            try {
                int spot = scanner.nextInt();
                parkingLot.parkCar(spot);
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Введите целое число.");
                scanner.next(); // Сбрасываем неверный ввод
            }
        });
        actions.put(3, () -> {
            System.out.print("Введите место, откуда уезжает машина: ");
            try {
                int spot = scanner.nextInt();
                parkingLot.leaveParking(spot);
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Введите целое число.");
                scanner.next(); // Сбрасываем неверный ввод
            }
        });
        actions.put(0, () -> System.out.println("Выход из программы."));

        // Основное меню
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Показать состояние парковки");
            System.out.println("2 - Припарковать машину");
            System.out.println("3 - Убрать машину");
            System.out.println("0 - Выход");

            int choice;
            try {
                choice = scanner.nextInt();
                Runnable action = actions.get(choice);
                if (action != null) {
                    action.run();
                    if (choice == 0) {
                        break; // Выход из цикла, если выбрано действие выхода
                    }
                } else {
                    System.out.println("Некорректный выбор, попробуйте снова.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Введите целое число.");
                scanner.next(); // Сбрасываем неверный ввод
            }
        }

        scanner.close();
    }
}