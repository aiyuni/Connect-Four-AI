package connectFourAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;

public class Game implements Runnable {
    
    public static final int HEIGHT = 8;
    public static final int WIDTH = 9;
    public static final int DEPTH_lIMIT = 6;
    
    String[][] textBoard;
    Board GUIboard;
    boolean isFirst;  //if player is first player
    String playerText;
    String computerText;
    boolean gameOver = false;
    
    int depth = 0;
    int maxDepth = 0;
    int linesAnalyzed = 0;
    
    @Override
    public void run() {
        computerMove();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIboard.updateBoard(textBoard);
            }
        });
    }
    
    public Game(Board board, boolean isFirst) {
        
        GUIboard = board;
        this.isFirst = isFirst;
        
        if (isFirst) {
            playerText = " X ";
            computerText = " O ";
        }
        else {
            playerText = " O ";
            computerText = " X ";
        }
        
        textBoard = drawTextBoard();  //draw the starting text board
        printBoard(textBoard);
        
        GUIboard.drawEmptyBoard();  //draw the starting visual board
        
        if (!isFirst) {
            computerMove();
            GUIboard.updateBoard(getTextBoard());
            checkWin(textBoard, computerText);
            checkDraw(textBoard);
        }
    }
    
    public void computerMove() {
        
        int[] coordinateOfMove = new int[2];
        coordinateOfMove = findBestMove();
        textBoard[coordinateOfMove[0]][coordinateOfMove[1]] = computerText;
        
        System.out.println("Best Move is: " + coordinateOfMove[0] + ", " + coordinateOfMove[1]);
        
        printBoard(textBoard);
        
        if (checkWin(textBoard, computerText)) {
            gameOver = true;
            GUIboard.updateGameStatus("Computer has won.");
        }
        if (checkDraw(textBoard)) {
            gameOver = true;
            GUIboard.updateGameStatus("Game is drawn.");
        }
        
        System.out.println("Max Depth looked at for this move is :" + maxDepth);
        System.out.println("Lines Analyzed for this move is: " + linesAnalyzed);
        GUIboard.updateMaxDepth(maxDepth);
        GUIboard.updateAnalyzedLines(linesAnalyzed);
        maxDepth = 0;
        linesAnalyzed = 0;
        
    }
    
    public int[] findBestMove() {
        
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int[] bestMove = {0, 0};
        int bestVal = Integer.MIN_VALUE;
        int moveVal;
        
        List <int[]> bestMoves = new ArrayList(); 
        Random rand = new Random();
        
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (isMoveLegal(i, j, textBoard)) { 
                    
                    //make move
                    textBoard[i][j] = computerText;
                    
                    //evaluate move
                    moveVal = evaluateMove(textBoard, false, depth, alpha, beta); //human player is min
                    
                    //undo move
                    textBoard[i][j] = " - ";
                    
                    System.out.println("moveVal is: " + moveVal);
                    //if the move is the best move thus far, keep track of its val and coordinates
                    if (moveVal > bestVal) {
                        bestVal = moveVal;
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestMoves.clear(); //important to remove previous 'best' moves from array
                        bestMoves.add(bestMove);
                    }
                    
                    //If there are more than one more with the same evaluation score, add them to an array and random the move
                    else if (moveVal == bestVal) {
                        int[] currentBestMove = new int[2];
                        currentBestMove[0] = i;
                        currentBestMove[1] = j;
                        bestMoves.add(currentBestMove);
                    }
                }
            }
        }
        
        System.out.println("bestMoves size: " + bestMoves.size());
        if (bestMoves.size() > 1) {
            return bestMoves.get(rand.nextInt(bestMoves.size()-1));
        }
        else {
            return bestMove;
        }
    }
    
    public int evaluateMove(String[][] board, boolean isMax, int depth, int alpha, int beta) {
        
        int boardScore = 0 ;
        int best;
        
        if (maxDepth < depth) {
            maxDepth = depth;
        }       
        
        //Check for terminal states
        if (checkWin(board, playerText)) {
            linesAnalyzed++;
            boardScore = -100 + depth;
            return boardScore;
        }
        
        if (checkWin(board, computerText)) {
            linesAnalyzed++;
            boardScore = 100 - depth;
            return boardScore;
        }
 
        if (checkDraw(board)) {
            linesAnalyzed++;
            boardScore += 0;
            return boardScore;
        }
        
        boardScore = evaluateBoard(board);
        if (boardScore != 0) {
            if (isMax) {
                best = boardScore - depth;
            }
            else if (!isMax) {
                best = boardScore + depth;
            }
        }
        
        if (depth == DEPTH_lIMIT) {
            linesAnalyzed++;
            boardScore += 0; //if depth = predetermined value, return 0 (no evaluation on position)
            return boardScore;
        }
        
        //Computer is always the Max player (it wants to max its score)
        if (isMax) {
            //System.out.println("is MAX player");
            best =  Integer.MIN_VALUE;
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    if (isMoveLegal(i, j, textBoard)) {
                        
                        //make move
                        board[i][j] = computerText; 
                        
                        //call evaluateMove recursively
                        best = Math.max(best, evaluateMove(board, false, depth + 1, alpha, beta));
                        
                       //undo move
                        board[i][j] = " - ";
                        
                        alpha = Math.max(alpha, best);
                        if (beta <= alpha) {
                            //System.out.println("breaking");
                            break;
                        }
                    }
                }
            }
            linesAnalyzed++;
            return best;
        }
        
        //The human player is always the minimizer. 
        else {
            //System.out.println("is MIN player");
            best = Integer.MAX_VALUE;
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    if (isMoveLegal(i, j, textBoard)) {

                        board[i][j] = playerText;
                        best = Math.min(best, evaluateMove(board, true, depth + 1, alpha, beta));
                        
                        //undo move
                        board[i][j] = " - ";
                        
                        beta = Math.min(beta, best);
                        if (beta <= alpha) {
                           // System.out.println("breaking");
                            break;
                        }
                    }
                }
            }
            linesAnalyzed++;
            return best;
        }
        
    }
    
    //computer is always max player
    public int evaluateBoard(String[][] board) {
        
        int countMax = 0;
        int countMin = 0;
        
        //evaluate row.
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == computerText) {
                    countMax++;
                    countMin = 0;
                }
                else if (board[i][j]== playerText) {
                    countMin++;
                    countMax = 0;
                }
                else {
                    countMin = 0;
                    countMax = 0;
                }
                if (countMin == 3) {
                    return -51;
                }
                if (countMax == 3) {
                    return 50;
                }
            }
            countMax = 0;
            countMin = 0;
        }
        
        //evaluate column
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (board[j][i] == computerText) {
                    countMax++;
                    countMin = 0;
                }
                else if (board[j][i] == playerText) {
                    countMin++;
                    countMax = 0;
                }
                else {
                    countMin = 0;
                    countMax = 0;
                }
                
                if (countMin == 3) {
                    return -51;
                }
                if (countMax == 3) {
                    return 50;
                }
            }
            countMax = 0;
            countMin = 0;
        }
        
        return 0;
        
    }
    
    public void userMove(int i, int j) {
        
        textBoard[i][j] = playerText;
        printBoard(textBoard); //display board to console
        
        //check for win and draw after move is made
        if (checkWin(textBoard, playerText)) {
            gameOver = true;
            GUIboard.updateGameStatus("You have won.");
        }
        if (checkDraw(textBoard)) {
            gameOver = true;
            GUIboard.updateGameStatus("Game is drawn.");
        }
  
    }
    
    /**
     * Checks if the move is a legal move.
     * @param i x coordinate of move
     * @param j y coordinate of move
     * @param board the board to check on 
     * @return
     */
    public boolean isMoveLegal(int i, int j, String[][] board) {
        
        //if user clicked on a square that has been played already, dont play move
        if (board[i][j] != " - ") {
            return false; 
        }
        
        //if user clicked on first row, play move (remember, going down = positive)
        if (i == HEIGHT - 1) {
            return true;
        }
        
        //if the square directly below is not empty, play move
        else if (!(board[i + 1][j] == " - ")) {
            return true;
        }
        
        //otherwise, not legal move, don't play move
        else {
            return false;
        }
    }
    
    public boolean checkWin(String[][] board, String player) {
        
        int count = 0;
        String playerText = player;
        
        //Check for win by backwards diagonal, in a 5x6 (height x width) rectangle starting from the upper left
        for (int i = 0; i < (HEIGHT/2) + 1; i++) {
            for (int j = 0; j  < (WIDTH/2)+ 2; j++ ) {  
                if (board[i][j] == playerText && board[i+1][j+1] == playerText && board[i+2][j+2] == playerText && board[i+3][j+3] == playerText) {
                    System.out.println("win by backwards diagonal!");
                    return true;
                }
            }
        }
        
        //Check for win by forward diagonal, in a 5x6 (height x width) rectangle starting from the lower left
        for (int i = (HEIGHT/2) - 1; i < HEIGHT; i++) {
            for (int j = 0; j  < (WIDTH/2)+2; j++ ) {
               
                if (board[i][j] == playerText && board[i-1][j+1] == playerText && board[i-2][j+2] == playerText && board[i-3][j+3] == playerText) {
                    System.out.println("Win by forward diagonal.");
                    return true;
                    
                }
            }
        }
        
        //Check for win by row.
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == playerText) {
                    count++;
                }
                else {
                    count = 0;
                }
                if (count == 4) {
                    System.out.println("win by row!");
                    return true;
                }
            }
            count = 0;
        }
        
        //check for win by column.
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (board[j][i] == playerText) {
                    count++;
                }
                else {
                    count = 0;
                }
                if (count == 4) {
                    System.out.println("Win by column!");
                    System.out.println("last winning coordinate is: " + i + ", " + j);
                    return true;
                }
            }
            count = 0;
        } 
        return false;
    }
    
    public boolean checkDraw(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == " - ") {
                    return false;
                }
            }
        }
        System.out.println("Game is drawn.");
        return true;
    }
    
    public String[][] drawTextBoard() {
        String[][] textBoard = new String[HEIGHT][WIDTH];
        
        for(int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                textBoard[i][j] = " - ";
            }
        }
        return textBoard;
    }
    
    /**
     * Method that prints the board to console.
     * @param board  The board to print to console.
     */
    public void printBoard(String[][] board) {
        for (int i =0; i<board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Returns the gameOver status of the game.
     * @return true if game is over, false otherwise.
     */
    public boolean isOver() {
        return gameOver;
    }
    
    /**
     * Getter for the board.
     * @return textBoard, the current board of this object.
     */
    public String[][] getTextBoard(){
        return textBoard;
    }
    
    public String getPlayerText() {
        return playerText;
    }
    
    public String getComputerText() {
        return computerText;
    }


}
