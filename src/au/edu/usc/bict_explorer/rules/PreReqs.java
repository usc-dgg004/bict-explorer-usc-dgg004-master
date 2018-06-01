package au.edu.usc.bict_explorer.rules;


import java.util.Arrays;
import java.util.Map;

/**
 * Records a set of prerequisite courses.
 *
 * For now, this just takes a flat set of prerequisite courses (comma-separated).
 * All of those courses are assumed to be required.
 *
 * @author Mark Utting
 */
public class PreReqs {
    /** All these course codes are required prerequisites. */
    private final String[] required;

    public PreReqs(String spec) {
        if (spec.isEmpty() || "nil".equals(spec.toLowerCase())) {
            this.required = new String[0];
        } else {
            this.required = spec.split(",");
        }
    }

    /**
     * True if these prerequisites are all satisfied.
     *
     * @param courses the courses in the degree, with chosen flags showing which ones will be taken.
     * @return true if prerequisites are satisfied.
     */
    public boolean isSatisfied(Map<String, Option> courses) {
        for (String req : required) {
            if (!courses.containsKey(req)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.required).replaceAll("[ \\[\\]]", "");
    }
}
