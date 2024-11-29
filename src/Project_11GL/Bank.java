package Project_11GL;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Observer interface
interface CashObserver {
    void update(int amount);
}

// Subject class
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

// Banking system
class BankSystem implements CashObserver {
    private static final int MAX_CASH = 10000; // Maximum cash in the register
    static final int MIN_CASH = 2000;         // Minimum cash in the register
    static final int CASH_FROM_STORAGE = 5000; // Amount to replenish from storage

    private int cashInRegister;
    private int cashInStorage;
    private final Queue<Client> clientQueue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    public BankSystem(int initialCash, int initialStorage, CashSubject cashSubject) {
        this.cashInRegister = initialCash;
        this.cashInStorage = initialStorage;
        cashSubject.addObserver(this); // Register as an observer
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
                System.out.println("Withdrawal amount must be positive.");
                return;
            }

            if (cashInRegister >= amount) {
                cashInRegister -= amount;
                System.out.println("Withdrawal: " + amount + ". Remaining in register: " + cashInRegister);
            } else {
                // Replenish the register if there is insufficient cash
                int shortage = amount - cashInRegister;
                System.out.println("Insufficient cash in register to withdraw " + amount + ". Shortage: " + shortage + ".");

                if (cashInStorage >= shortage) {
                    cashInRegister += shortage;
                    cashInStorage -= shortage;
                    System.out.println("Register replenished with " + shortage + " from storage. Remaining in storage: " + cashInStorage);
                    cashInRegister -= amount;
                    System.out.println("Withdrawal: " + amount + ". Remaining in register: " + cashInRegister);
                } else {
                    System.out.println("Unable to replenish the register. Insufficient storage funds.");
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
                System.out.println("Deposit: " + amount + ". Register total: " + cashInRegister);
            } else {
                System.out.println("Deposit amount must be positive.");
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
                System.out.println("Transferred to storage: " + toStorage + ". Storage total: " + cashInStorage);
            } else if (cashInRegister < MIN_CASH) {
                int fromStorage = Math.min(CASH_FROM_STORAGE, cashInStorage);
                if (fromStorage > 0) {
                    cashInRegister += fromStorage;
                    cashInStorage -= fromStorage;
                    System.out.println("Replenished from storage: " + fromStorage + ". Storage total: " + cashInStorage);
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

// Client
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
        System.out.println("Serving client: " + name);
        switch (operation.toLowerCase()) {
            case "withdraw" -> bank.withdrawCash(amount);
            case "deposit" -> bank.depositCash(amount);
            default -> System.out.println("Unknown operation: " + operation);
        }
    }
}

// Thread for cash refilling
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
                Thread.sleep(5000); // Check every 5 seconds
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

// Main class
public class Bank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CashSubject cashSubject = new CashSubject();
        BankSystem bank = new BankSystem(5000, 20000, cashSubject);
        CashRefiller cashRefiller = new CashRefiller(cashSubject, bank, BankSystem.MIN_CASH);
        cashRefiller.start();

        while (true) {
            System.out.println("\nChoose an action: \n1 - Withdraw \n2 - Deposit \n3 - Check cash \n0 - Exit");
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input error. Please enter a number.");
                scanner.next(); // Clear incorrect input
                continue;
            }

            if (choice == 0) {
                cashRefiller.stopRefiller();
                System.out.println("Program terminated.");
                break;
            }

            switch (choice) {
                case 1 -> { // Withdraw cash
                    System.out.print("Enter client name: ");
                    String withdrawName = scanner.next();
                    System.out.print("Enter withdrawal amount: ");
                    int withdrawAmount;
                    try {
                        withdrawAmount = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid amount input. Please enter a number.");
                        scanner.next(); // Clear incorrect input
                        continue;
                    }
                    bank.addClient(new Client(withdrawName, "withdraw", withdrawAmount));
                    bank.processClients();
                }
                case 2 -> { // Deposit cash
                    System.out.print("Enter client name: ");
                    String depositName = scanner.next();
                    System.out.print("Enter deposit amount: ");
                    int depositAmount;
                    try {
                        depositAmount = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid amount input. Please enter a number.");
                        scanner.next(); // Clear incorrect input
                        continue;
                    }
                    bank.addClient(new Client(depositName, "deposit", depositAmount));
                    bank.processClients();
                }
                case 3 -> {
                    System.out.println("Cash in register: " + bank.getCashInRegister());
                    System.out.println("Cash in storage: " + bank.getCashInStorage());
                }
                default -> System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}