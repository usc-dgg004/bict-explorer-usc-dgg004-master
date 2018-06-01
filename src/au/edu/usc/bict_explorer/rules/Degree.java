package au.edu.usc.bict_explorer.rules;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * This class implements the logic of the Degree degree rules at the University of the Sunshine Coast.
 *
 * @author Mark Utting
 */
public class Degree {

    private final Map<String, Option> courses;
    private final Map<String, Option> minors;
    private final Map<String, Option> careers;

    public Degree(File careersFile, File minorsFile, File coursesFile) throws IOException, ParseException {
        courses = Collections.unmodifiableMap(readOptions(coursesFile, new LinkedHashMap<>()));
        minors = Collections.unmodifiableMap(readOptions(minorsFile, courses));
        careers = Collections.unmodifiableMap(readOptions(careersFile, minors));
    }

    /**
     * Reads a tab-separated file describing the available options.
     *
     * Each line has the following fields, separated by tabs.
     * The Semester and Prereqs fields are optional (only for courses).
     * <ol>
     * <li>Code: the short name of the option/course/minor.</li>
     * <li>Name: the full name.</li>
     * <li>Downstream: the comma-separated codes of downstream options that this option requires.</li>
     * <li>Desc: the description paragraph.</li>
     * <li>Semester: a string that can contain only the characters '1', '2', and '3'.</li>
     * <li>Prereqs: the prerequisites of this option, as a list of courses.  For example: "ICT112,ICT115".</li>
     * </ol>
     *
     * @param file       the file to read courses/options from.
     * @param downStream the collection of downstream courses/options.
     * @return the newly read collection of options.  This is a LinkedHashMap so that order is preserved.
     * @throws IOException when read errors occur.
     */
    static Map<String, Option> readOptions(File file, Map<String, Option> downStream)
            throws IOException, ParseException {
        Map<String, Option> result = new LinkedHashMap<>();
        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.isEmpty() || line.startsWith("%")) {
                    // empty lines and comment lines are ignored
                    continue;
                }
                String[] fields = line.split("\t", -1);
                if (fields.length < 4 || fields[0].isEmpty() || fields[1].isEmpty()) {
                    throw new ParseException("Bad option: " + line, 0);
                }
                String[] downCodes = fields[2].isEmpty() ? new String[0] : fields[2].split(",");
                // map each downstream code to the corresponding option.
                // Option[] downOptions = Arrays.stream(downCodes).map(c -> downStream.get(c)).toArray(Option[]::new);

                // more readable as a loop
                Option[] downOptions = new Option[downCodes.length];
                for (int i = 0; i < downCodes.length; i++) {
                    downOptions[i] = downStream.get(downCodes[i]);
                    if (downOptions[i] == null) {
                        throw new ParseException("Unknown downstream option: " + downCodes[i], i);
                    }
                }

                // now parse any extra fields, which means it is a Course object.
                final Option opt;
                if (fields.length == 4) {
                    opt = new Option(fields[0], fields[1], fields[3], downOptions);
                } else if (fields.length == 5) {
                    PreReqs pre = new PreReqs(""); // no prereqs
                    opt = new Course(fields[0], fields[1], fields[3], fields[4], pre, downOptions);
                } else if (fields.length == 6) {
                    PreReqs pre = new PreReqs(fields[5]);
                    opt = new Course(fields[0], fields[1], fields[3], fields[4], pre, downOptions);
                } else {
                    throw new ParseException("Bad course: " + line, 0);
                }
                result.put(fields[0], opt);
            }
        }
        return result;
    }

    public Map<String, Option> courses() {
        return courses;
    }

    public Map<String, Option> minors() {
        return minors;
    }

    public Map<String, Option> careers() {
        return careers;
    }

    /**
     * Propagates recent changes upwards to minors then careers.
     *
     * For example, if some courses have recently been selected or unselected,
     * then calling this method will update all the minors and careers to be consistent with
     * those changes.
     */
    public void processChanges() {
        // refresh back-end state upwards: first minors, then careers
        for (Option opt : minors.values()) {
            opt.refresh();
        }
        for (Option opt : careers.values()) {
            opt.refresh();
        }
    }
}
