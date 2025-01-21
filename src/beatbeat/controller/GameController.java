package beatbeat.controller;

import beatbeat.board.GameBoard;
import beatbeat.view.GameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements GameEvent {
    private GameBoard board;
    private GameView view;

    public GameController(GameBoard board, GameView view) {
        this.board = board;
        this.view = view;

        view.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        //updateView();
    }

    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            //case KeyEvent.VK_UP -> board.
        }
    }

    @Override
    public void onGameFinished() {
        
    }
}
