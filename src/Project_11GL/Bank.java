package Project_11GL;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Интерфейс Наблюдателя
interface CashObserver {
    void update(int amount);
}

// Класс Субъекта
class CashSubject {
    private final List<CashObserver> observers = new ArrayList<>();

    public void addObserver(CashObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(int amount) {
        for (CashObserver observer : observers) {
            observer.update(amount);
        }
    }
}

// Банковская система
class BankSystem implements CashObserver {
    private static final int MAX_CASH = 10000; // Максимум наличных в кассе
    static final int MIN_CASH = 2000;         // Минимум наличных в кассе
    static final int CASH_FROM_STORAGE = 5000; // Сумма пополнения из хранилища

    private int cashInRegister;
    private int cashInStorage;
    private final Queue<Client> clientQueue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    public BankSystem(int initialCash, int initialStorage, CashSubject cashSubject) {
        this.cashInRegister = initialCash;
        this.cashInStorage = initialStorage;
        cashSubject.addObserver(this); // Регистрация как наблюдателя
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
        lock.lock();
        try {
            if (amount <= 0) {
                System.out.println("Сумма для снятия должна быть положительной.");
                return;
            }

            if (cashInRegister >= amount) {
                cashInRegister -= amount;
                System.out.println("Снятие: " + amount + ". Осталось в кассе: " + cashInRegister);
            } else {
                // Пополнение кассы, если недостаточно средств
                int shortage = amount - cashInRegister;
                System.out.println("Недостаточно средств в кассе для снятия " + amount + ". Требуется пополнение на " + shortage + ".");

                if (cashInStorage >= shortage) {
                    cashInRegister += shortage;
                    cashInStorage -= shortage;
                    System.out.println("Касса пополнена на " + shortage + " из хранилища. Сумма в хранилище: " + cashInStorage);
                    cashInRegister -= amount;
                    System.out.println("Снятие: " + amount + ". Осталось в кассе: " + cashInRegister);
                } else {
                    System.out.println("Невозможно пополнить кассу. Недостаточно средств в хранилище.");
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void depositCash(int amount) {
        lock.lock();
        try {
            if (amount > 0) {
                cashInRegister += amount;
                System.out.println("Пополнение: " + amount + ". В кассе: " + cashInRegister);
            } else {
                System.out.println("Сумма должна быть положительной.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void monitorCash() {
        lock.lock();
        try {
            if (cashInRegister > MAX_CASH) {
                int toStorage = cashInRegister - MAX_CASH;
                cashInRegister -= toStorage;
                cashInStorage += toStorage;
                System.out.println("Перевод в хранилище: " + toStorage + ". Хранилище: " + cashInStorage);
            } else if (cashInRegister < MIN_CASH) {
                int fromStorage = Math.min(CASH_FROM_STORAGE, cashInStorage);
                if (fromStorage > 0) {
                    cashInRegister += fromStorage;
                    cashInStorage -= fromStorage;
                    System.out.println("Пополнение из хранилища: " + fromStorage + ". Хранилище: " + cashInStorage);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(int amount) {
        depositCash(amount);
    }

    public int getCashInRegister() {
        return cashInRegister;
    }

    public int getCashInStorage() {
        return cashInStorage;
    }
}

// Клиент
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

// Поток для пополнения наличных
class CashRefiller extends Thread {
    private final CashSubject cashSubject;
    private final BankSystem bank;
    private final int refillThreshold;
    private volatile boolean running = true;

    public CashRefiller(CashSubject cashSubject, BankSystem bank, int refillThreshold) {
        this.cashSubject = cashSubject;
        this.bank = bank;
        this.refillThreshold = refillThreshold;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(5000); // Проверка каждые 5 секунд
                if (bank.getCashInRegister() < refillThreshold) {
                    cashSubject.notifyObservers(BankSystem.CASH_FROM_STORAGE);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopRefiller() {
        running = false;
        interrupt();
    }
}

// Основной класс
public class Bank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CashSubject cashSubject = new CashSubject();
        BankSystem bank = new BankSystem(5000, 20000, cashSubject);
        CashRefiller cashRefiller = new CashRefiller(cashSubject, bank, BankSystem.MIN_CASH);
        cashRefiller.start();

        while (true) {
            System.out.println("\nВыберите действие: \n1 - Снять \n2 - Пополнить \n3 - Посмотреть кассу \n0 - Выход");
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода. Пожалуйста, введите число.");
                scanner.next(); // Очистка некорректного ввода
                continue;
            }

            if (choice == 0) {
                cashRefiller.stopRefiller();
                System.out.println("Программа завершена.");
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
                    } catch (InputMismatchException e) {
                        System.out.println("Некорректный ввод суммы. Пожалуйста, введите число.");
                        scanner.next(); // Очистка некорректного ввода
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
                    } catch (InputMismatchException e) {
                        System.out.println("Некорректный ввод суммы. Пожалуйста, введите число.");
                        scanner.next(); // Очистка некорректного ввода
                        continue;
                    }
                    bank.addClient(new Client(depositName, "пополнить", depositAmount));
                    bank.processClients();
                }
                case 3 -> {
                    System.out.println("Наличность в кассе: " + bank.getCashInRegister());
                    System.out.println("Наличность в хранилище: " + bank.getCashInStorage());
                }
                default -> System.out.println("Неверный выбор.");
            }
        }

        scanner.close();
    }
}
