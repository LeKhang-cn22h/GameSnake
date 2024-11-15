package model;

import java.time.LocalDate;

public class Rank {
    private int rank;
    private String username;
    private int score;
    private LocalDate date;

    public Rank(int rank, String username, int score, LocalDate date) {
        this.rank = rank;
        this.username = username;
        this.score = score;
        this.date = date;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
