package beatbeat.board.entities;

public class Entity {
    private int posX;
    private int posY;
    private HealthState health;

    public Entity(int initialPosX, int initialPosY) {
        setPosX(initialPosX);
        setPosY(initialPosY);
        setHealth(HealthState.MAX_HEALTH);
    }

    public HealthState getHealth() {
        return health;
    }

    void setHealth(HealthState health) {
        this.health = health;
    }
    
    public void increaseHealth() {
        switch (health) {
            case HealthState.HALF_HEALTH -> setHealth(HealthState.MAX_HEALTH);
            case HealthState.LOW_HEALTH -> setHealth(HealthState.HALF_HEALTH);
        }
    }

    public void decreaseHealth() {
        switch (health) {
            case HealthState.MAX_HEALTH -> setHealth(HealthState.HALF_HEALTH);
            case HealthState.HALF_HEALTH -> setHealth(HealthState.LOW_HEALTH);
            case HealthState.LOW_HEALTH -> setHealth(HealthState.DEAD);
        }
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
