import java.util.Arrays;

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
        System.out.println(board[0].toString());
        for( int[] row: board ) {
            for (int num: row) {
                System.out.print(num);
            }
            System.out.println("");
        }
        GameView game = new GameView(4,5);

    }
}
