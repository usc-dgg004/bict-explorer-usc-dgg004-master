package au.edu.usc.bict_explorer.rules;

import java.util.Map;

/**
 * A Course is a special kind of Option, which also has semesters and prerequisites.
 *
 * @author Mark Utting
 */
public class Course extends Option {

    /** A non-null string containing a set of '1', '2', and/or '3' characters. */
    private final String semesters;

    /** The prerequsites of this course. */
    private final PreReqs preReqs;

    /**
     * Create a new option.
     *
     * @param code       Unique code for this course or option.
     * @param name       Official title of this course or option.
     * @param desc       Information description.
     * @param semesters  A string containing '1', '2', and/or '3'.
     * @param pre        The prerequisites for this course.
     * @param downstream any sub-options that are dictated by this option.
     */
    public Course(String code, String name, String desc, String semesters, PreReqs pre, Option... downstream) {
        super(code, name, desc, downstream);
        assert semesters != null;
        assert pre != null;
        this.semesters = semesters;
        this.preReqs = pre;
    }

    /**
     * Find out if this course is offered in a given semester.
     *
     * @param semester
     * @return true if the course is offered that semester.
     */
    public boolean isOffered(int semester) {

        // there are more general ways of doing this, but this way is simple...
        switch (semester) {
            case 1: return semesters.contains("1");
            case 2: return semesters.contains("2");
            case 3: return semesters.contains("3");
            default: throw new IllegalArgumentException("Illegal semester: " + semester);
        }
    }

    /**
     * Check if the prerequisites of this course are satisfied.
     * @param courses
     * @return true if the prerequisites of this course have been chosen.
     */
    public boolean isSatisfied(Map<String, Option> courses) {
        return this.preReqs.isSatisfied(courses);
    }

    /** @return the prerequisites of this course. */
    public PreReqs getPreReqs() {
        return this.preReqs;
    }

    /** @return the semester in which this course is offered */
    public String getSemesters(){ return this.semesters;}
}
