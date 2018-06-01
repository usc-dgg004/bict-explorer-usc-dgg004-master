package au.edu.usc.bict_explorer.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class OptionTest {

    private Option opt1;
    private Option opt2;
    private Option suba;
    private Option subb;
    private Option subc;

    @BeforeEach
    void setUp() throws Exception {
        suba = new Option("SubA", "Sub-option A", "AAAA");
        subb = new Option("SubB", "Sub-option B", "BBBBB");
        subc = new Option("SubC", "Sub-option C", "CCCCCC");
        opt1 = new Option("Opt1", "Option 1", "Option 1 requires a and b", suba, subb);
        opt2 = new Option("Opt2", "Option 2", "Option 2 requires b and c", subb, subc);
    }

    @Test
    void testBasics() {
        assertEquals("Opt1", opt1.getCode());
        assertEquals("Option 1", opt1.getName());
        assertEquals("Option 1 requires a and b", opt1.getDescription());
        assertFalse(opt1.isChosen());
        assertFalse(opt1.isRequiredBy(opt2));
    }

    @Test
    void testChoose() {
        assertFalse(opt1.isChosen());
        opt1.setChosen(true);
        assertTrue(opt1.isChosen());
        opt1.setChosen(false);
        assertFalse(opt1.isChosen());
    }

    @Test
    void testChildren() {
        Set<Option> down = opt1.getDownstream();
        assertEquals(2, down.size());
        assertTrue(down.contains(suba));
        assertTrue(down.contains(subb));
        Iterator<Option> iter = down.iterator();
        assertTrue(iter.next().equals(suba));
        assertTrue(iter.next().equals(subb));
        assertFalse(iter.hasNext());
    }

    @Test
    void testChosen1() {
        assertFalse(suba.isChosen());
        assertFalse(subb.isChosen());
        assertFalse(subc.isChosen());
        // this should also choose all its downstream options.
        opt1.setChosen(true);
        assertTrue(opt1.isChosen());
        assertTrue(suba.isChosen());
        assertTrue(subb.isChosen());
        assertFalse(subc.isChosen());
        // this should make optional all its downstream options.
        // (since all parents are not chosen, all downstream options will become false).
        opt1.setChosen(false);
        assertFalse(opt1.isChosen());
        assertFalse(suba.isChosen());
        assertFalse(subb.isChosen());
        assertFalse(subc.isChosen());
    }

    @Test
    void testChosenOverlap() {
        assertFalse(suba.isChosen());
        assertFalse(subb.isChosen());
        assertFalse(subc.isChosen());
        // this should also choose all its downstream options.
        opt1.setChosen(true);
        opt2.setChosen(true);
        assertTrue(opt1.isChosen());
        assertTrue(opt2.isChosen());
        assertTrue(suba.isChosen());
        assertTrue(subb.isChosen());
        assertTrue(subc.isChosen());
        // this should make optional all opt1's downstream options.
        opt1.setChosen(false);
        assertFalse(opt1.isChosen());
        assertTrue(opt2.isChosen());
        assertFalse(suba.isChosen());
        assertTrue(subb.isChosen()); // still required by opt2
        assertTrue(subc.isChosen()); // still required by opt2
    }

    /**
     * Check that refresh on a leaf node does not change its chosen flag.
     */
    @Test
    void testLeafRefresh() {
        assertFalse(suba.isChosen());
        suba.refresh();
        assertFalse(suba.isChosen());
        suba.setChosen(true);
        assertTrue(suba.isChosen());
        suba.refresh();
        assertTrue(suba.isChosen());
        suba.setChosen(false);
        assertFalse(suba.isChosen());
        suba.refresh();
        assertFalse(suba.isChosen());
    }

    @Test
    void testParentRefresh() {
        assertFalse(suba.isChosen());
        assertFalse(subb.isChosen());
        assertFalse(opt1.isChosen());
        // turn both children on
        suba.setChosen(true);
        subb.setChosen(true);
        assertFalse(opt1.isChosen());
        opt1.refresh();
        assertTrue(opt1.isChosen());
        // now turn one child off
        subb.setChosen(false);
        assertTrue(opt1.isChosen());
        opt1.refresh();
        assertFalse(opt1.isChosen());
    }
}
