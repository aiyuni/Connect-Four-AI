/**
 * 
 */
package connectFourAI;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**This creates each individual block of the 8x8 grid.
 * @author Perry
 *
 */
public class Tile extends StackPane {
    
    Text text = new Text();
    Circle circle = new Circle(25);
    
    public Tile(String contents) {

        if (contents == null) {
            circle.setFill(null);
        }
        else if (contents.equals(" X " )) {
            circle.setFill(Color.RED);
        }
        else if (contents.equals(" O ")) {
            circle.setFill(Color.BLACK);
        }

        Rectangle border = new Rectangle(60,60);
        border.setFill(Color.WHITE);
        border.setStroke(Color.BLACK);

        getChildren().addAll(border, circle);
        
    }
    
    /**
     * Unused for now. Doing this in the constructor instead.
     * @param string
     */
    public void fillCircle(String string) {
        if (string.equals(" X ")) {
            circle.setFill(Color.RED);
        }
        else {
            circle.setFill(Color.BLACK);
        }
    }
    
}
