package ServerClient.Mokkel;

import ServerClient.SearchQuery;
import ServerClient.SearchResult;
import edu.stanford.nlp.ie.machinereading.structure.Event;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;

public class GUI extends Application{
    private BorderPane searchPane;
    private Stage window;
    private Client client;

    @Override
    public void start(Stage primaryStage) throws IOException{

        window = primaryStage;

        Scene promptScene = makePromptScene();
        Scene searchScene = makeSearchScene();
        
        primaryStage.setTitle("Client");
        primaryStage.setScene(makePromptScene());
        primaryStage.show();
    }

    private Scene makeSearchScene () {
        HBox searchBar = makeSearchBar();
        searchPane = new BorderPane();
        searchPane.setTop(searchBar);
        return new Scene(searchPane, 640, 480);
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
            searchPane.setCenter(null);
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

    private void searchButtonAction(TextField searchBox) {
        try {
            String input = searchBox.getText();
            SearchQuery query = new SearchQuery(input);
            System.out.println("Searching for " + query);

            client.write(query);
            SearchResult result = client.read();

            ListView resultsList = new ListView();
            for ( String s: result.getResults()) {
                Hyperlink link = new Hyperlink(s);
                link.setOnAction( e -> {
                    getHostServices().showDocument(s);
                });
                resultsList.getItems().add(link);
            }

            if (result.getResults().size() == 0) {
                Label notFoundLabel = new Label("No results found.");
                resultsList.getItems().add(notFoundLabel);
            }

            searchPane.setCenter(resultsList);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from Network");
            client.clear();
        }
    }

    private Scene makePromptScene() {
        VBox promptWindow = new VBox(10);

        Label enterIp = new Label("Enter IP Address: ");

        TextField ipBox = new TextField("127.0.0.1");
        ipBox.setOnKeyPressed( e -> {
            if (e.getCode() == KeyCode.ENTER)
            connectButtonAction(ipBox);
        });

        Button connectButton = new Button("Connect");
        connectButton.setOnAction( e -> {
            connectButtonAction(ipBox);
        });

        promptWindow.getChildren().addAll(enterIp, ipBox, connectButton);
        return new Scene(promptWindow, 200, 100);
    }

    private void connectButtonAction(TextField searchBox) {
        try {
            String s = searchBox.getText();
            client = new Client(s, 33333);
            window.setScene(makeSearchScene());
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Error Connecting to Serevr.");
        }
    }
}