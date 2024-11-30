package Project_11GL;

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
