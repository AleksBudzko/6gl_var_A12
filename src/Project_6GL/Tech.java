package Project_6GL;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

// Interface common for all devices
interface Technic {
    boolean IsTurnedOn();
    void ChangeCondition();
}

// Abstract class for all possible players
abstract class Player implements Technic {
    private final String name;
    private final String type;

    public Player(String name) {
        this.name = name;
        this.type = this.getClass().getSimpleName();
    }

    public String getName() { return name; }
    public String getType() { return type; }
    protected boolean turnedOn = false;

    @Override
    public boolean IsTurnedOn() { return turnedOn; }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void ChangeCondition() {
        turnedOn = !turnedOn;
    }
}

// Video player class
class VideoPlayer extends Player {
    public VideoPlayer(String name) {
        super(name);
    }
}

// Main class
public class Tech {
    public static void main(String[] args) {
        VideoPlayer myVideoPlayer = new VideoPlayer("Sony");

        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, String> videos = new HashMap<>();
        videos.put(1, "Video with cats");
        videos.put(2, "Funny moments");
        videos.put(3, "Cybersport");

        while (true) {
            String[] options = {"Turn on/off the player", "Select a video", "Player information", "Exit"};
            System.out.println("Choose an action:");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ": " + options[i]);
            }
            String input = scanner.next();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    myVideoPlayer.ChangeCondition();
                    String condition = myVideoPlayer.IsTurnedOn() ? "on" : "off";
                    System.out.println("The video player is " + condition);
                }
                case 2 -> {
                    System.out.println("Available videos:");
                    for (Map.Entry<Integer, String> entry : videos.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }

                    System.out.print("Select the video number: ");
                    int videoChoice;
                    try {
                        videoChoice = Integer.parseInt(scanner.next());
                        if (videos.containsKey(videoChoice)) {
                            System.out.println("You selected: " + videos.get(videoChoice));
                        } else {
                            System.out.println("Invalid video choice.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
                case 3 -> {
                    System.out.println("Here is the information about the video player:");
                    String condition = (myVideoPlayer.IsTurnedOn()) ? "on" : "off";
                    System.out.println("Name: " + myVideoPlayer.getName() + "  Type: " + myVideoPlayer.getType() + "  Condition: " + condition);
                }
                case 4 -> {
                    System.out.println("Exiting the program.");
                    scanner.close();
                    return; // End program execution
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}