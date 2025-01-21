import java.util.Arrays;

import beatbeat.board.GameBoard;
import beatbeat.controller.GameController;
import beatbeat.view.GameView;
import utils.StringUtils;

public class Main {
    public static void main(String[] args) {
        int x = 10;
        int y = 2;
        int z;
        String sum;

        sum = Integer.toString(x + y);

        System.out.println(sum);
        System.out.println(StringUtils.isPalindrome(""));
        int[][] board;

        board = new int[5][6];
        System.out.println(board.length);
        System.out.println(board[0].length);
        System.out.println(Arrays.toString(board[0]));
        for( int[] row: board ) {
            for (int num: row) {
                System.out.print(num);
            }
            System.out.println("");
        }
        GameController newGame = new GameController(new GameBoard(10,15, 5), new GameView(10, 15, 5));

    }
}
