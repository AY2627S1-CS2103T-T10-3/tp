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
        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_afterCommittedChange_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(new PersonBuilder().withName("Zed").build());
        model.commitAddressBook();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_afterFilteredList_showsAllPersons() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(new PersonBuilder().withName("Zed").build());
        model.commitAddressBook();
        model.updateFilteredPersonList(person -> false);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
