package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {
    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_validTag_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(new Tag("friends"));
        assertParseSuccess(parser, "t/friends", expectedCommand);
        assertParseSuccess(parser, "  t/  friends  ", expectedCommand);
    }

    @Test
    public void parse_missingTagPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);
        assertParseFailure(parser, "friends", expectedMessage);
    }

    @Test
    public void parse_unexpectedPreamble_throwsParseException() {
        assertParseFailure(parser, "1 t/friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        assertParseFailure(parser, "t/", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "t/friends!", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleTags_throwsParseException() {
        assertParseFailure(parser, "t/friends t/owesMoney",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TAG));
    }
}
