package beatbeat.board;

import beatbeat.board.entities.*;

import java.util.Random;

public class GameBoard {
    private Field[][] board;
    private int boardHeight;
    private int boardWidth;
    private Enemy[] enemies;
    private Player player;
    private Goal goal;
    private class MovementResult {
        boolean canMove;
        MovementOptions movementOptions;
        MovementResult(boolean canMove, MovementOptions movementOptions) {
            this.canMove = canMove;
            this.movementOptions = movementOptions;
        }
    }
    public GameBoard(int height, int width, int numOfEnemies) {
        this.boardWidth = width;
        this.boardHeight = height;
        this.board = new Field[height][width];
        this.enemies = new Enemy[numOfEnemies];



    }

    private void setupBoard() {
        for(int i = 0; i < boardHeight; i++) {
            for(int j = 0; j < boardWidth; j++) {
                board[i][j] = new Field(j, i);
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
        if( board[enemyY][enemyX].isEntityOnField() &&
            !(board[enemyY][enemyX].getEntity() instanceof Player) &&
            !(board[enemyY][enemyX].getEntity() instanceof Enemy) &&
            !(board[enemyY][enemyX] instanceof Goal)
        ) {
            Enemy newEnemy;
            newEnemy = new Enemy(enemyX, enemyY,rng.nextBoolean());
            enemies[enemies.length - numOfEnemies] = newEnemy;
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

    private void moveEntity(Entity toMove, Field moveTo) {
        board[toMove.getPosY()][toMove.getPosX()].removeEntity();
        moveTo.placeEntity(toMove);
        updateEntityPosition(toMove, moveTo.getPosX(), moveTo.getPosY());
    }

    private void updateEntityPosition(Entity toMove, int newPosX, int newPosY) {
        toMove.setPosY(newPosY);
        toMove.setPosX(newPosX);
    }

    public void movePlayer(char direction) {
        switch(direction) {
            case 'u' -> {
                handlePlayerMove(player.getPosX(), player.getPosY() + 1);
            }
            case 'd' -> {
                handlePlayerMove(player.getPosX(), player.getPosY() - 1);
            }
            case 'l' -> {
                handlePlayerMove(player.getPosX() - 1, player.getPosY());
            }
            case 'r' -> {
                handlePlayerMove(player.getPosX() + 1, player.getPosY());
            }
        }
    }

    private void handlePlayerMove(int moveX, int moveY) {
        MovementResult canMove = canMove(player, board[player.getPosY() + moveY][player.getPosX() + moveX]);
        if(canMove.canMove) {
            switch (canMove.movementOptions) {
                case MovementOptions.PLAYER_TO_FIELD -> moveEntity(player, board[player.getPosY() + moveY][player.getPosX() + moveX]);
                case MovementOptions.PLAYER_TO_GOAL -> {
                    moveEntity(player, board[player.getPosY() + moveY][player.getPosX() + moveX]);
                    finishGame();
                }
            }
            moveEntity(player, board[moveY][moveX]);
            if(isEnemyNear()) {
                player.decreaseHealth();
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
                    if(board[newColIndex][newRowIndex].isEntityOnField() && board[newColIndex][newRowIndex].getEntity() instanceof Enemy) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void finishGame() {
    }

    private MovementResult canMove(Entity toMove, Field moveTo) {
        if(moveTo.getPosX() >= 0 && moveTo.getPosX() < boardWidth && moveTo.getPosY() >= 0 && moveTo.getPosY() < boardHeight) {
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

    /*


    public void onTick(int boundaryX, int boundaryY) {
        if (vertical) {
            if (getPosY() < boundaryY) {
                moveEntity(getPosX(), getPosY() + 1);
            } else if (getPosY() > 0) {
                moveEntity(getPosX(), getPosY() - 1);
            }
        } else {
            if (getPosX() < boundaryX) {
                moveEntity(getPosX() + 1, getPosY());
            } else if (getPosY() > 0) {
                moveEntity(getPosX() - 1, getPosY());
            }
        }
    }
     */
}

