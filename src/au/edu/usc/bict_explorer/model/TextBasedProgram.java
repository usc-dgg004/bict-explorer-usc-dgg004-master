package au.edu.usc.bict_explorer.model;


import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class TextBasedProgram {

    public static void main(String...args) {
        try {

            Degree degree = new Degree(new File("src/au/edu/usc/bict_explorer/resources/careers.options"),
                    new File("src/au/edu/usc/bict_explorer/resources/minors.options"),
                    new File("src/au/edu/usc/bict_explorer/resources/courses.options"));

            StringBuilder output = new StringBuilder();

            for (Map.Entry<String, Option> me: degree.minors().entrySet()) {
                output.append("Minor: ").append(me.getKey()).append("\r\n");
                output.append("Courses: ");
               for (Option opt: me.getValue().getDownstream()) {
                   output.append(opt).append(" ");
               }
                output.append("\r\n\r\n");
            }

            try (FileWriter fw = new FileWriter(new File("bict.txt"))) {
                fw.write(output.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
