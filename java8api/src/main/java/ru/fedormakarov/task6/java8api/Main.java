package ru.fedormakarov.task6.java8api;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {

        RacersSorter racersSorter = new RacersSorter();
        try {
            System.out.println(racersSorter.writeResult());
        } catch (IOException e) {

            e.printStackTrace();
        } catch (URISyntaxException e) {

            e.printStackTrace();
        }
    }

}
