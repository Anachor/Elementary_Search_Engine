package ServerClient.Mokkel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class GUI extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox searchBar = makeSearchBar();

        BorderPane window = new BorderPane();
        window.setTop(searchBar);

        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(window, 640, 480));
        primaryStage.show();
    }

    private HBox makeSearchBar() {
        TextField searchBox = new TextField();
        searchBox.setPromptText("Enter keyword");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            System.out.println("Ohhhhhhh!!!!");
        });

        Button luckyButton = new Button("I'm Feeling Lucky!");
        luckyButton.setOnAction(e -> {
            System.out.println("Unlucky!!!!");
        });

        HBox searchBar = new HBox(5);
        searchBar.getChildren().addAll(searchBox, searchButton, luckyButton);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        HBox.setHgrow(searchButton, Priority.NEVER);
        HBox.setHgrow(luckyButton, Priority.NEVER);

        return searchBar;
    }
}
