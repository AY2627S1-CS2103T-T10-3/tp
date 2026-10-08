package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

/**
 * Tests the filter command's argument feedback and preservation of model state.
 */
public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_tagProvided_reportsTagWithoutChangingModel() {
        assertCommandFailure(new FilterCommand(new Tag("friends")), model, "Tag: friends");
    }

    @Test
    public void equals() {
        FilterCommand command = new FilterCommand(new Tag("friends"));

        assertEquals(command, command);
        assertEquals(command, new FilterCommand(new Tag("friends")));
        assertNotEquals(command, new FilterCommand(new Tag("owesMoney")));
        assertNotEquals(command, null);
        assertNotEquals(command, "friends");
    }
}
