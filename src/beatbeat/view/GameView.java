package beatbeat.view;

import beatbeat.board.entities.Enemy;
import beatbeat.board.entities.Player;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private final int boardHeight;
    private final int boardWidth;
    private JPanel[][] gameBoardPanels;

    public GameView(int height, int width) {
        this.boardHeight = height;
        this.boardWidth = width;
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
    }

    public void updateView(Player player, Enemy[] enemies) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                gameBoardPanels[i][j].setBackground(Color.WHITE);
            }
        }

        switch(player.getHealth()) {
            case MAX_HEALTH -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.GREEN);
            case HALF_HEALTH -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.YELLOW);
            case LOW_HEALTH -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.RED);
            case DEAD -> gameBoardPanels[player.getPosY()][player.getPosX()].setBackground(Color.DARK_GRAY);
        }

        for(Enemy enemy: enemies) {
            gameBoardPanels[enemy.getPosY()][player.getPosX()].setBackground(Color.MAGENTA);
        }

        revalidate();
        repaint();
    }
}
