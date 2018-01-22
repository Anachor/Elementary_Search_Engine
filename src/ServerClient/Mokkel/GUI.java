package ServerClient.Mokkel;

import ServerClient.SearchQuery;
import ServerClient.SearchResult;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application{
    private BorderPane window;
    private Client client;

    @Override
    public void start(Stage primaryStage) throws IOException {
        client = new Client("127.0.0.1", 33333);

        HBox searchBar = makeSearchBar();
        window = new BorderPane();
        window.setTop(searchBar);

        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(window, 640, 480));
        primaryStage.show();
    }

    private void searchButtonAction(TextField searchBox) {
        try {
            String input = searchBox.getText();
            SearchQuery query = new SearchQuery(input);
            System.out.println("Searching for " + query);

            client.write(query);
            SearchResult result = client.read();

            VBox resultsList = new VBox();
            for (String url : result.getResults()) {
                TextArea t = new TextArea(url);
                resultsList.getChildren().add(t);
            }

            window.setCenter(resultsList);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from Network");
            client.clear();
        }
    }

    private HBox makeSearchBar() {
        TextField searchBox = new TextField();
        searchBox.setPromptText("Enter keyword");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            searchButtonAction(searchBox);
        });

        Button clearButton = new Button("Clear Results");
        clearButton.setOnAction(e -> {
            System.out.println("Cleared!!!");
            window.setCenter(null);
        });

        searchBox.setOnKeyPressed((event) -> {
            if(event.getCode() == KeyCode.ENTER) {
                searchButtonAction(searchBox);
            }
        });

        HBox searchBar = new HBox(5);
        searchBar.getChildren().addAll(searchBox, searchButton, clearButton);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        HBox.setHgrow(searchButton, Priority.NEVER);
        HBox.setHgrow(clearButton, Priority.NEVER);

        return searchBar;
    }
}
