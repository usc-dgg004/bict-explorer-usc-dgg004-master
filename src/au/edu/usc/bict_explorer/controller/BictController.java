package au.edu.usc.bict_explorer.controller;


import au.edu.usc.bict_explorer.model.CareerRecord;
import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class BictController implements Initializable {

    @FXML
    private TableView<CareerRecord> viewRecords;

    @FXML
    private AnchorPane mainPanel;

    private Scene selectionScene;

    private SelectionController selectionController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewRecords.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("career"));
        viewRecords.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("minor"));
        viewRecords.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("course"));
        updateCareerRecords();

        viewRecords.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            if (newSelection != null) {
                selectionController.setCareerRecord(newSelection);

                selectionController.getLblCareerName().setText(newSelection.getCareer());
                selectionController.getLblCareerDescription().setText(newSelection.getCareerDescription());



                Stage stage = (Stage) mainPanel.getScene().getWindow();
                stage.setScene(selectionScene);selectionController.updateComboBoxes();
            }
        });
    }

    /*
    This method allows this program to be Data-Driver since each
    time when it called all data files processed.
     */
    private void updateCareerRecords() {
        try {
            Degree degree = new Degree(new File("src/au/edu/usc/bict_explorer/resources/careers.options"),
                    new File("src/au/edu/usc/bict_explorer/resources/minors.options"),
                    new File("src/au/edu/usc/bict_explorer/resources/courses.options"));
            Map<String, Option> opts = degree.careers();
            List<CareerRecord> records = new ArrayList<>();

            for (Option opt: opts.values()) {
                CareerRecord cr = new CareerRecord();
                Map<String, List<Option>> minorCoursesMap = new TreeMap<>();
                cr.setCareer(opt.getName());
                cr.setCareerDescription(opt.getDescription());
                cr.setMinorCoursesMap(minorCoursesMap);

                StringBuilder minors = new StringBuilder();
                StringBuilder courses = new StringBuilder();

                // Retrieve minors for current career.
                for (Option min: opt.getDownstream()) {
                    minors.append(min.getName()).append("\n");
                    List<Option> listCourses =  new ArrayList<>();

                    // Retrieve required courses.
                    for (Option cour: min.getDownstream()) {
                        if (courses.indexOf(cour.getName()) == -1) { // To avoid duplication.
                            courses.append(cour.getName()).append("\n");
                        }
                        listCourses.add(cour);
                    }
                    Collections.sort(listCourses, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                    minorCoursesMap.put(min.getName(), listCourses);
                }
                cr.setCourse(courses.toString());
                cr.setMinor(minors.toString());
                records.add(cr);
            }

            viewRecords.getItems().clear();
            ObservableList<CareerRecord> listRecords = FXCollections.observableList(records);
            viewRecords.setItems(listRecords);
        } catch (IOException | ParseException exc) {
            exc.printStackTrace(); // FIXME change to alert dialog!
        }
    }

    public void setSelectionScene(Scene selectionScene) {
        this.selectionScene = selectionScene;
    }

    public void setSelectionController(SelectionController selectionController) {
        this.selectionController = selectionController;
    }
}
