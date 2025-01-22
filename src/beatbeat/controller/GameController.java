package beatbeat.controller;

import beatbeat.board.GameBoard;
import beatbeat.view.GameView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController implements GameEvent {
    private GameBoard board;
    private GameView view;
    private final ScheduledExecutorService gameLoop;
    private boolean canMove;

    public GameController(GameBoard board, GameView view) {
        this.board = board;
        this.view = view;
        this.gameLoop = Executors.newScheduledThreadPool(1);
        board.registerGameEventListener(this);
        view.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        canMove = true;
        this.view.setGameOptions(board.getBoardHeight(), board.getBoardWidth());
        startGameLoop(2000);
    }

    private void handleKeyPress(int keyCode) {
        if(canMove) {
            switch (keyCode) {
                case KeyEvent.VK_UP -> board.movePlayer('u');
                case KeyEvent.VK_DOWN -> board.movePlayer('d');
                case KeyEvent.VK_LEFT -> board.movePlayer('l');
                case KeyEvent.VK_RIGHT -> board.movePlayer('r');
            }
        }
        canMove = false;
    }

    public void startGameLoop(int loopIntervalMillisec) {
        board.startGame(board.getBoardHeight(), board.getBoardWidth(), board.getNumOfEnemies());
        gameLoop.scheduleAtFixedRate(() -> {
            try {
                canMove = true;
                board.updateGame();
                view.updateView(board.getBoard());
                System.out.println("Moving");
            } catch(Exception e) {
                e.printStackTrace();
                stopGameLoop();
            }

        }, 0, loopIntervalMillisec, TimeUnit.MILLISECONDS);
    }

    private void updateGame() {
        stopGameLoop();
    }

    private void stopGameLoop() {
        gameLoop.shutdown();
    }

    @Override
    public void onGameFinished(boolean reachedFinish) {
        String message;
        if(reachedFinish) {
            message = "Congratulations! You won!";
        } else {
            message = "Game over! The enemies danced better than you.";
        }
        JOptionPane.showMessageDialog(view, message);
        System.exit(0);
    }
}
