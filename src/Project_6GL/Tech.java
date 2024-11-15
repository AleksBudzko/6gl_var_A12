package Project_6GL;

//Интерфейс общий для всей техники
interface Technic{
    boolean IsTurnedOn();

}
//Абстрактный класс для всех возможных плееров
abstract class Player implements Technic{
    private final String name;
    private final String type;
    public Player(String name) {
        this.name = name;
        this.type = this.getClass().getSimpleName();
    }

    public String getName(){return name;}
    public String getType(){return type;}
    boolean TurnedOn;
    @Override
    public boolean IsTurnedOn(){return TurnedOn;}
    @Override
    public String toString() {
        return name;
    }

}

//Класс видеоплеера
class VideoPlayer extends Player {

public VideoPlayer(String name){
    super(name);
}

}

//Основной класс
public class Tech {
    public static void main(String[] args) {


    }
}
