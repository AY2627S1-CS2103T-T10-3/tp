package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.model.ModelManager;

public class SortCommandParserTest {
    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validAttributes_acceptsCaseAndSurroundingWhitespace() throws Exception {
        for (String attribute : new String[] {"name", "status"}) {
            assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, attribute),
                    parser.parse(" \t" + attribute.toUpperCase(Locale.ROOT) + "\n ")
                            .execute(new ModelManager()).getFeedbackToUser());
        }
    }

    @Test
    public void parse_invalidArguments_throwsParseException() {
        for (String args : new String[] {"", " ", "role", "name status", "status name", "status desc"}) {
            assertParseFailure(parser, args, String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }
}
