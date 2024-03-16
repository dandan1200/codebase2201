package PoolGame.strategy;

public class BlueStrategy extends PocketStrategy {
    /** Creates a new blue strategy. */
    public BlueStrategy() {
        this.lives = 2;
    }

    public void reset() {
        this.lives = 2;
    }
    public PocketStrategy clone(){
        PocketStrategy clone = new BlueStrategy();
        clone.setLives(this.lives);
        return clone;
    }
}
