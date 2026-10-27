package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GroupTest {

    @Test
    public void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Group.MESSAGE_CONSTRAINTS, () -> new Group(" \t "));
    }

    @Test
    public void constructor_extraWhitespace_normalizesName() {
        assertEquals("Chamber Orchestra", new Group("  Chamber   Orchestra  ").getName());
    }

    @Test
    public void equals_sameNameIgnoringCaseAndWhitespace_returnsTrue() {
        Group group = new Group("Chamber Orchestra");
        Group equivalent = new Group(" chamber   orchestra ");
        assertTrue(group.equals(group));
        assertTrue(group.equals(equivalent));
        assertEquals(group.hashCode(), equivalent.hashCode());
        assertFalse(group.equals(new Group("Training")));
        assertFalse(group.equals(null));
        assertFalse(group.equals("Chamber Orchestra"));
    }

    @Test
    public void isIdenticalTo_sameName_returnsTrue() {
        assertTrue(new Group("Band").isIdenticalTo(new Group("Band")));
    }

    @Test
    public void isIdenticalTo_differentCase_returnsFalse() {
        Group band = new Group("Band");
        Group lowercaseBand = new Group("band");

        // equals remains case-insensitive
        assertTrue(band.equals(lowercaseBand));
        assertFalse(band.isIdenticalTo(lowercaseBand));
    }

    @Test
    public void isIdenticalTo_null_returnsFalse() {
        assertFalse(new Group("Band").isIdenticalTo(null));
    }
}
