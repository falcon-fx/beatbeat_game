package beatbeat.board.entities;

public class Enemy extends Entity {
    private boolean vertical;
    private boolean directionSwitched;
    private int movementBoundary;

    public Enemy(int initialPosX, int initialPosY, boolean isVertical) {
        super(initialPosX, initialPosY);
        directionSwitched = false;
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

    public boolean isDirectionSwitched() {
        return directionSwitched;
    }

    public void switchDirection() {
        directionSwitched = !directionSwitched;
    }
}
