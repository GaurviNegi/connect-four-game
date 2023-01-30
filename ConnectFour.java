package game.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectFour extends Application {
    public Controller controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ConnectFour.class.getResource("hello-view.fxml"));
        GridPane rootNode  = fxmlLoader.load();

        MenuBar menubar = createMenu();

        Pane menuPane = (Pane) rootNode.getChildren().get(0);
        menuPane.getChildren().add(menubar);
        menubar.prefWidthProperty().bind(stage.widthProperty());
        menubar.prefHeightProperty().bind(menuPane.heightProperty());
        controller  = fxmlLoader.getController();

        controller.createPlayground();

        Scene scene = new Scene(rootNode);
        stage.setTitle("Connect Four Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private MenuBar createMenu() {
        MenuBar menubar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> controller.resetGame());
        MenuItem exitGame = new MenuItem("Exit Game");
        exitGame.setOnAction(event-> exitGame());
        SeparatorMenuItem separator1 = new SeparatorMenuItem();
        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(event-> controller.resetGame());
        file.getItems().addAll(newGame,resetGame,separator1,exitGame);


        Menu help = new Menu("Help");
        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event->aboutMe());

        MenuItem aboutGame = new MenuItem("About Connect Four");
        aboutGame.setOnAction(event->aboutGame());
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        help.getItems().addAll(aboutGame,separator2,aboutMe);
        menubar.getMenus().addAll(file,help);
        return menubar;
    }

    private void aboutGame() {
        Alert a= new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("About the Connect Four ");
        a.setHeaderText("How to play?");
        a.setContentText("Connect Four is a two-player connection game in which the" +
                " players first choose a color and then take turns dropping colored" +
                " discs from the top into a seven-column, six-row vertically suspended grid." +
                " The pieces fall straight down, occupying the next available space within the column." +
                " The objective of the game is to be the first to form a horizontal, vertical," +
                " or diagonal line of four of one's own discs. Connect Four is a solved game." +
                " The first player can always win by playing the right moves.");
        a.getDialogPane().setPrefSize(400,300);
        a.setResizable(true);
        a.show();
    }

    private void aboutMe() {
        Alert as = new Alert(Alert.AlertType.INFORMATION);
       as.setTitle("About the developer");
       as.setHeaderText("Gaurvi Negi");
       as.setContentText("I love to play with codes and games.i am a keen java developer.");
       as.show();
    }



    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }





    public static void main(String[] args) {
        launch();
    }
}