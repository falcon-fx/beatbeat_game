package beatbeat.view;

import beatbeat.board.entities.Enemy;
import beatbeat.board.entities.Field;
import beatbeat.board.entities.Goal;
import beatbeat.board.entities.Player;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private int boardHeight;
    private int boardWidth;
    private JPanel[][] gameBoardPanels;

    public GameView() {
    }

    public void setGameOptions(int boardHeight, int boardWidth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
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
                System.out.println("View: goal is found here? "+ i + " " + j + ", " + (board[i][j] instanceof Goal));
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
                if(board[i][j] instanceof Goal) {
                    gameBoardPanels[i][j].setBackground(Color.CYAN);
                }
            }
        }

        revalidate();
        repaint();
    }
}
