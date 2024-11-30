package Project_10GL;

class ParkingLot {
    private final boolean[] parkingSpaces; // Array of parking spaces (true = occupied, false = free)

    public ParkingLot(int size) {
        parkingSpaces = new boolean[size]; // Initialize the parking lot with the given number of spaces
    }

    // Method for parking a car
    public boolean parkCar(int preferredSpot) {
        // Check that the spot starts at 1, convert to array index
        int index = preferredSpot - 1;
        System.out.println("Car is trying to park at spot " + preferredSpot);

        // Check spaces from the given spot to the end
        for (int i = index; i < parkingSpaces.length; i++) {
            if (!parkingSpaces[i]) {
                parkingSpaces[i] = true; // Occupy the spot
                System.out.println("Car parked at spot " + (i + 1));
                return true;
            }
        }

        System.out.println("No available parking spots!");
        return false; // No free spots
    }

    // Method for leaving the parking spot
    public boolean leaveParking(int spot) {
        // Check that the spot starts at 1, convert to array index
        int index = spot - 1;
        if (index >= 0 && index < parkingSpaces.length && parkingSpaces[index]) {
            parkingSpaces[index] = false; // Free up the spot
            System.out.println("Car left spot " + spot);
            return true;
        }

        System.out.println("Error: Spot " + spot + " is already free or invalid.");
        return false; // Invalid spot or already free
    }

    // Print the state of the parking lot
    public void printParkingState() {
        System.out.print("Parking lot state: ");
        for (boolean parkingSpace : parkingSpaces) {
            System.out.print((parkingSpace ? "[X]" : "[ ]") + " "); // [X] occupied, [ ] free
        }
        System.out.println();
    }
}
