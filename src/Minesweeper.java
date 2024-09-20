import java.util.Random;

public class Minesweeper {

    // Data members
    private char[][] board;   // The game board where cells will be displayed
    private boolean[][] mines; // Array to track the locations of mines
    private boolean[][] revealed; // Array to track which cells have been revealed
    private int rows; // Number of rows in the board
    private int cols; // Number of columns in the board
    private int numMines; // Number of mines in the game
    private boolean gameOver; // Boolean to check if the game is over
    private int revealedCellsCount;

    // Constructor to initialize the board with the specified dimensions and number of mines
    public Minesweeper(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        this.board = new char[rows][cols];
        this.mines = new boolean[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.gameOver = false;
        this.revealedCellsCount = 0;

        initializeBoard();   // Initialize the board with default values
        placeMines();        // Place mines on the board
    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    public void setGameOver(boolean status) {
        this.gameOver = status;

    }

    // Method to initialize the game board with empty values
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '-';
                revealed[i][j] = false;
            }
        }
    }

    // Method to randomly place mines on the board
    private void placeMines() {
        Random rand = new Random();
        int placedMines = 0;

        while (placedMines < numMines) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);

            // Check if a mine is already placed here
            if (!mines[row][col]) {
                mines[row][col] = true;
                placedMines++;
            }
        }

        // After placing all mines, print the final number of placed mines
        System.out.println("Total mines placed: " + placedMines);
    }



    private int calculateNumbers(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && mines[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }




    // Method to display the current state of the board
    public void displayBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'F') {  // If the cell is flagged
                    System.out.print('F' + " ");  // Display the flag
                } else if (revealed[i][j]) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }


    // Method to handle a player's move (reveal a cell or place a flag)
    public void playerMove(int row, int col, String action) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            System.out.println("Invalid Move. Try Again");
            return;
        }

        // Handle the reveal action
        if (action.equalsIgnoreCase("reveal")) {
            if (revealed[row][col]) {
                System.out.println("Cell is already revealed.");
            } else if (mines[row][col]) {  // Game over only when revealing a mine
                System.out.println("Game over! You hit a mine.");
                setGameOver(true);
            } else {
                revealCell(row, col);  // Reveal the cell
            }

            // Handle the flag action
        } else if (action.equalsIgnoreCase("flag")) {
            if (board[row][col] == 'F') {  // If already flagged, remove the flag
                board[row][col] = '-';
                System.out.println("Flag removed.");
            } else if (!revealed[row][col]) {  // Place a flag if the cell is not revealed
                board[row][col] = 'F';
                System.out.println("Flag placed.");
            } else {
                System.out.println("Cannot place a flag on a revealed cell.");
            }
        } else {
            System.out.println("Invalid action. Use 'reveal' or 'flag'.");
        }
    }


    // Method to check if the player has won the game
    public boolean checkWin() {
        int totalNonMineCells = (rows * cols) - numMines;
        return revealedCellsCount == totalNonMineCells;
    }





    // Method to check if the player has lost the game
    public boolean checkLoss(int row, int col) {
        return mines[row][col];
    }

    // Method to reveal a cell (and adjacent cells if necessary)
    private void revealCell(int row, int col) {
        // Base case: If the cell is out of bounds, already revealed, or the game is over, stop
        if (row < 0 || col < 0 || row >= rows || col >= cols || revealed[row][col] || gameOver) {
            return;
        }

        // If the cell contains a mine, end the game
        if (mines[row][col]) {
            gameOver = true;  // Trigger game over
            return;  // No need to reveal further cells
        }

        // Mark the cell as revealed
        revealed[row][col] = true;
        revealedCellsCount++;  // Track the number of revealed cells, if needed for win condition

        // Calculate the number of adjacent mines
        int adjacentMines = calculateNumbers(row, col);
        board[row][col] = (char) (adjacentMines + '0');  // Display the number of adjacent mines

        // If the cell has adjacent mines, stop here (no recursion)
        if (adjacentMines > 0) {
            return;
        }

        // Otherwise, if the cell has 0 adjacent mines, recursively reveal neighboring cells
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Only recursively reveal valid neighboring cells
                if (!(i == 0 && j == 0)) {  // Don't check the current cell
                    revealCell(row + i, col + j);  // Recursively reveal neighbors
                }
            }
        }
    }








    private void flagCell(int row, int col) {
        if (!revealed[row][col]) {  // Only flag if the cell is not revealed
            board[row][col] = 'F';   // Update the board to show a flag
            System.out.println("Flag placed at: (" + row + ", " + col + ")");
        }
    }


    // Method to unflag a cell
    private void unflagCell(int row, int col) {
        if (board[row][col] == 'F') {
            board[row][col] = '-';
        }
    }

}