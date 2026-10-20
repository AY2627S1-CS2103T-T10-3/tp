package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests status ordering and its interaction with other displayed-list commands.
 */
public class SortStatusCommandTest {
    private final Person inactive = new PersonBuilder().withName("Amy")
            .withStatus("inactive-alumnus").build();
    private final Person member = new PersonBuilder().withName("Zoe")
            .withStatus("member").build();
    private final Person active = new PersonBuilder().withName("Bob")
            .withStatus("active-alumnus").build();
    private final Person secondMember = new PersonBuilder().withName("Alice")
            .withStatus("member").build();
    private final ModelManager model = new ModelManager(new AddressBookBuilder()
            .withPerson(inactive).withPerson(member).withPerson(active).withPerson(secondMember).build(),
            new UserPrefs());

    @Test
    public void execute_status_sortsByMembershipAndKeepsSourceOrderWithinGroups() throws Exception {
        new SortCommand("name").execute(model);
        Command command = new AddressBookParser().parseCommand("sort STATUS");
        assertEquals("Sorted displayed members by status.", command.execute(model).getFeedbackToUser());
        assertEquals(List.of(member, secondMember, active, inactive), model.getFilteredPersonList());
        assertEquals(List.of(inactive, member, active, secondMember), model.getAddressBook().getPersonList());

        new ListCommand().execute(model);
        assertEquals(List.of(inactive, member, active, secondMember), model.getFilteredPersonList());
    }

    @Test
    public void execute_filteredResults_preservesFilterAndRetainsSortingAcrossQueries() throws Exception {
        AddressBookParser parser = new AddressBookParser();
        parser.parseCommand("filter s/member s/inactive-alumnus").execute(model);
        new SortCommand("status").execute(model);
        assertEquals(List.of(member, secondMember, inactive), model.getFilteredPersonList());

        parser.parseCommand("find amy bob").execute(model);
        assertEquals(List.of(active, inactive), model.getFilteredPersonList());
        parser.parseCommand("filter s/active-alumnus s/member").execute(model);
        assertEquals(List.of(member, secondMember, active), model.getFilteredPersonList());
    }

    @Test
    public void execute_deleteAfterSorting_usesDisplayedIndex() throws Exception {
        new SortCommand("status").execute(model);
        new DeleteCommand(INDEX_FIRST_PERSON).execute(model);
        assertEquals(List.of(secondMember, active, inactive), model.getFilteredPersonList());
        assertEquals(List.of(inactive, active, secondMember), model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_editStatus_reordersEditedMember() throws Exception {
        new SortCommand("status").execute(model);
        new AddressBookParser().parseCommand("edit 1 s/inactive-alumnus").execute(model);
        Person editedMember = new PersonBuilder(member).withStatus("inactive-alumnus").build();
        assertEquals(List.of(secondMember, active, inactive, editedMember), model.getFilteredPersonList());
    }
}
