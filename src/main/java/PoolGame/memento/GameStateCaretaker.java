package PoolGame.memento;

public class GameStateCaretaker {
    private GameStateMemento memento;
    
    public GameStateMemento getMemento() {
        return this.memento;
    }

    public void setMemento(GameStateMemento memento) {
        this.memento = memento;
    }
}
