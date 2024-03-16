package PoolGame;

public class GameTimer {
    private static GameTimer instance;

    private int seconds = 0;
    private int minutes = 0;
    private int tick = 0;
    
    private GameTimer() {

    }
    

    public static GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }

    public void tick() {
        tick++;
        if (tick == 120) {
            addSecond();
            tick = 0;
        }
    }

    private void addSecond() {
        seconds ++;
        if (seconds == 60) {
            seconds = 0;
            minutes ++;
        }
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

    public void reset(int s, int m, int t) {
        this.seconds = s;
        this.minutes = m;
        this.tick = t;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", minutes, seconds);
    }
}
