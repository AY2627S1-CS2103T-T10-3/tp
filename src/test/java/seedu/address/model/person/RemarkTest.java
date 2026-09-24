package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void value_emptyAndUnrestrictedText_preserved() {
        assertEquals("", new Remark("").value);
        assertEquals("  note / 中文!  ", new Remark("  note / 中文!  ").toString());
    }

    @Test
    public void equalsAndHashCode() {
        Remark remark = new Remark("note");
        assertEquals(remark, new Remark("note"));
        assertEquals(remark.hashCode(), new Remark("note").hashCode());
        assertNotEquals(remark, new Remark("other"));
        assertNotEquals(remark, null);
        assertNotEquals(remark, "note");
    }
}
