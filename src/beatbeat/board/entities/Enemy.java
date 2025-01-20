package beatbeat.board.entities;

public class Enemy extends Entity {
    private boolean vertical;
    private int movementBoundary;

    public Enemy(int initialPosX, int initialPosY, boolean isVertical) {
        super(initialPosX, initialPosY);
        setVertical(isVertical);
    }

    public void attackPlayer(Player player) {
        player.receiveDamage();
    }

    public boolean isVertical() {
        return vertical;
    }

    private void setVertical(boolean vertical) {
        this.vertical = vertical;
    }
}
