package Project_11GL;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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