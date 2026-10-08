package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_blankRole_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Role(""));
        assertThrows(IllegalArgumentException.class, () -> new Role(" \t "));
    }

    @Test
    public void isValidRole() {
        assertThrows(NullPointerException.class, () -> Role.isValidRole(null));
        assertFalse(Role.isValidRole(""));
        assertFalse(Role.isValidRole("  "));
        assertTrue(Role.isValidRole("Section Leader"));
        assertTrue(Role.isValidRole("Leader / Conductor (2026)"));
    }

    @Test
    public void toString_returnsRoleText() {
        assertEquals("Section Leader", new Role("Section Leader").toString());
    }

    @Test
    public void equals() {
        Role role = new Role("Section Leader");

        assertTrue(role.equals(role));
        assertTrue(role.equals(new Role("Section Leader")));
        assertFalse(role.equals(null));
        assertFalse(role.equals(5.0f));
        assertFalse(role.equals(new Role("Conductor")));
    }

    @Test
    public void hashCode_equalRoles_haveEqualHashCode() {
        assertEquals(new Role("Section Leader").hashCode(), new Role("Section Leader").hashCode());
    }
}
