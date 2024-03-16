# Pool Game Builder

To run the application, please use:

gradle run

# Game Notes
- In order to hit the ball, click and hold onto the edge of the cue ball where you'd like to hit. 
- Then, drag your cursor away (in the angle you'd like to hit), and then release.
- The power of your hit will be based on the distance of the cue from the ball. 

# Config Notes
When entering config details, please note the following restrictions:
- Friction must be value between 0 - 1 (not inclusive). [Would reccomend switching between 0.95, 0.9, 0.85 to see changes].
- Ball X and Y positions must be within the size of the table width and length, including the ball radius (10).
- Ball colours must be Paint string values as expected.

# Extra controls
To choose the difficulty, use the keys '1', '2' or '3' to select the mode.

To undo the previous turn, use the 'z' key. All moves including cheat moves can be undone using 'z'. Undo cannot be done for difficulty change.

Cheat mapping:
To perform a cheat move click the below key to correspond to each colour ball:
- Blue = 'b'
- Red = 'r'
- Green = 'g'
- Purple = 'p'
- Yellow = 'y'
- Brown = 'w'
- Orange = 'o'
- Black = 'k'

Design patterns:
    Memento design pattern for undo:
        memento.GameStateCaretaker (Caretaker)
        memento.GameStateMemento (Memento)
        GameManager (Originator)

    Strategy design pattern for points:
        points.PointsStrategy (Strategy)

        Ball (Context)

        points.BallPointsStrategy (Concrete Strategy)
        points.BlackPointsStrategy (Concrete Strategy)
        points.BluePointsStrategy (Concrete Strategy)
        points.BrownPointsStrategy (Concrete Strategy)
        points.GreenPointsStrategy (Concrete Strategy)
        points.OrangePointsStrategy (Concrete Strategy)
        points.PurplePointsStrategy (Concrete Strategy)
        points.RedPointsStrategy (Concrete Strategy)
        points.YellowPointsStrategy (Concrete Strategy)

    Singleton design pattern for timer:
        GameTimer.java (Singleton)
        GameManager.java (Client)