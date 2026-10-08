package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class InstrumentTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Instrument(null, 3));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Instrument("", 3));
        assertThrows(IllegalArgumentException.class, () -> new Instrument("Trumpet:3", 3));
    }

    @Test
    public void constructor_invalidYears_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Instrument("Trumpet", -1));
        assertThrows(IllegalArgumentException.class, () -> new Instrument("Trumpet", Instrument.MAX_YEARS + 1));
    }

    @Test
    public void constructor_extraWhitespaceInName_normalizesName() {
        assertEquals("French Horn", new Instrument("  French   Horn ", 2).getName());
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Instrument.isValidName(null));

        // invalid names
        assertFalse(Instrument.isValidName("")); // empty string
        assertFalse(Instrument.isValidName(" \t ")); // whitespace only
        assertFalse(Instrument.isValidName("Trumpet:3")); // contains the years separator

        // valid names
        assertTrue(Instrument.isValidName("Trumpet"));
        assertTrue(Instrument.isValidName("French Horn")); // with space
        assertTrue(Instrument.isValidName("Bb Clarinet (2nd)")); // with digits and symbols
        assertTrue(Instrument.isValidName("Er-hu"));
    }

    @Test
    public void isValidYears() {
        assertFalse(Instrument.isValidYears(-1));
        assertFalse(Instrument.isValidYears(Instrument.MAX_YEARS + 1));

        assertTrue(Instrument.isValidYears(0)); // boundary
        assertTrue(Instrument.isValidYears(5));
        assertTrue(Instrument.isValidYears(Instrument.MAX_YEARS)); // boundary
    }

    @Test
    public void hasName() {
        Instrument instrument = new Instrument("French Horn", 3);

        assertTrue(instrument.hasName("French Horn"));
        assertTrue(instrument.hasName("french horn")); // different case
        assertTrue(instrument.hasName("  FRENCH   horn ")); // extra whitespace

        assertFalse(instrument.hasName("Horn")); // partial name
        assertFalse(instrument.hasName("Trumpet"));
        assertThrows(NullPointerException.class, () -> instrument.hasName(null));
    }

    @Test
    public void equals() {
        Instrument instrument = new Instrument("Trumpet", 3);

        // same values -> returns true
        assertTrue(instrument.equals(new Instrument("Trumpet", 3)));

        // same name in different case -> returns true
        assertTrue(instrument.equals(new Instrument("TRUMPET", 3)));

        // same object -> returns true
        assertTrue(instrument.equals(instrument));

        // null -> returns false
        assertFalse(instrument.equals(null));

        // different types -> returns false
        assertFalse(instrument.equals(5.0f));

        // different name -> returns false
        assertFalse(instrument.equals(new Instrument("Cornet", 3)));

        // different years -> returns false
        assertFalse(instrument.equals(new Instrument("Trumpet", 4)));
    }

    @Test
    public void hashCode_equalInstruments_haveEqualHashCode() {
        assertEquals(new Instrument("Trumpet", 3).hashCode(), new Instrument("trumpet", 3).hashCode());
    }

    @Test
    public void toString_returnsNameAndYears() {
        assertEquals("French Horn:3", new Instrument("French Horn", 3).toString());
    }
}
