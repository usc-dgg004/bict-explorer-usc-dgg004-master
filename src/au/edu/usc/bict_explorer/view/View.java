package au.edu.usc.bict_explorer.view;


import au.edu.usc.bict_explorer.controller.BictController;
import au.edu.usc.bict_explorer.controller.SelectionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class View extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.getIcons().add(new Image("file:src/au/edu/usc/bict_explorer/resources/newlogo2.png"));

        FXMLLoader bictLoader = new FXMLLoader(getClass().getResource("BictController.fxml"));
        Parent bictParent = (Parent) bictLoader.load();

        FXMLLoader selectionLoader = new FXMLLoader(getClass().getResource("SelectionController.fxml"));
        Parent selectionParent = (Parent) selectionLoader.load();

        BictController bictController = (BictController) bictLoader.getController();
        SelectionController selectionController = (SelectionController)selectionLoader.getController();

        Scene bictScene = new Scene(bictParent);
        Scene selectionScene = new Scene(selectionParent);

        bictController.setSelectionScene(selectionScene);
        selectionController.setBictScene(bictScene);
        bictController.setSelectionController(selectionController);

        stage.setScene(bictScene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
