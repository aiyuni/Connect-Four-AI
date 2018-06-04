
package connectFourAI;

import java.util.Optional;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Board {
    
    private BorderPane root = new BorderPane();
    private Game game;
    private VBox vbox = new VBox();
    private HBox titleHBox = new HBox();
    private GridPane grid;
    private HBox bottom = new HBox();
    
    
    private Text title = new Text("Play against AI");
    private Text gameOver = new Text("No winner yet.");
    private Text maxDepth = new Text("Max depth: ");
    private Text calculations = new Text("Lines analyzed: ");
    private Button newGame = new Button("New Game");
    
    public Board() {
        //nothing here
    }
    
    public void openWindow(Stage primaryStage, boolean isFirst) {
        
        grid = new GridPane(); //creates grid pane 
        
        game = new Game(this, isFirst);
        
        vbox.getChildren().addAll(maxDepth, calculations);
        vbox.setAlignment(Pos.CENTER_LEFT);
        root.setCenter(grid);
        titleHBox.getChildren().add(title);
        titleHBox.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(gameOver, newGame);
        root.setTop(titleHBox);
        root.setRight(vbox);
        root.setBottom(bottom);
        
        maxDepth.setFont(Font.font(20));
        calculations.setFont(Font.font(20));
        title.setFont(Font.font(30));
        gameOver.setFont(Font.font(20));
        gameOver.setTextAlignment(TextAlignment.CENTER);
        
        Scene scene = new Scene(root, 750, 600);
        primaryStage.setTitle("Connect Four " + isFirst);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        newGame.setOnAction((event) -> {
            System.out.println("hello");
            
            Board board = new Board();

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Pick your color");
            alert.setHeaderText("Pick your color");
            alert.setContentText("Go first or go second?");

            ButtonType buttonTypeOne = new ButtonType("Go first.");
            ButtonType buttonTypeTwo = new ButtonType("Go second.");
            
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            
            //this shows the alert, without needing "alert.show();"
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.get() == buttonTypeOne){
                board.openWindow(primaryStage, true);
            } else if (result.get() == buttonTypeTwo) {
                board.openWindow(primaryStage, false);
            }
        });
    }
    
    public void updateBoard(String[][] textBoard) {
        
        for (int i = 0; i < textBoard.length; i++) {
            int tempI = i;
            for (int j = 0; j < textBoard[i].length; j++) {
                int tempJ = j;
                if (!(textBoard[i][j] == " - ")) {
                    Tile tile = new Tile(textBoard[i][j]);
                    tile.setOnMouseClicked((event) -> {      
                        if (game.isMoveLegal(tempI, tempJ, game.getTextBoard()) && !game.isOver()) {
                            game.userMove(tempI, tempJ);
                            updateBoard(game.getTextBoard());
                            
                            Thread newThread = new Thread(game);
                            newThread.start();
                            
                        }
                    });
                    grid.add(tile, j, i);
                }
            }
        }
    }
    
    public void drawEmptyBoard() {
        for (int i = 0; i<8; i++) {
            int tempI = i;
            for (int j = 0; j<9; j++) {
                int tempJ = j;
                Tile tile = new Tile(null);
                tile.setOnMouseClicked((event) -> {
                    
                    if (game.isMoveLegal(tempI, tempJ, game.getTextBoard()) && !game.isOver()) {
                        game.userMove(tempI, tempJ);
                        updateBoard(game.getTextBoard());
                        
                        Thread newThread = new Thread(game);
                        newThread.start();
                    }                   
                });
                grid.add(tile, j, i);
            }
        }
    }
    
    public void updateMaxDepth(int maxDepth) {
        this.maxDepth.setText("Max depth: " + Integer.toString(maxDepth));
    }
    
    public void updateAnalyzedLines(int lines) {
        calculations.setText("Lines analyzed: " + Integer.toString(lines));
    }
    
    public void updateGameStatus(String text) {
        gameOver.setText(text);
    }
}
