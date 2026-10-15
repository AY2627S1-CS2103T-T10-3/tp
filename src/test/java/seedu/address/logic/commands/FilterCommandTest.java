package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests instrument filtering and preservation of directory records.
 */
public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_matchingInstrument_showsMatchingPeople() {
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> List.of(ALICE, BENSON, DANIEL).contains(person));

        assertCommandSuccess(new FilterCommand("VIOLIN"), model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3), expectedModel);
        assertEquals(List.of(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
        assertEquals(getTypicalAddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_noMatchingInstrument_showsEmptyList() {
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> false);

        assertCommandSuccess(new FilterCommand("Harp"), model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0), expectedModel);
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    @Test
    public void execute_previouslyFiltered_searchesWholeDirectory() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        new FilterCommand("Cello").execute(model);

        assertEquals(List.of(BENSON), model.getFilteredPersonList());
        assertEquals(getTypicalAddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_listAfterFilter_showsEveryone() {
        new FilterCommand("Cello").execute(model);
        new ListCommand().execute(model);

        assertEquals(getTypicalAddressBook().getPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_spaceAfterPrefix_matchesWholeInstrumentName() throws Exception {
        Person guitarist = new PersonBuilder().withName("Alice").withInstruments("Guitar:3").build();
        Person bassist = new PersonBuilder().withName("Bob").withInstruments("Bass Guitar:2").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(guitarist);
        addressBook.addPerson(bassist);
        Model customModel = new ModelManager(addressBook, new UserPrefs());

        CommandResult result = new AddressBookParser().parseCommand("filter i/ guitar").execute(customModel);

        assertEquals(List.of(guitarist), customModel.getFilteredPersonList());
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), result.getFeedbackToUser());
        assertEquals(addressBook, customModel.getAddressBook());
    }

    @Test
    public void execute_multipleInstruments_showsPeoplePlayingEitherInstrument() throws Exception {
        Person violinist = new PersonBuilder().withName("Alice").withInstruments("Violin:3").build();
        Person violist = new PersonBuilder().withName("Bob").withInstruments("Viola:1").build();
        Person both = new PersonBuilder().withName("Carol").withInstruments("Violin:2", "Viola:4").build();
        Person unrelated = new PersonBuilder().withName("Dan").withInstruments("Drums:5").build();
        AddressBook addressBook = new AddressBook();
        for (Person person : List.of(violinist, violist, both, unrelated)) {
            addressBook.addPerson(person);
        }
        Model customModel = new ModelManager(addressBook, new UserPrefs());

        CommandResult result = new AddressBookParser().parseCommand(
                "filter i/ VIOLIN i/viola").execute(customModel);

        assertEquals(List.of(violinist, violist, both), customModel.getFilteredPersonList());
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3), result.getFeedbackToUser());
        assertEquals(addressBook, customModel.getAddressBook());
    }

    @Test
    public void hashCode_equalInstrumentSets_supportsHashSetLookup() {
        FilterCommand command = new FilterCommand(Set.of("Violin", "Viola"));
        FilterCommand equivalentCommand = new FilterCommand(Set.of("Viola", "Violin"));
        Set<FilterCommand> commands = new HashSet<>();
        commands.add(command);

        assertEquals(command, equivalentCommand);
        assertEquals(command.hashCode(), equivalentCommand.hashCode());
        assertTrue(commands.contains(equivalentCommand));
    }

    @Test
    public void equals() {
        FilterCommand command = new FilterCommand("Violin");

        assertEquals(command, command);
        assertEquals(command, new FilterCommand("Violin"));
        assertNotEquals(command, new FilterCommand("Cello"));
        assertNotEquals(command, null);
        assertNotEquals(command, "Violin");
    }
}
