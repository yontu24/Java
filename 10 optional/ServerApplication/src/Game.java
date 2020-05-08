public class Game {
    private Board board;
    Player player1;
    Player player2;
    private boolean turn = true;

    public Game(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public synchronized int addPiece(Player currentPlayer, String request) {

        if (currentPlayer.getTurn() != turn)
            return 2;

        String[] exploded;
        int x, y;
        exploded = request.substring(5).split(" ");
        x = Integer.parseInt(exploded[0]);
        y = Integer.parseInt(exploded[1]);

        if (x > Board.getDimension() || x < 0 || y > Board.getDimension() || y < 0)
            return -1;

        if (board.isFilled(x, y) || !board.isBoardFull())
            return 0;

        board.addToLocation(x, y, turn ? 'W' : 'B');

        if (board.isWinner(turn && !currentPlayer.getTurn() ? 'W' : 'B', x, y))
            return 3;

        turn = !turn;
        notifyAll();
        return 1;
    }

    /*
    public void notifyAllPlayers(String message) {
        for (Player player : game.getPlayers()) {
            player.out.println(message);
            player.out.flush();
        }
    }
    */
}