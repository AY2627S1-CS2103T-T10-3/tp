package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests tag filtering and preservation of directory records.
 */
public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_matchingTag_showsMatchingPeople() {
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> List.of(ALICE, BENSON, DANIEL).contains(person));

        assertCommandSuccess(new FilterCommand(new Tag("FRIENDS")), model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3), expectedModel);
        assertEquals(List.of(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
        assertEquals(getTypicalAddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_noMatchingTag_showsEmptyList() {
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> false);

        assertCommandSuccess(new FilterCommand(new Tag("unknown")), model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0), expectedModel);
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    @Test
    public void execute_previouslyFiltered_searchesWholeDirectory() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        new FilterCommand(new Tag("owesMoney")).execute(model);

        assertEquals(List.of(BENSON), model.getFilteredPersonList());
        assertEquals(getTypicalAddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_listAfterFilter_showsEveryone() {
        new FilterCommand(new Tag("owesMoney")).execute(model);
        new ListCommand().execute(model);

        assertEquals(getTypicalAddressBook().getPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_spaceAfterPrefix_matchesWholeTag() throws Exception {
        Person friend = new PersonBuilder().withName("Alice").withTags("friend").build();
        Person friends = new PersonBuilder().withName("Bob").withTags("friends").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(friend);
        addressBook.addPerson(friends);
        Model customModel = new ModelManager(addressBook, new UserPrefs());

        CommandResult result = new AddressBookParser().parseCommand("filter t/ friend").execute(customModel);

        assertEquals(List.of(friend), customModel.getFilteredPersonList());
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), result.getFeedbackToUser());
        assertEquals(addressBook, customModel.getAddressBook());
    }

    @Test
    public void execute_multipleTags_showsPeopleWithEitherTag() throws Exception {
        Person friend = new PersonBuilder().withName("Alice").withTags("friend").build();
        Person colleague = new PersonBuilder().withName("Bob").withTags("colleague").build();
        Person both = new PersonBuilder().withName("Carol").withTags("friend", "colleague").build();
        Person unrelated = new PersonBuilder().withName("Dan").withTags("family").build();
        AddressBook addressBook = new AddressBook();
        for (Person person : List.of(friend, colleague, both, unrelated)) {
            addressBook.addPerson(person);
        }
        Model customModel = new ModelManager(addressBook, new UserPrefs());

        CommandResult result = new AddressBookParser().parseCommand(
                "filter t/ FRIEND t/colleague").execute(customModel);

        assertEquals(List.of(friend, colleague, both), customModel.getFilteredPersonList());
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3), result.getFeedbackToUser());
        assertEquals(addressBook, customModel.getAddressBook());
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
