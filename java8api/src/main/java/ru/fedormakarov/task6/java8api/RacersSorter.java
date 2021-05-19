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

public class RacersSorter {
    private static final String WHITESPACE = " ";
    private static final String TIME_PATTERN = "m:ss.SSS";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final String LINE_SEPARATOR = "\r\n";
    private static final char DASH = '-';

    public String writeResult() throws IOException, URISyntaxException {

        Path pathToAbbreviationsFile = Paths
                .get(this.getClass().getClassLoader().getResource("abbreviations.txt").toURI());
        List<Racer> sortedRacerList = createRacerList(pathToAbbreviationsFile).stream().sorted()
                .collect(Collectors.toList());

        Integer maxNameLength = sortedRacerList.stream().mapToInt(racer -> racer.getName().length()).max().getAsInt();
        Integer maxTeamNameLength = sortedRacerList.stream().mapToInt(racer -> racer.getTeam().length()).max()
                .getAsInt();

        StringBuilder result = new StringBuilder();
        IntSupplier rowCounter = new IntSupplier() {
            Integer counter = 1;

            @Override
            public int getAsInt() {
                return counter++;
            }
        };
        Integer topRacersSeparatorNumber = 15;
        sortedRacerList.stream().forEach(racer -> {

            result.append(viewForOneRacer(racer, rowCounter.getAsInt(), topRacersSeparatorNumber, maxNameLength,
                    maxTeamNameLength));

        });
        return result.toString();
    }

    private String viewForOneRacer(Racer racer, Integer racerCount, Integer topRacersNumber, int maxNameLength,
            int maxTeamLength) {

        StringBuilder result = new StringBuilder();
        String racerLapTime = TIME_FORMATTER.format(LocalTime.MIDNIGHT.plus(racer.getLapTime()));
        if (racerCount < 10) {
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

    private List<Racer> createRacerList(Path pathToAbbreviationsFile) throws IOException {
        return Files.lines(pathToAbbreviationsFile).map(line -> line.split("_")).map(s -> {
            return new Racer(s[0], s[1], s[2], getLapTime(s[0]));
        }).collect(Collectors.toList());

    }

    private Duration getLapTime(String racerAbbreviation) {

        try {
            Path pathStartFile = Paths.get(this.getClass().getClassLoader().getResource("start.log").toURI());
            Path pathEndFile = Paths.get(this.getClass().getClassLoader().getResource("end.log").toURI());
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
