package au.edu.usc.bict_explorer.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreReqsTest {
    private Map<String, Option> courses;

    @BeforeEach
    void setup() {
        courses = new HashMap<>();
        courses.put("ICT112", new Option("ICT112", "Python", "Learn Python"));
        courses.put("ICT115", new Option("ICT115", "Design", "System Design"));
    }

    @Test
    void testEmpty() {
        PreReqs pre = new PreReqs("");
        assertTrue(pre.isSatisfied(courses));
    }

    @Test
    void testSingleMissing() {
        PreReqs pre = new PreReqs("ICT113");
        System.out.println("here: " + pre.isSatisfied(courses));
        assertFalse(pre.isSatisfied(courses));
    }

    @Test
    void testSingleDone() {
        PreReqs pre = new PreReqs("ICT112");
        assertTrue(pre.isSatisfied(courses));
    }

    @Test
    void testTwoDone() {
        PreReqs pre = new PreReqs("ICT112,ICT115");
        assertTrue(pre.isSatisfied(courses));
    }

    @Test
    void testTwoFirst() {
        PreReqs pre = new PreReqs("ICT112,ICT116");
        assertFalse(pre.isSatisfied(courses));
    }

    @Test
    void testTwoSecond() {
        PreReqs pre = new PreReqs("ICT111,ICT112");
        assertFalse(pre.isSatisfied(courses));
    }

    @Test
    void testToString() {
        PreReqs pre = new PreReqs("ICT111,ICT112");
        assertEquals("ICT111,ICT112", pre.toString());
    }
}
