package au.edu.usc.bict_explorer.controller;


import au.edu.usc.bict_explorer.model.CareerRecord;
import au.edu.usc.bict_explorer.rules.Course;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class SelectionController implements Initializable {

    @FXML
    private AnchorPane mainPanel;

    private Scene bictScene;

    @FXML
    private Label lblCareerName;

    @FXML
    private Label lblCareerDescription;

    @FXML
    private ComboBox<String> cbMinors;

    @FXML
    private ComboBox<String> cbCourses;

    @FXML
    private Label lblNumSelCourses;

    private CareerRecord careerRecord;

    private int numCourses;

    private List<String> selectedCourses;

    private List<String> selectedMinors;

    private List<String> selectedCareers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SelectionController initialized");
        selectedCourses = new ArrayList<>();
        selectedMinors = new ArrayList<>();
        selectedCareers = new ArrayList<>();
    }

    @FXML
    private void handleSelectMinor() {
        String minor = cbMinors.getSelectionModel().getSelectedItem();
        List<Option> courses = careerRecord.getMinorCoursesMap().get(minor);
        cbCourses.getItems().clear();
        List<String> list = new ArrayList<>();
        try {courses.forEach(c -> list.add(c.getName()));
        cbCourses.getItems().addAll(list);} catch (Exception e){}
        cbCourses.getSelectionModel().select(0);
    }

    public void updateComboBoxes() {
        // Fill out combo boxes with minors and courses required for the selected career.
        cbMinors.getItems().clear();
        cbMinors.getItems().addAll(careerRecord.getMinorCoursesMap().keySet());
        cbMinors.getSelectionModel().select(0);
        handleSelectMinor();
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.setScene(bictScene);
    }

    @FXML
    private void handleAddCourse() {
        String course = cbCourses.getSelectionModel().getSelectedItem();
        Option optCourse = careerRecord.getMinorCoursesMap().get(cbMinors.getSelectionModel().getSelectedItem())
                .get(cbCourses.getSelectionModel().getSelectedIndex());
        Map<String, Option> courses = new HashMap<>();
        for (String prereq: selectedCourses) {
            courses.put(prereq, null);
        }
        if (selectedCourses.contains(course)) {
            showMessage(Alert.AlertType.WARNING, course + " has already been selected!", "Wrong selection");
        } else if (!((Course)optCourse).isSatisfied(courses)) {
            showMessage(Alert.AlertType.WARNING, course + " is not satisfied!", "Wrong selection");
        } else {
            selectedCourses.add(course);
            numCourses ++;
            lblNumSelCourses.setText(numCourses + "");
            showMessage(Alert.AlertType.INFORMATION, course + " has been added successfully", "Success");
        }

        if (!selectedCareers.contains(careerRecord.getCareer())) {
            selectedCareers.add(careerRecord.getCareer());
        }
        if (!selectedMinors.contains(cbMinors.getSelectionModel().getSelectedItem())) {
            selectedMinors.add(cbMinors.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void handleSaveReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Report File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        Stage stage = (Stage) mainPanel.getScene().getWindow();

        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            StringBuilder report = new StringBuilder();
            report.append(selectedFile.getName()).append("\r\n\r\n");
            report.append("Selected careers: ").append("\r\n");
            for (String car: selectedCareers) {
                report.append(car).append(", ");
            }
            report.append("\r\n\r\nSelected minors: ").append("\r\n");
            for (String min: selectedMinors) {
                report.append(min).append(", ");
            }
            report.append("\r\n\r\nSelected courses: ").append("\r\n");
            for (String cour: selectedCourses) {
                report.append(cour).append(", ");
            }
            report.append("\r\n\r\n" + LocalDateTime.now()).append("\r\n");
            report.append("---------------------------------------------------------------------\r\n\r\n\r\n");
           try (FileWriter fw = new FileWriter(selectedFile, true)) {
               fw.write(report.toString());
               showMessage(Alert.AlertType.INFORMATION, "Report has been saved successfully", "Success");
           } catch (IOException exc) {
               showMessage(Alert.AlertType.ERROR, "File cannot be saved: " + exc, "Report Failed");
           }
        }
    }

    private void showMessage(Alert.AlertType type, String msg, String title) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(title);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public void setBictScene(Scene bictScene) {
        this.bictScene = bictScene;
    }

    public Label getLblCareerName() {
        return lblCareerName;
    }

    public Label getLblCareerDescription() {
        return lblCareerDescription;
    }

    public void setCareerRecord(CareerRecord careerRecord) {
        this.careerRecord = careerRecord;
    }
}
