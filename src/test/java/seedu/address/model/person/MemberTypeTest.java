package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class MemberTypeTest {

    @Test
    public void isValidMemberType_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> MemberType.isValidMemberType(null));
    }

    @Test
    public void isValidMemberType_invalidMemberType_returnsFalse() {
        assertFalse(MemberType.isValidMemberType(""));
        assertFalse(MemberType.isValidMemberType(" "));
        assertFalse(MemberType.isValidMemberType("alumnus"));
    }

    @Test
    public void isValidMemberType_validMemberType_returnsTrue() {
        assertTrue(MemberType.isValidMemberType("student"));
        assertTrue(MemberType.isValidMemberType("TEACHER"));
        assertTrue(MemberType.isValidMemberType(" student "));
    }

    @Test
    public void fromString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> MemberType.fromString(null));
    }

    @Test
    public void fromString_invalidMemberType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> MemberType.fromString("alumnus"));
    }

    @Test
    public void fromString_validMemberType_returnsMatchingMemberType() {
        assertEquals(MemberType.STUDENT, MemberType.fromString(" Student "));
        assertEquals(MemberType.TEACHER, MemberType.fromString("TEACHER"));
    }

    @Test
    public void toString_returnsKeyword() {
        assertEquals("student", MemberType.STUDENT.toString());
        assertEquals("teacher", MemberType.TEACHER.toString());
    }
}
