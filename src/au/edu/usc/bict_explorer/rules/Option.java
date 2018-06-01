package au.edu.usc.bict_explorer.rules;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents one choice out of a set of options.
 * <p>
 * Selecting this choice object typically enables a set of 'downstream'
 * options (also Option objects) that are required by this choice.
 * <p>
 * In turn, this option may be controlled by one or more 'upstream' options.
 *
 * @author Mark Utting
 */
public class Option {
    private final String code;

    private final String name;

    private final String description;

    private boolean chosen;

    /**
     * The downstream choices of this choice.
     */
    private final Set<Option> controls;

    /**
     * The upstream choices that this choice is controlled by.
     */
    private final Set<Option> controlledBy;


    /**
     * Create a new option.
     *
     * @param code       Unique code for this course or option.
     * @param name       Official title of thie course or option.
     * @param desc       Information description.
     * @param downstream any sub-options that are dictated by this option.
     */
    public Option(String code, String name, String desc, Option... downstream) {
        this.code = code;
        this.name = name;
        this.description = desc;
        controls = new LinkedHashSet<>();
        controlledBy = new LinkedHashSet<>();

        if (downstream != null) {
            for (Option child : downstream) {
                controls.add(child);
                child.addController(this);
            }
        }
    }

    /**
     * Tell this option that it may be controlled by the given upstream option.
     *
     * @param option the controlling option.
     */
    public void addController(Option option) {
        controlledBy.add(option);
    }

    @Override
    public String toString() {
        return code;
    }

    /**
     * Checks if a given downstream choice is required by this choice.
     *
     * @param ch a downstream choice.
     * @return true if ch is required when this option is chosen.
     */
    public boolean isRequiredBy(Option ch) {
        return controls.contains(ch);
    }

    /**
     * @return true if this option has been chosen.
     */
    public boolean isChosen() {
        return chosen;
    }

    /**
     * Turns this choice on/off, and updates all downstream choices.
     *
     * @param on true means this option has been chosen.
     */
    public void setChosen(boolean on) {
        if (on && !chosen) {
            // turn ON this choice
            chosen = true;
            for (Option choice : controls) {
                choice.setChosen(true);
            }
        } else if (!on && chosen) {
            // turn OFF this choice
            chosen = false;
            for (Option choice : controls) {
                choice.makeOptional();
            }
        }
        // else no change!
    }

    /**
     * Sets the chosen flag based on the current downstream choices that have been made.
     * This never changes the status of leaf nodes, because they have no downstream choices.
     */
    public void refresh() {
        if (!controls.isEmpty()) {
            boolean ok = true;
            for (Option choice : controls) {
                if (!choice.isChosen()) {
                    ok = false;
                }
            }
            chosen = ok;
        }
    }

    /**
     * @return the name
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * An upstream controller of this choice has changed, so make this
     * choice optional (un-choose it), if no other controllers require it.
     */
    public void makeOptional() {
        if (controlledBy.stream().noneMatch(o -> o.isChosen())) {
            setChosen(false);
        }
    }

    /**
     * Get a copy of all the downstream options.
     *
     * @return an immutable set.
     */
    public Set<Option> getDownstream() {
//		Set<String> result = new LinkedHashSet<>();
//		for (Option opt : controls) {
//			result.add(opt.getName());
//		}
        return Collections.unmodifiableSet(controls);
    }

    /**
     * Get a copy of all the upstream options.
     *
     * @return an immutable set.
     */
    public Set<Option> getUpstream() {
        return Collections.unmodifiableSet(controlledBy);
    }

    /**
     * Equality of option objects is based just on their code, which should always be unique.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        Option option = (Option) o;
        return Objects.equals(code, option.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
