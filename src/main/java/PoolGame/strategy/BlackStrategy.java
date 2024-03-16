package PoolGame.strategy;

public class BlackStrategy extends PocketStrategy {
    /** Creates a new black strategy. */
    public BlackStrategy() {
        this.lives = 3;
    }

    public void reset() {
        this.lives = 3;
    }
    public PocketStrategy clone(){
        PocketStrategy clone = new BlackStrategy();
        clone.setLives(this.lives);
        return clone;
    }
}
