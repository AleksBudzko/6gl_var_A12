package Project_11GL;

import java.util.ArrayList;
import java.util.List;

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
