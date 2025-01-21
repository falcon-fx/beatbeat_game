package beatbeat.view;

import beatbeat.board.entities.Enemy;
import beatbeat.board.entities.Field;
import beatbeat.board.entities.Player;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private int boardHeight;
    private int boardWidth;
    private int numOfEnemies;
    private JPanel[][] gameBoardPanels;

    public GameView(int height, int width, int numOfEnemies) {
        this.boardHeight = height;
        this.boardWidth = width;
        this.numOfEnemies = numOfEnemies;
        initializePanels();
    }

    private void initializePanels() {
        setTitle("BeatBeat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(boardHeight,boardWidth));
        setResizable(false);
        gameBoardPanels = new JPanel[boardHeight][boardWidth];
        for(int i = 0; i < boardHeight; i++) {
            for(int j = 0; j < boardWidth; j++) {
                gameBoardPanels[i][j] = new JPanel();
                gameBoardPanels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(gameBoardPanels[i][j]);
            }
        }
        pack();
        setVisible(true);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void updateView(Field[][] board) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if(!board[i][j].isEntityOnField()) {
                    gameBoardPanels[i][j].setBackground(Color.WHITE);
                } else if(board[i][j].getEntity() instanceof Player player) {
                    switch(player.getHealth()) {
                        case MAX_HEALTH -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.GREEN);
                        case HALF_HEALTH -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.YELLOW);
                        case LOW_HEALTH -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.RED);
                        case DEAD -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.DARK_GRAY);
                    }
                } else if(board[i][j].getEntity() instanceof Enemy currEnemy) {
                    gameBoardPanels[currEnemy.getPosY()][currEnemy.getPosX()].setBackground(Color.MAGENTA);
                }
            }
        }

        revalidate();
        repaint();
    }
}
