import java.util.*;

public class Board
{
    private static final int dimension = 10;
    private char[][] board = new char[dimension][dimension];

    public static int getDimension() {
        return dimension;
    }

    public boolean isBoardFull()
    {
        return Arrays.stream(board).allMatch(Objects::nonNull);
    }

    public String displayBoardToPlayer()
    {
        return Arrays.deepToString(board);
    }

    public boolean isWinner(char symbol, int x, int y)
    {
        /*
         * algoritm: verific intai coloana pornind din x (sus si jos)
         * verific apoi linia din y (sus si jos)
         * verific apoi diagonala principala
         * verific apoi diagonala secundara
         */

        final int win = 5;

        int posx = x;
        int counter = 0;

        /* coloana sus */
        while(posx > 0)
        {
            if(board[posx][y] == symbol) {
                posx--;
                counter++;
            }
            else break;
        }

        posx = x;
        /* coloana jos */
        while(posx < dimension)
        {
            if(board[posx][y] == symbol) {
                posx++;
                counter++;
            } else break;
        }

        if(counter == win)
            return true;

        /* linie dreapta */
        counter = 0;
        int posy = y;

        while(posy < dimension) {
            if (board[x][posy] == symbol) {
                posy++;
                counter++;
            } else break;
        }

        posy = y;
        /* linie stanga */
        while(posy > 0) {
            if (board[x][posy] == symbol) {
                posy--;
                counter++;
            } else break;
        }

        if(counter == win)
            return true;

        /* diagonala dreapta sus */
        counter = 0;
        posx = x;
        posy = y;

        while(posy < dimension && posx > 0) {
            if (board[posx][posy] == symbol) {
                posx--;
                posy++;
                counter++;
            } else break;
        }

        /* diagonala stanga jos */
        posx = x;
        posy = y;

        while(posx < dimension && posy > 0) {
            if (board[posx][posy] == symbol) {
                posx++;
                posy--;
                counter++;
            } else break;
        }

        if(counter == win)
            return true;

        /* diagonala stanga sus */
        counter = 0;
        posx = x;
        posy = y;

        while(posx > 0 && posy > 0) {
            if (board[posx][posy] == symbol) {
                posx--;
                posy--;
                counter++;
            } else break;
        }

        /* diagonala dreapta jos */
        posx = x;
        posy = y;

        while(posx < dimension && posy < dimension) {
            if (board[posx][posy] == symbol) {
                posx++;
                posy++;
                counter++;
            } else break;
        }

        return counter == win;
    }

    public synchronized void addToLocation(int x, int y, char value)
    {
        board[x][y] = value;
        notifyAll();
    }

    public boolean isFilled(int x, int y)
    {
        return board[x][y] != 0;
    }
}
