package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_INVALID_FILENAME;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ImportCommandParserTest {

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_validFileName_success() throws ParseException {
        ImportCommand command = parser.parse("members.csv");
        assertNotNull(command);
        assertEquals("members.csv", command.getFileName());
    }

    @Test
    public void parse_emptyFileName_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE);
        assertParseException(expectedMessage, "");
    }

    @Test
    public void parse_nonCsvFile_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE);
        assertParseException(expectedMessage, "members.txt");
    }

    @Test
    public void parse_invalidFilenameWithQuestionMark_throwsParseException() {
        assertParseException(MESSAGE_INVALID_FILENAME, "bad?name.csv");
    }

    @Test
    public void parse_invalidFilenameWithAsterisk_throwsParseException() {
        assertParseException(MESSAGE_INVALID_FILENAME, "bad*name.csv");
    }

    @Test
    public void parse_invalidFilenameWithOtherInvalidChars_throwsParseException() {
        String[] invalidFilenames = {
            "bad<name.csv", "bad>name.csv", "bad|name.csv", "bad:name.csv",
            "bad\\name.csv", "bad\"name.csv"
        };
        for (String filename : invalidFilenames) {
            assertParseException(MESSAGE_INVALID_FILENAME, filename);
        }
    }

    private void assertParseException(String expectedMessage, String userInput) {
        try {
            parser.parse(userInput);
            throw new AssertionError("Expected ParseException not thrown");
        } catch (ParseException actualException) {
            assertEquals(expectedMessage, actualException.getMessage());
        }
    }
}
