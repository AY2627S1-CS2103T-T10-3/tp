package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StatusTest {

    @Test
    public void isValidStatus_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Status.isValidStatus(null));
    }

    @Test
    public void isValidStatus_invalidStatus_returnsFalse() {
        assertFalse(Status.isValidStatus(""));
        assertFalse(Status.isValidStatus(" "));
        assertFalse(Status.isValidStatus("alumnus"));
    }

    @Test
    public void isValidStatus_validStatus_returnsTrue() {
        assertTrue(Status.isValidStatus("member"));
        assertTrue(Status.isValidStatus("ACTIVE-ALUMNUS"));
        assertTrue(Status.isValidStatus(" inactive-alumnus "));
    }

    @Test
    public void fromString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Status.fromString(null));
    }

    @Test
    public void fromString_invalidStatus_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Status.fromString("alumnus"));
    }

    @Test
    public void fromString_validStatus_returnsMatchingStatus() {
        assertEquals(Status.MEMBER, Status.fromString(" member "));
        assertEquals(Status.ACTIVE_ALUMNUS, Status.fromString("Active-Alumnus"));
        assertEquals(Status.INACTIVE_ALUMNUS, Status.fromString("INACTIVE-ALUMNUS"));
    }

    @Test
    public void toString_returnsKeyword() {
        assertEquals("member", Status.MEMBER.toString());
        assertEquals("active-alumnus", Status.ACTIVE_ALUMNUS.toString());
        assertEquals("inactive-alumnus", Status.INACTIVE_ALUMNUS.toString());
    }
}
