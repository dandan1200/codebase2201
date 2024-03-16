package PoolGame;

import PoolGame.config.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/** Main application entry point. */
public class App extends Application {

    private String configPath = "src/main/resources/config_easy.json";
    private Stage primStage;
    private GameManager gameManager;

    /**
     * @param args First argument is the path to the config file
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**
     * Starts the application.
     * 
     * @param primaryStage The primary stage for the application.
     */
    public void start(Stage primaryStage) {
        // READ IN CONFIG
        this.primStage = primaryStage;
        this.gameManager = new GameManager();
        

        // List<String> args = getParameters().getRaw();
        // String configPath = checkConfig(args);

        ReaderFactory tableFactory = new TableReaderFactory();
        Reader tableReader = tableFactory.buildReader();
        tableReader.parse(configPath, gameManager);

        ReaderFactory ballFactory = new BallReaderFactory();
        Reader ballReader = ballFactory.buildReader();
        ballReader.parse(configPath, gameManager);
        gameManager.buildManager();

        // START GAME MANAGER
        gameManager.run();
        primaryStage.setTitle("Pool");
        primaryStage.setScene(gameManager.getScene());
        setKeyEvents(gameManager.getScene());
        primaryStage.show();

        gameManager.run();
    }

    /**
     * Checks if the config file path is given as an argument.
     * 
     * @param args
     * @return config path.
     */
    // private static String checkConfig(List<String> args) {
    //     String configPath;
    //     if (args.size() > 0) {
    //         configPath = args.get(0);
    //     } else {
    //         configPath = "src/main/resources/config_normal.json";
    //     }
    //     return configPath;
    // }

    private void setKeyEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DIGIT1) {
                //System.out.println("1 pressed");
                this.configPath = "src/main/resources/config_easy.json";
                this.gameManager.deactivate();
                this.start(primStage);
            } else if (event.getCode() == KeyCode.DIGIT2) {
                //System.out.println("2 pressed");
                this.configPath = "src/main/resources/config_normal.json";
                this.gameManager.deactivate();
                this.start(primStage);
            } else if (event.getCode() == KeyCode.DIGIT3) {
                //System.out.println("3 pressed");
                this.configPath = "src/main/resources/config_hard.json";
                this.gameManager.deactivate();
                this.start(primStage);
            } else if (event.getCode() == KeyCode.Z) {
                //System.out.println("Undo");
                gameManager.undo(gameManager.getLastState());
            } else if (event.getCode() == KeyCode.B) {
                gameManager.cheat(Paint.valueOf("blue"));
            } else if (event.getCode() == KeyCode.R) {
                gameManager.cheat(Paint.valueOf("red"));
            } else if (event.getCode() == KeyCode.G) {
                gameManager.cheat(Paint.valueOf("green"));
            } else if (event.getCode() == KeyCode.P) {
                gameManager.cheat(Paint.valueOf("purple"));
            } else if (event.getCode() == KeyCode.Y) {
                gameManager.cheat(Paint.valueOf("yellow"));
            } else if (event.getCode() == KeyCode.O) {
                gameManager.cheat(Paint.valueOf("orange"));
            } else if (event.getCode() == KeyCode.W) {
                gameManager.cheat(Paint.valueOf("brown"));
            } else if (event.getCode() == KeyCode.K) {
                gameManager.cheat(Paint.valueOf("black"));
            }
            
        });
    }
}
