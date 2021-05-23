package ru.fedormakarov.task6.java8api;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {
        RacerService racersSorter = new RacerService();
        String abbrevioationFileName = "abbreviations.txt";
        String startFileName = "start.log";
        String endFileName = "end.log";

        try {
            System.out.println(racersSorter.showTableRacers(abbrevioationFileName, startFileName, endFileName));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
