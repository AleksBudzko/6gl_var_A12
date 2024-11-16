package Project_6GL;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

// Интерфейс общий для всей техники
interface Technic {
    boolean IsTurnedOn();
}

// Абстрактный класс для всех возможных плееров
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

    public void ChangeCondition() {
        turnedOn = !turnedOn;
    }
}

// Класс видеоплеера
class VideoPlayer extends Player {
    public VideoPlayer(String name) {
        super(name);
    }
}

// Основной класс
public class Tech {
    public static void main(String[] args) {
        VideoPlayer myVideoPlayer = new VideoPlayer("Sony");

        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, String> videos = new HashMap<>();
        videos.put(1, "Video with cats");
        videos.put(2, "Funny moments");
        videos.put(3, "Cybersport");

        while (true) {
            String[] options = {"Включить/выключить плеер", "Выбрать видео", "Информация о плеере", "Exit"};
            System.out.println("Выбирите действие:");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ": " + options[i]);
            }
            String input = scanner.next();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный выбор.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    myVideoPlayer.ChangeCondition();
                    String condition = myVideoPlayer.IsTurnedOn() ? "включен" : "выключен";
                    System.out.println("Видеоплеер " + condition);
                }
                case 2 -> {
                    System.out.println("Доступные видео:");
                    for (Map.Entry<Integer, String> entry : videos.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }

                    System.out.print("Выберите номер видео: ");
                    int videoChoice;
                    try {
                        videoChoice = Integer.parseInt(scanner.next());
                        if (videos.containsKey(videoChoice)) {
                            System.out.println("Вы выбрали: " + videos.get(videoChoice));
                        } else {
                            System.out.println("Некорректный выбор видео.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный ввод. Пожалуйста, введите номер.");
                    }
                }
                case 3 -> {
                    System.out.println("Вот информация о Видеоплеере:");
                    String condition = (myVideoPlayer.IsTurnedOn()) ? "включен" : "выключен";
                    System.out.println("Название:"+myVideoPlayer.getName()+"  Тип:" + myVideoPlayer.getType()+"  Состояние:"+ condition);
                }
                case 4 -> {
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return; // Завершаем выполнение программы
                }
                default -> System.out.println("Некорректный выбор.");
            }
        }
    }
}
