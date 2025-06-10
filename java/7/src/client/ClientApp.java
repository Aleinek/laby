package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class ClientApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/MainView.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();

        primaryStage.setTitle("Klient drzewa binarnego");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> controller.shutdown()); // zamykanie socketu
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
