package ru.fedormakarov.task6.java8api;

import java.time.Duration;

public class Racer implements Comparable<Racer> {
    private String racerAbbreviation;
    private String name;
    private String team;
    private Duration lapTime;

    public Racer(String racerAbbreviation, String name, String team, Duration lapTime) {
        this.racerAbbreviation = racerAbbreviation;
        this.name = name;
        this.team = team;
        this.lapTime = lapTime;
    }

    public String getName() {
        return name;
    }

    public String getRacerAbbreviation() {
        return racerAbbreviation;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Duration getLapTime() {
        return lapTime;
    }

    public void setLapTime(Duration lapTime) {
        this.lapTime = lapTime;
    }

    @Override
    public int compareTo(Racer o) {
        return this.getLapTime().compareTo(o.getLapTime());

    }

}
