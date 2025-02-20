package beatbeat.board;

import beatbeat.board.entities.*;
import beatbeat.controller.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameBoard {
    private Field[][] board;
    private int boardHeight;
    private int boardWidth;
    private Enemy[] enemies;
    private Player player;
    private Goal goal;
    private List<GameEvent> gameEventListeners;
    private static class MovementResult {
        boolean canMove;
        MovementOptions movementOptions;
        MovementResult(boolean canMove, MovementOptions movementOptions) {
            this.canMove = canMove;
            this.movementOptions = movementOptions;
        }
    }
    public GameBoard(int height, int width, int numOfEnemies) {
        gameEventListeners = new ArrayList<>();
        startGame(height, width, numOfEnemies);
    }

    public void startGame(int newHeight, int newWidth, int newEnemyCount) {
        setupBoard(newHeight, newWidth, newEnemyCount);
    }

    public Field[][] getBoard() {
        return Objects.requireNonNullElseGet(this.board, () -> new Field[0][0]);
    }

    public void registerGameEventListener(GameEvent newListener) {
        this.gameEventListeners.add(newListener);
    }

    public void deregisterGameEventListener(GameEvent listener) {
        this.gameEventListeners.remove(listener);
    }

    public void updateGame() {
        moveEnemies();
        if(player.getHealth() == HealthState.DEAD) {
            finishGame(false);
        }
        if(isEnemyNear()) {
            player.decreaseHealth();
        }
    }

    private void setupBoard(int height, int width, int numOfEnemies) {
        this.boardWidth = width;
        this.boardHeight = height;
        this.board = new Field[boardHeight][boardWidth];
        this.enemies = new Enemy[numOfEnemies];
        this.goal = new Goal(boardWidth - 1, 0);
        for(int i = 0; i < boardHeight; i++) {
            for(int j = 0; j < boardWidth; j++) {
                if(i == goal.getPosY() && j == goal.getPosX()) {
                    board[i][j] = goal;
                } else {
                    board[i][j] = new Field(j, i);
                }
            }
        }
        this.player = new Player(0, boardHeight - 1);
        this.board[player.getPosY()][player.getPosX()].placeEntity(player);
        genEnemies(enemies.length);
    }

    private void genEnemies(int numOfEnemies) {
        if(numOfEnemies == 0) {
            return;
        }
        Random rng = new Random();
        int enemyX = rng.nextInt(0, boardWidth);
        int enemyY = rng.nextInt(0, boardHeight);
        //System.out.println("lmao" + enemyX + "," + enemyY);
        if( !board[enemyY][enemyX].isEntityOnField() &&
            !(board[enemyY][enemyX] instanceof Goal)
        ) {
            //System.out.println("lol" + board[enemyY][enemyX].getEntity() + enemyX + enemyY);
            Enemy newEnemy;
            newEnemy = new Enemy(enemyX, enemyY,rng.nextBoolean());
            enemies[enemies.length - numOfEnemies] = newEnemy;
            board[enemyY][enemyX].placeEntity(newEnemy);
            genEnemies(--numOfEnemies);
        } else {
            genEnemies(numOfEnemies);
        }
    }

    public int getBoardWidth() {
        if(board != null && board[0] != null) {
            return this.boardWidth;
        } else {
            return 0;
        }
    }

    public int getBoardHeight() {
        if(board != null) {
            return this.boardHeight;
        } else {
            return 0;
        }
    }

    public int getNumOfEnemies() {
        if(enemies != null) {
            return this.enemies.length;
        } else {
            return 0;
        }
    }

    private void moveEntity(Entity toMove, Field moveTo) {
        board[toMove.getPosY()][toMove.getPosX()].removeEntity();
        System.out.println("Board: removed entity from board, pos: " + toMove.getPosY() + "," + toMove.getPosX() + ", is it there? " + board[toMove.getPosY()][toMove.getPosX()].isEntityOnField());
        moveTo.placeEntity(toMove);
        System.out.println("Board: moved entity to " + moveTo.getPosY() + "," + moveTo.getPosX() + "is it there? " + board[moveTo.getPosY()][moveTo.getPosX()].isEntityOnField());
        updateEntityPosition(toMove, moveTo.getPosX(), moveTo.getPosY());
    }

    private void updateEntityPosition(Entity toMove, int newPosX, int newPosY) {
        toMove.setPosY(newPosY);
        toMove.setPosX(newPosX);
    }

    public void moveEnemies() {
        for(Enemy enemy: enemies) {
            handleEnemyMove(enemy);
        }
    }

    private void handleEnemyMove(Enemy enemy) {
        int newPosY = enemy.isVertical() ? (enemy.isDirectionSwitched() ? (enemy.getPosY() + 1) : (enemy.getPosY() - 1)) : enemy.getPosY();
        int newPosX = enemy.isVertical() ? enemy.getPosX() : (enemy.isDirectionSwitched() ? (enemy.getPosX() - 1) : (enemy.getPosX() + 1));
        System.out.println("Enemy move pre: oldpos: " + enemy.getPosX() + "," + enemy.getPosY() + " newPosX: " + newPosX + "newPosY: " + newPosY);
        MovementResult canMove = canMove(enemy, newPosY, newPosX);
        System.out.println("Enemy move: oldpos: " + enemy.getPosX() + "," + enemy.getPosY() + " newPosX: " + newPosX + "newPosY: " + newPosY + "canmove: " + canMove.canMove + ", options: " + canMove.movementOptions);
        if(!canMove.canMove && (canMove.movementOptions == MovementOptions.ENEMY_TO_ENTITY || canMove.movementOptions == MovementOptions.OUT_OF_BOUNDS)) {
            System.out.println("Enemy move: cant move, switching direction");
            enemy.switchDirection();
        } else if(canMove.canMove && canMove.movementOptions == MovementOptions.ENEMY_TO_FIELD) {

            moveEntity(enemy, board[newPosY][newPosX]);
        }
    }

    public void movePlayer(char direction) {
        switch(direction) {
            case 'u' -> {
                handlePlayerMove(0, - 1);
            }
            case 'd' -> {
                handlePlayerMove(0, 1);
            }
            case 'l' -> {
                handlePlayerMove(- 1, 0);
            }
            case 'r' -> {
                handlePlayerMove(1, 0);
            }
        }
    }

    private void handlePlayerMove(int moveX, int moveY) {
        MovementResult canMove = canMove(player, (player.getPosY() + moveY), (player.getPosX() + moveX));
        if(canMove.canMove) {
            switch (canMove.movementOptions) {
                case MovementOptions.PLAYER_TO_FIELD -> moveEntity(player, board[player.getPosY() + moveY][player.getPosX() + moveX]);
                case MovementOptions.PLAYER_TO_GOAL -> {
                    moveEntity(player, board[player.getPosY() + moveY][player.getPosX() + moveX]);
                    finishGame(true);
                }
            }
        }
    }

    private boolean isEnemyNear() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int newRowIndex = player.getPosY() + i;
                int newColIndex = player.getPosX() + j;
                
                if((newRowIndex >= 0 && newRowIndex < boardHeight) && (newColIndex >= 0 && newColIndex < boardWidth)) {
                    if(board[newRowIndex][newColIndex].isEntityOnField() && board[newRowIndex][newColIndex].getEntity() instanceof Enemy) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void finishGame(boolean reachedFinish) {
        for (GameEvent listener : gameEventListeners) {
            listener.onGameFinished(reachedFinish);
        }
    }

    private MovementResult canMove(Entity toMove, int moveToY, int moveToX) {
        if(moveToX >= 0 && moveToX < boardWidth && moveToY >= 0 && moveToY < boardHeight) {
            System.out.println("canMove: " + moveToX + "," + moveToY);
            Field moveTo = board[moveToY][moveToX];
            if(toMove instanceof Player) {
                if(moveTo instanceof Goal goalToMoveTo) {
                    if(goalToMoveTo.isEntityOnField()) {
                        return new MovementResult(false, MovementOptions.PLAYER_TO_ENEMY); // Only enemy can be on goal if not player
                    } else {
                        return new MovementResult(true, MovementOptions.PLAYER_TO_GOAL);
                    }
                } else if(moveTo.isEntityOnField()) {
                    return new MovementResult(false, MovementOptions.PLAYER_TO_ENEMY); // Only enemy can be on field if not player
                }
                return new MovementResult(true, MovementOptions.PLAYER_TO_FIELD);
            } else if(toMove instanceof Enemy) {
                if((moveTo instanceof Goal goalToMoveTo && goalToMoveTo.isEntityOnField()) || moveTo.isEntityOnField()) {
                    return new MovementResult(false, MovementOptions.ENEMY_TO_ENTITY);
                }
                return new MovementResult(true, MovementOptions.ENEMY_TO_FIELD);
            } else {
                return new MovementResult(false, MovementOptions.OUT_OF_BOUNDS);
            }
        } else {
            return new MovementResult(false, MovementOptions.OUT_OF_BOUNDS);
        }
    }
}

