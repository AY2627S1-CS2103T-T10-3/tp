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
            assertEquals(SortCommand.MESSAGE_SUCCESS, command.execute(model).getFeedbackToUser());
            assertEquals(List.of(alice, zoe), model.getFilteredPersonList());
            assertEquals(List.of(zoe, alice), model.getAddressBook().getPersonList());
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
        for (String input : List.of("phone", "status", "names")) {
            assertParseFailure(parser, input,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        for (String input : List.of("name descending", "name name", "name\tphone")) {
            assertParseFailure(parser, input,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }
}
