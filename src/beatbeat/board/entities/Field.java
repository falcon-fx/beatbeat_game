package beatbeat.board.entities;

public class Field extends Entity {
    private Entity entityOnField;
    private boolean isOnField;
    public Field(int initialPosX, int initialPosY) {
        super(initialPosX, initialPosY);
        removeEntity();
    }

    public void placeEntity(Entity toPlace) {
        if(!isOnField && entityOnField == null) {
            entityOnField = toPlace;
            isOnField = true;
        }
    }

    public void removeEntity() {
        if(entityOnField != null) {
            entityOnField = null;
        }
        isOnField = false;
    }

    public boolean isEntityOnField() {
        return isOnField;
    }

    public Entity getEntity() {
        return entityOnField;
    }
}
