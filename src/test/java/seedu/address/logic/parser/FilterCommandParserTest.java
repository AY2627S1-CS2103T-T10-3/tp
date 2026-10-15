package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.Instrument;

public class FilterCommandParserTest {
    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_validInstrument_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand("Violin");
        assertParseSuccess(parser, "i/Violin", expectedCommand);
        assertParseSuccess(parser, "  i/  Violin  ", expectedCommand);
        assertParseSuccess(parser, "i/Bass Guitar", new FilterCommand("Bass Guitar"));
    }

    @Test
    public void parse_missingInstrumentPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);
        assertParseFailure(parser, "Violin", expectedMessage);
    }

    @Test
    public void parse_tagPrefix_throwsParseException() {
        assertParseFailure(parser, "t/friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unsupportedPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "i/Violin s/member", expectedMessage);
        assertParseFailure(parser, "s/member i/Violin", expectedMessage);
        assertParseFailure(parser, "i/Violin n/Alice", expectedMessage);
        assertParseFailure(parser, "i/Violin ty/teacher r/Conductor", expectedMessage);
        assertParseFailure(parser, "i/Violin p/91234567 e/alice@example.com", expectedMessage);
    }

    @Test
    public void parse_unexpectedPreamble_throwsParseException() {
        assertParseFailure(parser, "1 i/Violin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidInstrument_throwsParseException() {
        assertParseFailure(parser, "i/", Instrument.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "i/Violin:3", Instrument.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "i/Violin i/", Instrument.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "i/Violin:3 i/Cello", Instrument.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_multipleInstruments_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(Set.of("Violin", "Cello"));
        assertParseSuccess(parser, "i/Violin i/Cello", expectedCommand);
        assertParseSuccess(parser, "i/ Cello i/ Violin i/Violin", expectedCommand);
    }
}
