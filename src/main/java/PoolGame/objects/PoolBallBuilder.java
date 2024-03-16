package PoolGame.objects;

import PoolGame.strategy.PocketStrategy;
import PoolGame.points.BallPointsStrategy;
import PoolGame.points.BlackPointsStrategy;
import PoolGame.points.BluePointsStrategy;
import PoolGame.points.BrownPointsStrategy;
import PoolGame.points.GreenPointsStrategy;
import PoolGame.points.OrangePointsStrategy;
import PoolGame.points.PointsStrategy;
import PoolGame.points.PurplePointsStrategy;
import PoolGame.points.RedPointsStrategy;
import PoolGame.points.YellowPointsStrategy;
import PoolGame.strategy.BallStrategy;
import PoolGame.strategy.BlackStrategy;
import PoolGame.strategy.BlueStrategy;

/** Builds pool balls. */
public class PoolBallBuilder implements BallBuilder {
    // Required Parameters
    private String colour;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private double mass;

    // Variable Parameters
    private boolean isCue = false;
    public PocketStrategy strategy;
    public PointsStrategy pointsStrategy;

    @Override
    public void setColour(String colour) {
        this.colour = colour;
    };

    @Override
    public void setxPos(double xPosition) {
        this.xPosition = xPosition;
    };

    @Override
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    };

    @Override
    public void setxVel(double xVelocity) {
        this.xVelocity = xVelocity;
    };

    @Override
    public void setyVel(double yVelocity) {
        this.yVelocity = yVelocity;
    };

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    };

    /**
     * Builds the ball.
     * 
     * @return ball
     */
    public Ball build() {
        if (colour.equals("white")){
            isCue = true;
            strategy = new BallStrategy();
            pointsStrategy = new BallPointsStrategy();
        } else if (colour.equals("red")) {
            strategy = new BallStrategy();
            pointsStrategy = new RedPointsStrategy();
        } else if (colour.equals("yellow")) {
            strategy = new BallStrategy();
            pointsStrategy = new YellowPointsStrategy();
        } else if (colour.equals("green")) {
            strategy = new BlueStrategy();
            pointsStrategy = new GreenPointsStrategy();
        } else if (colour.equals("brown")) {
            strategy = new BlackStrategy();
            pointsStrategy = new BrownPointsStrategy();
        } else if (colour.equals("blue")) {
            strategy = new BlueStrategy();
            pointsStrategy = new BluePointsStrategy();
        } else if (colour.equals("purple")) {
            strategy = new BlueStrategy();
            pointsStrategy = new PurplePointsStrategy();
        } else if (colour.equals("black")) {
            strategy = new BlackStrategy();
            pointsStrategy = new BlackPointsStrategy();
        } else if (colour.equals("orange")) {
            strategy = new BallStrategy();
            pointsStrategy = new OrangePointsStrategy();
        } else {
            strategy = new BallStrategy();
            pointsStrategy = new BallPointsStrategy();
        }

        return new Ball(colour, xPosition, yPosition, xVelocity, yVelocity, mass, isCue, strategy, pointsStrategy);
    }
}
