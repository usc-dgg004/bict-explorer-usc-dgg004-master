package au.edu.usc.bict_explorer.model;


import au.edu.usc.bict_explorer.rules.Option;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

// Handles single row in table view.
public class CareerRecord {

    private StringProperty career;

    private StringProperty minor;

    private StringProperty course;

    private String careerDescription;

    private Map<String, List<Option>> minorCoursesMap;

    public void setCareer(String value) {
        careerProperty().set(value);
    }

    public String getCareer() {
        return careerProperty().get();
    }

    public void setMinor(String value) {
        minorProperty().set(value);
    }

    public String getMinor() {
        return minorProperty().get();
    }

    public void setCourse(String value) {
        courseProperty().set(value);
    }

    public String getCourse() {
        return courseProperty().get();
    }

    public StringProperty careerProperty() {
        if (career == null) {
            career = new SimpleStringProperty(this, "career");
        }
        return career;
    }

    public StringProperty minorProperty() {
        if (minor == null) {
            minor = new SimpleStringProperty(this, "minor");
        }
        return minor;
    }

    public StringProperty courseProperty() {
        if (course == null) {
            course = new SimpleStringProperty(this, "course");
        }
        return course;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public Map<String, List<Option>> getMinorCoursesMap() {
        return minorCoursesMap;
    }

    public void setMinorCoursesMap(Map<String, List<Option>> minorCoursesMap) {
        this.minorCoursesMap = minorCoursesMap;
    }
}
