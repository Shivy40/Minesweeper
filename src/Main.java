import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Minesweeper game = new Minesweeper(10, 10, 10);
        Scanner scanner = new Scanner(System.in);

        while (!game.getGameOver()) {
            game.displayBoard();  // Correctly display the board after each action

            // Get player input for row, col, and action (reveal or flag)
            System.out.print("Enter row (0-9): ");
            int row = scanner.nextInt();
            System.out.print("Enter col (0-9): ");
            int col = scanner.nextInt();
            System.out.print("Enter action (reveal or flag): ");
            String action = scanner.next();

            game.playerMove(row, col, action);

            // Only check for a loss after a reveal, not after flagging
            if (action.equals("reveal")) {
                if (game.checkLoss(row, col)) {
                    System.out.println("Game Over! You hit a mine.");
                    game.setGameOver(true);
                }
            }

            // Check for win condition after either action
            if (game.checkWin()) {
                System.out.println("Congratulations! You've won the game.");
                break;
            }
        }


        scanner.close();
    }
}
