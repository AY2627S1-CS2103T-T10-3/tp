package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class RedoCommandTest {

    @Test
    public void execute_nothingUndone_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertCommandFailure(new RedoCommand(), model, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_afterUndo_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person zed = new PersonBuilder().withName("Zed").build();
        model.addPerson(zed);
        model.commitAddressBook();
        model.undoAddressBook();

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.addPerson(zed);
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_afterNewChange_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person zed = new PersonBuilder().withName("Zed").build();
        model.addPerson(zed);
        model.commitAddressBook();
        model.undoAddressBook();
        model.addPerson(new PersonBuilder().withName("Yan").build());
        model.commitAddressBook();

        assertCommandFailure(new RedoCommand(), model, RedoCommand.MESSAGE_FAILURE);
    }
}
