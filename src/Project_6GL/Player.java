package Project_6GL;

// Abstract class for all possible players
abstract class Player implements Technic {
    private final String name;
    private final String type;

    public Player(String name) {
        this.name = name;
        this.type = this.getClass().getSimpleName();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    protected boolean turnedOn = false;

    @Override
    public boolean IsTurnedOn() {
        return turnedOn;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void ChangeCondition() {
        turnedOn = !turnedOn;
    }
}
