package beatbeat.board.entities;

public class Player extends Entity {
    public Player(int initialPosX, int initialPosY) {
        super(initialPosX, initialPosY);
    }

    public void receiveDamage() {
        decreaseHealth();
    }
}
