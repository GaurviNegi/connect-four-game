package game.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.scene.paint.Color.*;

public class Controller implements Initializable {
    public TextField pOne;
    public TextField pTwo;
    public Button setname;
    private boolean isAllowedToInsert = true;          // flag to avoid insertion of multiple disc by a single player..
    private static  final int ROWS = 6;
    private static  final int COLUMNS = 7;
    private static  final int CIRCLE_DIAMETER = 80;

     static String playerOne = "Player One";
     static String playerTwo = "Player Two";
    static boolean isPlayerOne = true;
    private  static final String disc_color1 = "#101820FF";
    private  static final String disc_color2 = "#AA336A";

    private Disc  DiscArray[][] = new Disc[ROWS][COLUMNS];
    @FXML
public GridPane rootGridPane;
public Label playerName;
public Pane discPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void createPlayground() {
        setname.setOnAction(event->setNamefunc());
       Shape rectangleWithHoles = createGridStructure();
      List<Rectangle> rectangleList = clickableRectangle();

       rootGridPane.add(rectangleWithHoles,0,1);
       for(Rectangle rectangle : rectangleList){ rootGridPane.add(rectangle,0,1);}


    }

    public void setNamefunc() {
        playerName.setText(pOne.getText());
        playerOne = pOne.getText();
        playerTwo = pTwo.getText();
    }

    private Shape createGridStructure(){
        Shape rectangleWithHoles = new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);
        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLUMNS;col++){
                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER/2);
                circle.setCenterX(CIRCLE_DIAMETER/2);
                circle.setCenterY(CIRCLE_DIAMETER/2);
                circle.setSmooth(true);
                circle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                rectangleWithHoles = Shape.subtract(rectangleWithHoles,circle);

            }
        }

        rectangleWithHoles.setFill(WHITE);
        return rectangleWithHoles;
    }

    private List<Rectangle> clickableRectangle(){
        List<Rectangle> rectangleList = new ArrayList<>();
        for(int col = 0; col< COLUMNS;col++){
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);
            rectangle.setFill(TRANSPARENT);
            rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

            rectangle.setOnMouseEntered(event -> rectangle.setFill(valueOf("#eeeeee30")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(TRANSPARENT));
            final int COL = col;

                rectangle.setOnMouseClicked(event -> {
                    if(isAllowedToInsert){
                isAllowedToInsert= false;
                insertDisc(new Disc(isPlayerOne), COL);}
            });


            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    private void insertDisc(Disc disc, int col) {
        int row =   ROWS-1;
        while(row>=0){
           if(getDiscCheck(row,col)==null){break;}
           row--;
        }
        if(row<0){return;}
        DiscArray[row][col]=disc;
        discPane.getChildren().add(disc);
        disc.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5),disc);
        tt.setToY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        int CurrentRow = row;           //lambda expression function use constant or final terms only
        tt.setOnFinished(event -> {
            isAllowedToInsert=true;          // to allow insertion of disc by next user
            if(gameEnded(CurrentRow,col)){
                System.out.println(gameEnded(CurrentRow,col));
                gameOver();
            }
            isPlayerOne = !isPlayerOne;
            playerName.setText(!isPlayerOne ? playerTwo : playerOne);
        });
        tt.play();



    }

    private void gameOver() {
        String winner = isPlayerOne? playerOne:playerTwo;

        Alert as = new Alert(Alert.AlertType.INFORMATION);
        as.setTitle("Game Ended ");
        as.setHeaderText("The Winner is "+ winner);
        as.setContentText("Do you want to play again...?");
        as.getDialogPane().setPrefSize(400,200);
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType(" No Exit");
        as.getButtonTypes().setAll(yes,no);

        Platform.runLater(()-> {Optional<ButtonType>buttonClicked =  as.showAndWait();
        if(buttonClicked.isPresent()&& buttonClicked.get()==yes){
            resetGame();
        }
        else{
            Platform.exit();
            System.exit(0);
        }});


    }

     void resetGame() {

        discPane.getChildren().clear();
        for(int i=0;i<DiscArray.length;i++){
            for(int j=0;j<DiscArray[i].length;j++){
                DiscArray[i][j] = null;
            }
        }
        isPlayerOne = true;
       pOne.clear();
       pTwo.clear();
        playerOne="Player one";
        playerTwo = "Player Two";
        pOne.setPromptText("Player One Name");
        pTwo.setPromptText("Player Two Name");
        playerName.setText(playerOne);
        createPlayground();

    }

    public boolean  gameEnded(int row, int col) {
     List<Point2D> verticalPoints =  IntStream.rangeClosed(row-3,row+3).mapToObj(r-> new Point2D(r,col)).collect(Collectors.toList());
     List<Point2D> horizontalPoints = IntStream.rangeClosed(col-3,col+3).mapToObj(c->new Point2D(row,c)).collect(Collectors.toList());

     Point2D startPoint1 = new Point2D(row-3,col+3);
     List<Point2D> diagonal1= IntStream.rangeClosed(0,6).mapToObj(i->startPoint1.add(i,-i)).collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row-3,col-3);
        List<Point2D> diagonal2= IntStream.rangeClosed(0,6).mapToObj(i->startPoint2.add(i,i)).collect(Collectors.toList());

     boolean isEnded = checkPointMatch(verticalPoints)  || checkPointMatch(horizontalPoints)|| checkPointMatch(diagonal1) || checkPointMatch(diagonal2);

       return isEnded;
    }
    private boolean checkPointMatch(List<Point2D> Points) {
       int chain = 0;
        for(Point2D point : Points){
         int rowIndex = (int)point.getX();
         int colIndex = (int)point.getY();
         Disc disc = getDiscCheck(rowIndex,colIndex);
//            System.out.println(disc);
         if(disc!=null  &&  disc.isPlayerOneTurn == isPlayerOne ){
             chain++;

             if(chain == 4){
                 return true;
             }
         }
         else{
             chain=0;}
        }
        return false;
    }

    private Disc getDiscCheck(int row , int column){
        if(row>=ROWS|| row<0 || column>= COLUMNS||column<0){  return null; }
        return DiscArray[row][column];
    }

    class Disc extends Circle{
        private final boolean isPlayerOneTurn;
        Disc(boolean isPlayerOne){
            this.isPlayerOneTurn = isPlayerOne;
            setCenterX(CIRCLE_DIAMETER/2);
            setCenterY(CIRCLE_DIAMETER/2);
            setRadius(CIRCLE_DIAMETER/2);
            setFill(isPlayerOneTurn?valueOf(disc_color1):valueOf(disc_color2));
        }

    }





}