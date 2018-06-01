package au.edu.usc.bict_explorer.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Course course1;

    private Course course2;

    private Course course3;

    private Course courseIllegal;

    private Course courseSat;

    private Course courseNotSat;

    private Map<String, Option> courses = new HashMap<>();

    @BeforeEach
    void setUp() throws Exception {
        courses.put("req1", null);
        courses.put("req2", null);
        courses.put("req3", null);
        course1 = new Course("BUS101", "name1", "name1 desc", "sem1",
                new PreReqs("req1,req2,req3"));
        course2 = new Course("BUS102", "name2", "name2 desc", "sem2",
                new PreReqs("req1,req2,req3"));
        course3 = new Course("BUS103", "name3", "name3 desc", "sem3",
                new PreReqs("req1,req2,req3"));
        courseIllegal = new Course("BUS103", "name3", "name3 desc", "sem3",
                new PreReqs("req1,req2,req3"));
        courseSat = new Course("BUS103", "name3", "name3 desc", "sem3",
                new PreReqs("req1,req2,req3"));
        courseNotSat = new Course("BUS103", "name3", "name3 desc", "sem3",
                new PreReqs("req1,req2,req4"));
    }

    @Test
    void testIsOffered1() {
        assertTrue(course1.isOffered(1));
        assertFalse(course1.isOffered(2));
        assertFalse(course1.isOffered(3));
    }

    @Test
    void testIsOffered2() {
        assertFalse(course2.isOffered(1));
        assertTrue(course2.isOffered(2));
        assertFalse(course2.isOffered(3));
    }

    @Test
    void testIsOffered3() {
        assertFalse(course3.isOffered(1));
        assertFalse(course3.isOffered(2));
        assertTrue(course3.isOffered(3));
    }

    @Test
    void testIsOfferedIllegal() {
        assertThrows(IllegalArgumentException.class,
                ()->{
                    courseIllegal.isOffered(4);
                });
    }

    @Test
    void testIsSatisfied() {
        assertTrue(courseSat.isSatisfied(courses));
    }

    @Test
    void testIsNotSatisfied() {
        assertFalse(courseNotSat.isSatisfied(courses));
    }

}
