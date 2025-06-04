package lk.ijse.finalProject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        stage.setTitle("Dream Baby Care Products");


        InputStream iconStream = getClass().getResourceAsStream("/images/dbcp logo.jpg");
        if (iconStream != null) {
            Image icon = new Image(iconStream);
            stage.getIcons().add(icon);
        } else {
            System.err.println("Icon image not found");
        }
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        Platform.runLater(() -> {
            stage.setMaximized(true);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
