package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class InstrumentCriterionTest {

    @Test
    public void constructor_invalidName_throwsException() {
        assertThrows(NullPointerException.class, () -> new InstrumentCriterion(null, 3));
        for (String name : new String[] {"", " \t ", "Violin:3"}) {
            assertThrows(IllegalArgumentException.class,
                    Instrument.MESSAGE_NAME_CONSTRAINTS, () -> new InstrumentCriterion(name, 3));
        }
    }

    @Test
    public void constructor_invalidMinimumYears_throwsExceptionWithCriterionMessage() {
        String expectedMessage = InstrumentCriterion.MESSAGE_MINIMUM_YEARS_CONSTRAINTS;
        assertThrows(IllegalArgumentException.class, expectedMessage, () -> new InstrumentCriterion("Violin", -1));
        assertThrows(IllegalArgumentException.class, expectedMessage, () -> new InstrumentCriterion(
                "Violin", Instrument.MAX_YEARS + 1));
    }

    @Test
    public void constructor_validDetails_normalizesNameAndPreservesMinimumYears() {
        InstrumentCriterion criterion = new InstrumentCriterion("  French   Horn  ", 3);
        assertEquals("French Horn", criterion.getName());
        assertEquals(3, criterion.getMinimumYears());
    }

    @Test
    public void matches_requiresWholeNameAndInclusiveMinimumYears() {
        InstrumentCriterion criterion = new InstrumentCriterion("  BASS   guitar ", 3);
        assertTrue(criterion.matches(new Instrument("Bass Guitar", 3)));
        assertTrue(criterion.matches(new Instrument("BASS GUITAR", 4)));
        assertFalse(criterion.matches(new Instrument("Bass Guitar", 2)));
        assertFalse(criterion.matches(new Instrument("Guitar", 20)));
        assertThrows(NullPointerException.class, () -> criterion.matches(null));
    }

    @Test
    public void matches_boundaryMinimumYears_acceptsZeroAndMaximum() {
        InstrumentCriterion anyExperience = new InstrumentCriterion("Violin", 0);
        assertTrue(anyExperience.matches(new Instrument("Violin", 0)));
        assertTrue(anyExperience.matches(new Instrument("Violin", Instrument.MAX_YEARS)));

        InstrumentCriterion maximumExperience = new InstrumentCriterion("Violin", Instrument.MAX_YEARS);
        assertTrue(maximumExperience.matches(new Instrument("Violin", Instrument.MAX_YEARS)));
        assertFalse(maximumExperience.matches(new Instrument("Violin", Instrument.MAX_YEARS - 1)));
    }

    @Test
    public void equalsAndHashCode_includeNormalizedNameAndMinimumYears() {
        InstrumentCriterion criterion = new InstrumentCriterion("Bass Guitar", 3);
        InstrumentCriterion equivalent = new InstrumentCriterion("  BASS   guitar ", 3);
        assertEquals(criterion, criterion);
        assertEquals(criterion, equivalent);
        assertEquals(criterion.hashCode(), equivalent.hashCode());
        assertTrue(Set.of(criterion).contains(equivalent));
        assertNotEquals(criterion, new InstrumentCriterion("Bass Guitar", 4));
        assertNotEquals(criterion, new InstrumentCriterion("Guitar", 3));
        assertNotEquals(criterion, new Instrument("Bass Guitar", 3));
        assertNotEquals(criterion, null);
    }
}
