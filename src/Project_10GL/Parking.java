package Project_10GL;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Parking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User sets the number of parking spaces
        int parkingSize;
        while (true) {
            System.out.print("Enter the number of parking spaces: ");
            try {
                parkingSize = scanner.nextInt();
                if (parkingSize > 0) break; // Check that a positive number is entered
                System.out.println("Invalid input. The number of spaces must be a positive number.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }

        ParkingLot parkingLot = new ParkingLot(parkingSize);

        // Create a HashMap for action selection
        HashMap<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, parkingLot::printParkingState);
        actions.put(2, () -> {
            System.out.print("Enter the spot where you want to park: ");
            try {
                int spot = scanner.nextInt();
                parkingLot.parkCar(spot);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        });
        actions.put(3, () -> {
            System.out.print("Enter the spot from which the car is leaving: ");
            try {
                int spot = scanner.nextInt();
                parkingLot.leaveParking(spot);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        });
        actions.put(0, () -> System.out.println("Exiting the program."));

        // Main menu
        while (true) {
            System.out.println("\nChoose an action:");
            System.out.println("1 - Show parking lot state");
            System.out.println("2 - Park a car");
            System.out.println("3 - Remove a car");
            System.out.println("0 - Exit");

            int choice;
            try {
                choice = scanner.nextInt();
                Runnable action = actions.get(choice);
                if (action != null) {
                    action.run();
                    if (choice == 0) {
                        break; // Exit the loop if the exit action is chosen
                    }
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear invalid input
            }
        }

        scanner.close();
    }
}