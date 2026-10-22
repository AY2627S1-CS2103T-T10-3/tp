package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class SortCommandParserTest {
    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_returnsNameSortCommand() throws Exception {
        Person zoe = new PersonBuilder().withName("Zoe").build();
        Person alice = new PersonBuilder().withName("alice").build();
        for (String input : List.of("name", "NAME", "NaMe", " \n name \t ")) {
            ModelManager model = new ModelManager(new AddressBookBuilder()
                    .withPerson(zoe).withPerson(alice).build(), new UserPrefs());
            SortCommand command = parser.parse(input);
            assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, "name"),
                    command.execute(model).getFeedbackToUser());
            assertEquals(List.of(alice, zoe), model.getFilteredPersonList());
            assertEquals(List.of(zoe, alice), model.getAddressBook().getPersonList());
        }
    }

    @Test
    public void parse_statusArgs_returnsStatusSortCommand() throws Exception {
        Person inactive = new PersonBuilder().withName("Alice").withStatus("inactive-alumnus").build();
        Person member = new PersonBuilder().withName("Zoe").withStatus("member").build();
        Person active = new PersonBuilder().withName("Bob").withStatus("active-alumnus").build();
        for (String input : List.of("status", "STATUS", "StAtUs", " \n status \t ")) {
            ModelManager model = new ModelManager(new AddressBookBuilder()
                    .withPerson(inactive).withPerson(member).withPerson(active).build(), new UserPrefs());
            assertEquals("Sorted displayed members by status.", parser.parse(input).execute(model).getFeedbackToUser());
            assertEquals(List.of(member, active, inactive), model.getFilteredPersonList());
            assertEquals(List.of(inactive, member, active), model.getAddressBook().getPersonList());
        }
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        for (String input : List.of("", " ", "\n\t")) {
            assertParseFailure(parser, input,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }

    @Test
    public void parse_unsupportedAttribute_throwsParseException() {
        for (String input : List.of("phone", "role", "names")) {
            assertParseFailure(parser, input,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        for (String input : List.of("name descending", "name name", "name\tphone", "name status", "status desc")) {
            assertParseFailure(parser, input,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }
}
