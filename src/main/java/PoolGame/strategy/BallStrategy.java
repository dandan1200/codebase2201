package PoolGame.strategy;

public class BallStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public BallStrategy() {
        this.lives = 1;
    }

    public void reset() {
        this.lives = 1;
    }

    public PocketStrategy clone(){
        PocketStrategy clone = new BallStrategy();
        clone.setLives(this.lives);
        return clone;
    }
}
