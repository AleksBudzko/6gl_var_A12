package Project_11GL;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class BankSystem {
    private static final int MAX_CASH = 10000; // Максимум наличных в кассе
    private static final int MIN_CASH = 2000;  // Минимум наличных в кассе
    private static final int CASH_FROM_STORAGE = 5000; // Сумма пополнения из хранилища

    private int cashInRegister;
    private int cashInStorage;
    private final Queue<Client> clientQueue;

    public BankSystem(int CashInRegister, int CashInStorage) {
        this.cashInRegister = CashInRegister;
        this.cashInStorage = CashInStorage;
        this.clientQueue = new LinkedList<>();
    }

    public void addClient(Client client) {
        clientQueue.add(client);
    }

    public void processClients() {
        while (!clientQueue.isEmpty()) {
            Client client = clientQueue.poll();
            client.performOperation(this);
            monitorCash();
        }
    }

    public void withdrawCash(int amount) {
        if (amount <= 0) {
            System.out.println("Сумма для снятия должна быть положительной.");
            return;
        }
        if (cashInRegister >= amount) {
            cashInRegister -= amount;
            System.out.println("Клиент снял " + amount + ". Наличность в кассе: " + cashInRegister);
        } else {
            System.out.println("Недостаточно наличных в кассе для снятия " + amount);
        }
    }

    public void depositCash(int amount) {
        if (amount <= 0) {
            System.out.println("Сумма для пополнения должна быть положительной.");
            return;
        }
        cashInRegister += amount;
        System.out.println("Клиент внес " + amount + ". Наличность в кассе: " + cashInRegister);
    }

    public void monitorCash() {
        if (cashInRegister > MAX_CASH) {
            int toStorage = cashInRegister - MAX_CASH;
            cashInStorage += toStorage;
            cashInRegister -= toStorage;
            System.out.println("Перевод в хранилище: " + toStorage + ". Хранилище: " + cashInStorage);
        } else if (cashInRegister < MIN_CASH) {
            int fromStorage = Math.min(CASH_FROM_STORAGE, cashInStorage);
            cashInRegister += fromStorage;
            cashInStorage -= fromStorage;
            System.out.println("Пополнение из хранилища: " + fromStorage + ". Хранилище: " + cashInStorage);
        }
    }

    public int getCashInRegister() {
        return cashInRegister;
    }
}

class Client {
    private final String name;
    private final String operation;
    private final int amount;

    public Client(String name, String operation, int amount) {
        this.name = name;
        this.operation = operation;
        this.amount = amount;
    }

    public void performOperation(BankSystem bank) {
        System.out.println("Обслуживается клиент: " + name);
        switch (operation.toLowerCase()) {
            case "снять" -> bank.withdrawCash(amount);
            case "пополнить" -> bank.depositCash(amount);
            default -> System.out.println("Неизвестная операция: " + operation);
        }
    }
}

public class Bank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankSystem bank = new BankSystem(5000, 20000); // Инициализация банка с 5000 в кассе и 20000 в хранилище

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Снять наличные");
            System.out.println("2 - Пополнить наличные");
            System.out.println("3 - Посмотреть состояние кассы");
            System.out.println("0 - Выход");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите число.");
                scanner.next();
                continue;
            }

            if (choice == 0) {
                System.out.println("Выход из программы.");
                break;
            }

            switch (choice) {
                case 1 -> { // Снятие наличных
                    System.out.print("Введите имя клиента: ");
                    String withdrawName = scanner.next();
                    System.out.print("Введите сумму для снятия: ");
                    int withdrawAmount;
                    try {
                        withdrawAmount = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("Некорректный ввод суммы. Пожалуйста, введите число.");
                        scanner.next();
                        continue;
                    }
                    bank.addClient(new Client(withdrawName, "снять", withdrawAmount));
                    bank.processClients();
                }
                case 2 -> { // Пополнение наличных
                    System.out.print("Введите имя клиента: ");
                    String depositName = scanner.next();
                    System.out.print("Введите сумму для пополнения: ");
                    int depositAmount;
                    try {
                        depositAmount = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("Некорректный ввод суммы. Пожалуйста, введите число.");
                        scanner.next();
                        continue;
                    }
                    bank.addClient(new Client(depositName, "пополнить", depositAmount));
                    bank.processClients();
                }
                case 3 -> // Состояние кассы
                        System.out.println("Наличность в кассе: " + bank.getCashInRegister());
                default -> System.out.println("Некорректный выбор, попробуйте снова.");
            }
        }

        scanner.close();
    }
}