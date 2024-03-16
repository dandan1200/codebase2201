package PoolGame.memento;

import java.util.ArrayList;

import PoolGame.objects.Ball;

public class GameStateMemento {
    private ArrayList<Ball> balls;
    private int minutes;
    private int seconds;
    private int tick;
    private int score;

    public GameStateMemento(ArrayList<Ball> balls, int score, int minutes, int seconds, int tick) {
        this.balls = balls;
        this.score = score;
        this.minutes = minutes;
        this.seconds = seconds;
        this.tick = tick;
    }

    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getTick() {
        return tick;
    }

    public int getScore() {
        return this.score;
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
