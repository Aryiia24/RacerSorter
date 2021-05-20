package ru.fedormakarov.task6.java8api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.w3c.dom.css.Counter;

public class RacerService {
    private static final String WHITESPACE = " ";
    private static final String TIME_PATTERN = "m:ss.SSS";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final String LINE_SEPARATOR = "\r\n";
    private static final char DASH = '-';

    public String showTableRacers(String abbreviationFileName, String startTimeFileName, String endTimeFileName)
            throws IOException, URISyntaxException {

        Path pathToAbbreviationsFile = Paths
                .get(this.getClass().getClassLoader().getResource(abbreviationFileName).toURI());
        List<Racer> sortedRacerList = createRacerList(pathToAbbreviationsFile, startTimeFileName, endTimeFileName)
                .stream().sorted().collect(Collectors.toList());

        Integer maxNameLength = sortedRacerList.stream().mapToInt(racer -> racer.getName().length()).max().getAsInt();
        Integer maxTeamNameLength = sortedRacerList.stream().mapToInt(racer -> racer.getTeam().length()).max()
                .getAsInt();

        StringBuilder result = new StringBuilder();
        Integer topRacersSeparatorNumber = 15;
        int rowCounter = 1;
        for (int i = 0; i < sortedRacerList.size(); i++) {

            result.append(viewForOneRacer(sortedRacerList.get(i), rowCounter++, topRacersSeparatorNumber, maxNameLength,
                    maxTeamNameLength));
        }

        return result.toString();
    }

    private String viewForOneRacer(Racer racer, int racerCount, Integer topRacersNumber, int maxNameLength,
            int maxTeamLength) {
        int numberForLineOffset = 10;
        StringBuilder result = new StringBuilder();
        String racerLapTime = TIME_FORMATTER.format(LocalTime.MIDNIGHT.plus(racer.getLapTime()));
        if (racerCount < numberForLineOffset) {
            return result
                    .append(String.format("%d. %-20s", racerCount, racer.getName())
                            + String.format("|%-26s|", racer.getTeam(), WHITESPACE) + racerLapTime + LINE_SEPARATOR)
                    .toString();
        }
        if (racerCount == topRacersNumber) {
            result.append(repeatChar(DASH, maxNameLength + maxTeamLength + 17) + LINE_SEPARATOR);
        }
        return result
                .append(String.format("%d. %-19s", racerCount, racer.getName())
                        + String.format("|%-26s|", racer.getTeam(), WHITESPACE) + racerLapTime + LINE_SEPARATOR)
                .toString();
    }

    private List<Racer> createRacerList(Path pathToAbbreviationsFile, String startTimeFileName, String endTimeFileName)
            throws IOException {
        int numbFirstWord = 0;
        int numbSecondWord = 1;
        int numbThirdWord = 2;

        return Files.lines(pathToAbbreviationsFile).map(line -> line.split("_")).map(s -> {
            return new Racer(s[numbFirstWord], s[numbSecondWord], s[numbThirdWord],
                    getLapTime(s[numbFirstWord], startTimeFileName, endTimeFileName));
        }).collect(Collectors.toList());

    }

    private Duration getLapTime(String racerAbbreviation, String startTimeFileName, String endTimeFileName) {

        try {
            Path pathStartFile = Paths.get(this.getClass().getClassLoader().getResource(startTimeFileName).toURI());
            Path pathEndFile = Paths.get(this.getClass().getClassLoader().getResource(endTimeFileName).toURI());

            String startTimeRightFormat = Files.lines(pathStartFile).filter(s -> s.contains(racerAbbreviation))
                    .map(s -> s.substring(3).replace("_", "T").concat("Z")).findAny().get();
            String endTimeRightFormat = Files.lines(pathEndFile).filter(s -> s.contains(racerAbbreviation))
                    .map(s -> s.substring(3).replace("_", "T").concat("Z")).findAny().get();

            Instant start = Instant.parse(startTimeRightFormat);
            Instant end = Instant.parse(endTimeRightFormat);
            return Duration.between(start, end);
        } catch (IOException | URISyntaxException e) {

            e.printStackTrace();
        }
        return null;
    }

    private String repeatChar(char inputCharacter, int quantity) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < quantity; i++) {
            stringBuilder.append(inputCharacter);
        }

        return stringBuilder.toString();
    }
}
