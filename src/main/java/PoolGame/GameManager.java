package PoolGame;

import PoolGame.memento.GameStateCaretaker;
import PoolGame.memento.GameStateMemento;
import PoolGame.objects.*;
import java.util.ArrayList;

import javafx.geometry.Point2D;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import javafx.util.Duration;
import javafx.util.Pair;

/**
 * Controls the game interface; drawing objects, handling logic and collisions.
 */
public class GameManager {
    private Table table;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private Line cue;
    private boolean cueSet = false;
    private boolean cueActive = false;
    private boolean winFlag = false;
    private int score = 0;
    private GameTimer timer = GameTimer.getInstance();;

    private GameStateCaretaker lastState = new GameStateCaretaker();

    private final double TABLEBUFFER = Config.getTableBuffer();
    private final double TABLEEDGE = Config.getTableEdge();
    private final double FORCEFACTOR = 0.15;

    private Scene scene;
    private GraphicsContext gc;

    private boolean active = true;

    /**
     * Initialises timeline and cycle count.
     */
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Builds GameManager properties such as initialising pane, canvas,
     * graphicscontext, and setting events related to clicks.
     */
    public void buildManager() {
        Pane pane = new Pane();
        setClickEvents(pane);
        this.scene = new Scene(pane, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        setKeyEvents(scene);
        Canvas canvas = new Canvas(table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
        this.timer.reset(0,0,0);
        this.lastState.setMemento(this.saveState());
    }

    public void deactivate() {
        this.active = false;
    }

    /**
     * Draws all relevant items - table, cue, balls, pockets - onto Canvas.
     * Used Exercise 6 as reference.
     */
    private void draw() {
        tick();
        if (this.active) {
            timer.tick();
        }
        

        // Fill in background
        gc.setFill(Paint.valueOf("white"));
        gc.fillRect(0, 0, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);

        //Menu text
        gc.setStroke(Paint.valueOf("black"));
        gc.setFont(new Font("Arial", 16));
        gc.strokeText("Press '1' for easy, '2' for normal, '3' for hard, 'Z' for undo.", 15,20);

        //Timer
        gc.setStroke(Paint.valueOf("black"));
        gc.setFont(new Font("Arial", 16));
        gc.strokeText(this.timer.toString(), 650,20);

        //Score
        gc.setStroke(Paint.valueOf("black"));
        gc.setFont(new Font("Arial", 16));
        gc.strokeText("Score: " + this.score, 500,20);

        // Fill in edges
        gc.setFill(Paint.valueOf("brown"));
        gc.fillRect(TABLEBUFFER - TABLEEDGE, TABLEBUFFER - TABLEEDGE, table.getxLength() + TABLEEDGE * 2,
                table.getyLength() + TABLEEDGE * 2);

        // Fill in Table
        gc.setFill(table.getColour());
        gc.fillRect(TABLEBUFFER, TABLEBUFFER, table.getxLength(), table.getyLength());

        // Fill in Pockets
        for (Pocket pocket : table.getPockets()) {
            gc.setFill(Paint.valueOf("black"));
            gc.fillOval(pocket.getxPos() - pocket.getRadius(), pocket.getyPos() - pocket.getRadius(),
                    pocket.getRadius() * 2, pocket.getRadius() * 2);
        }

        // Cue
        if (this.cue != null && cueActive) {
            gc.setLineWidth(5);
            gc.strokeLine(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());
            gc.setLineWidth(1);
        }

        for (Ball ball : balls) {
            if (ball.isActive()) {
                gc.setFill(ball.getColour());
                gc.fillOval(ball.getxPos() - ball.getRadius(),
                        ball.getyPos() - ball.getRadius(),
                        ball.getRadius() * 2,
                        ball.getRadius() * 2);
            }

        }

        // Win
        if (winFlag) {
            gc.setStroke(Paint.valueOf("white"));
            gc.setFont(new Font("Impact", 80));
            gc.strokeText("Win and bye", table.getxLength() / 2 + TABLEBUFFER - 180,
                    table.getyLength() / 2 + TABLEBUFFER);
        }

    }

    /**
     * Updates positions of all balls, handles logic related to collisions.
     * Used Exercise 6 as reference.
     */
    public void tick() {
        int winScore = 0;
        for (Ball b : balls) {
            winScore += b.getPoints();
        }
        if (score == winScore) {
            winFlag = true;
        }

        for (Ball ball : balls) {
            ball.tick();

            if (ball.isCue() && cueSet) {
                hitBall(ball);
            }

            double width = table.getxLength();
            double height = table.getyLength();

            // Check if ball landed in pocket
            for (Pocket pocket : table.getPockets()) {
                if (pocket.isInPocket(ball)) {
                    if (ball.isCue()) {
                        this.reset();
                    } else {
                        if (ball.remove()) {
                            score+= ball.getPoints();
                        } else {
                            // Check if when ball is removed, any other balls are present in its space. (If
                            // another ball is present, blue ball is removed)
                            for (Ball otherBall : balls) {
                                double deltaX = ball.getxPos() - otherBall.getxPos();
                                double deltaY = ball.getyPos() - otherBall.getyPos();
                                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                                if (otherBall != ball && otherBall.isActive() && distance < 10) {
                                    ball.remove();
                                }
                            }
                        }
                    }
                    break;
                }
            }

            // Handle the edges (balls don't get a choice here)
            if (ball.getxPos() + ball.getRadius() > width + TABLEBUFFER) {
                ball.setxPos(width - ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getxPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setxPos(ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getyPos() + ball.getRadius() > height + TABLEBUFFER) {
                ball.setyPos(height - ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }
            if (ball.getyPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setyPos(ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }

            // Apply table friction
            double friction = table.getFriction();
            ball.setxVel(ball.getxVel() * friction);
            ball.setyVel(ball.getyVel() * friction);

            // Check ball collisions
            for (Ball ballB : balls) {
                if (checkCollision(ball, ballB)) {
                    Point2D ballPos = new Point2D(ball.getxPos(), ball.getyPos());
                    Point2D ballBPos = new Point2D(ballB.getxPos(), ballB.getyPos());
                    Point2D ballVel = new Point2D(ball.getxVel(), ball.getyVel());
                    Point2D ballBVel = new Point2D(ballB.getxVel(), ballB.getyVel());
                    Pair<Point2D, Point2D> changes = calculateCollision(ballPos, ballVel, ball.getMass(), ballBPos,
                            ballBVel, ballB.getMass(), false);
                    calculateChanges(changes, ball, ballB);
                }
            }
        }
    }

    /**
     * Resets the game.
     */
    public void reset() {
        for (Ball ball : balls) {
            ball.reset();
        }

        this.score = 0;
    }

    /**
     * @return scene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Sets the table of the game.
     * 
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Sets the balls of the game.
     * 
     * @param balls
     */
    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    /**
     * Hits the ball with the cue, distance of the cue indicates the strength of the
     * strike.
     * 
     * @param ball
     */
    private void hitBall(Ball ball) {
        // Check that start of cue is within cue ball
        
        // Collide ball with cue
        double hitxVel = (ball.getxPos() - cue.getStartX()) * FORCEFACTOR;
        double hityVel = (ball.getyPos() - cue.getStartY()) * FORCEFACTOR;
        ball.setxVel(hitxVel);
        ball.setyVel(hityVel);
        

        cueSet = false;

    }

    /**
     * Changes values of balls based on collision (if ball is null ignore it)
     * 
     * @param changes
     * @param ballA
     * @param ballB
     */
    private void calculateChanges(Pair<Point2D, Point2D> changes, Ball ballA, Ball ballB) {
        ballA.setxVel(changes.getKey().getX());
        ballA.setyVel(changes.getKey().getY());
        if (ballB != null) {
            ballB.setxVel(changes.getValue().getX());
            ballB.setyVel(changes.getValue().getY());
        }
    }

    public GameStateMemento getLastState() {
        return this.lastState.getMemento();
    }

    private void setKeyEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Z) {
                System.out.println("Undo");
                this.undo(this.lastState.getMemento());
            }
        });
    }

    /**
     * Sets the cue to be drawn on click, and manages cue actions
     * 
     * @param pane
     */
    private void setClickEvents(Pane pane) {
        pane.setOnMousePressed(event -> {

            for (Ball b : this.balls) {
                if (b.isCue()) {
                    if (event.getX() > b.getxPos() - b.getRadius() && event.getX() < b.getxPos() + b.getRadius() && event.getY() > b.getyPos() - b.getRadius() && event.getY() < b.getyPos() + b.getRadius()) {
                        double mouseToBallRatioSin = (b.getxPos() - event.getX())/Math.sqrt(Math.pow((b.getyPos() - event.getY()),2) + (Math.pow((b.getxPos() - event.getX()),2)));
                        double mouseToBallRatioCos = (b.getyPos() - event.getY())/Math.sqrt(Math.pow((b.getyPos() - event.getY()),2) + (Math.pow((b.getxPos() - event.getX()),2)));

                        int endx = (int) event.getX() - (int) Math.round(100*mouseToBallRatioSin);
                        int endy = (int) event.getY() - (int) Math.round(100*mouseToBallRatioCos);
                        
                        // double endx = event.getX() - (b.getxPos() - event.getX());
                        // double endy = event.getY() - (b.getyPos() - event.getY());


                        cue = new Line(event.getX(), event.getY(), endx, endy);
                        cueSet = false;
                        cueActive = true;
                    }
                    
                }
            }
            
        });

        pane.setOnMouseDragged(event -> {
            
            if (cueActive == true) {
                cue.setStartX(event.getX());
                cue.setStartY(event.getY());
                for (Ball b : this.balls) {
                    if (b.isCue()) {
                        double mouseToBallRatioSin = (b.getxPos() - event.getX())/Math.sqrt(Math.pow((b.getyPos() - event.getY()),2) + (Math.pow((b.getxPos() - event.getX()),2)));
                        int endx = (int) event.getX() - (int) Math.round(100*mouseToBallRatioSin);

                        double mouseToBallRatioCos = (b.getyPos() - event.getY())/Math.sqrt(Math.pow((b.getyPos() - event.getY()),2) + (Math.pow((b.getxPos() - event.getX()),2)));
                        int endy = (int) event.getY() - (int) Math.round(100*mouseToBallRatioCos);
                        
                        // double endx = event.getX() - (b.getxPos() - event.getX());
                        // double endy = event.getY() - (b.getyPos() - event.getY());

                        cue.setEndX(endx);
                        cue.setEndY(endy);
                    
                }
            }
            }
            
        });

        pane.setOnMouseReleased(event -> {
            
            if (cueActive == true) {
                cueSet = true;
                cueActive = false;
                this.lastState.setMemento(this.saveState());
            }
            
        });
    }


    /**
     * Checks if two balls are colliding.
     * Used Exercise 6 as reference.
     * 
     * @param ballA
     * @param ballB
     * @return true if colliding, false otherwise
     */
    private boolean checkCollision(Ball ballA, Ball ballB) {
        if (ballA == ballB) {
            return false;
        }

        return Math.abs(ballA.getxPos() - ballB.getxPos()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getyPos() - ballB.getyPos()) < ballA.getRadius() + ballB.getRadius();
    }

    /**
     * Collision function adapted from assignment, using physics algorithm:
     * http://www.gamasutra.com/view/feature/3015/pool_hall_lessons_fast_accurate_.php?page=3
     *
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA     The mass of ball A (for the moment this should always be the
     *                  same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB     The mass of ball B (for the moment this should always be the
     *                  same as ball A)
     *
     * @return A Pair in which the first (key) Point2D is the new
     *         delta x,y vector for ball A, and the second (value) Point2D is the
     *         new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA,
            Point2D positionB, Point2D velocityB, double massB, boolean isCue) {

        // Find the angle of the collision - basically where is ball B relative to ball
        // A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this
        // allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide
        // again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the
        // original velocities
        if (vB <= 0 && vA >= 0 && !isCue) {
            return new Pair<>(velocityA, velocityB);
        }

        // This is the optimisation function described in the gamasutra link. Rather
        // than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their
        // final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        return new Pair<>(velAPrime, velBPrime);
    }

    public GameStateMemento saveState(){
        ArrayList<Ball> cloneBalls = new ArrayList<Ball>();
        for (Ball b : this.balls) {
            if (b.isActive()) {
                cloneBalls.add(b.clone());
            }   
        }

        return new GameStateMemento(cloneBalls, score, this.timer.getMinutes(), this.timer.getSeconds(), this.timer.getTick());

    }

    public void undo(GameStateMemento memento) {
        this.balls = memento.getBalls();
        this.timer.reset(memento.getSeconds(), memento.getMinutes(), memento.getTick());
        this.score = memento.getScore();
    }

    public void cheat(Paint colour) {
        this.lastState.setMemento(this.saveState());
        for (Ball b : balls) {
            
            if (b.getColour().equals(colour) && b.isActive()) {
                b.deactivate();
                this.score += b.getPoints();
            }
        }
        
    }
}
