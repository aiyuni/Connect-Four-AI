package connectFourAI;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    FlowPane root = new FlowPane();
    Button button = new Button("Play Connect Four against AI");
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        
        button.setOnAction((event) -> {
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
            
        root.getChildren().addAll(button);
        
        Scene scene = new Scene(root, 650, 650);
        primaryStage.setTitle("BotGame");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public static void main (String[] args) {
        launch(args);
    }

}
