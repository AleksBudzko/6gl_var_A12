package Project_6GL;

import java.util.Scanner;

//Интерфейс общий для всей техники
interface Technic{
boolean IsTurnedOn();

}
//Абстрактный класс для всех возможных плееров
abstract class Player implements Technic{

}

//Класс видеоплеера
class VideoPlayer extends Player{
    boolean TurnedOn;
    private final String name;
    public VideoPlayer(String name){
        this.name=name;
    }

@Override
public boolean IsTurnedOn(){return TurnedOn;}

}

//Основной класс
public class Tech {
    public static void main(String[] args) {


    }
}
