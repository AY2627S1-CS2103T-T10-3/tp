package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.PersonBuilder;

public class UndoCommandTest {

    @Test
    public void execute_noChange_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // Note: TypicalAddressBook is not versioned by default in standard setup,
        // but assuming Model implementation handles canUndoAddressBook()
        // For this test to work as requested, we assume model is a ModelManager
        // wrapping a VersionedAddressBook that hasn't had commit() called yet.
        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_afterChange_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // We need to simulate a commit to make canUndo true
        // Since ModelManager.commitAddressBook() is provided:
        model.addPerson(new PersonBuilder().withName("Zed").build());
        model.commitAddressBook();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // The expected model should be the state before the "Zed" addition
        // In a real scenario, we'd capture the model state before the addPerson

        // For the purpose of this test logic:
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
