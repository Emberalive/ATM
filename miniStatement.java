import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.*;
import javafx.scene.layout.GridPane;

class miniStatement
{
    int H = 600;
    int W = 300;
    
    TextField   statement;      //shows all of the exchanges for the account
    ScrollPane  scrollPane;     //scroll bar for the verticle height
    GridPane    grid;           //layout grid
    TilePane    menuPane;       //menu buttons at the top of the window
    
        public miniStatement()
    {
        Debug.trace("View::<constructor>");
    }
    
    
    public void start(Stage miniStatement){
            Debug.trace("miniStatement::start");
            
            
            //These are the layout objects
            grid = new GridPane();
            grid.setId("statementLayout");
            menuPane = new TilePane();
            menuPane.setId("menu");
            
            
            //These will be the controls
            
    }
}
