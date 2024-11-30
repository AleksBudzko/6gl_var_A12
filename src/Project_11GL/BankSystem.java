package Project_11GL;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
