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
        assertTrue(group.equals(equivalent));
        assertEquals(group.hashCode(), equivalent.hashCode());
        assertFalse(group.equals(new Group("Training")));
    }
}
