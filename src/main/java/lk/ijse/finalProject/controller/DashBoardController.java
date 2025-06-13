package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    public AnchorPane ancMainContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void orderPlacementOnAction(ActionEvent actionEvent) {
        navigateTo("/view/OrderPlacementView.fxml");
    }

    public void productionPlacementOnAction(ActionEvent actionEvent) {
        navigateTo("/view/ProductionPlacementView.fxml");
    }

    public void OverViewOnAction(ActionEvent actionEvent) {
        navigateTo("/view/OverView.fxml");

    }

    private void navigateTo(String path) {
       try {
            ancMainContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));


            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());

            ancMainContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }

       /*try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dream Baby Care Products");

            InputStream iconStream = getClass().getResourceAsStream("/images/dbcp logo.jpg");
            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
            } else {
                System.err.println("Icon image not found");
            }
            stage.setResizable(true);
            stage.setMaximized(true); // Maximizes the window with window controls
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }*/
    }
}
