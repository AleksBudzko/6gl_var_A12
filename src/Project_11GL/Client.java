package Project_11GL;

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
