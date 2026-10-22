package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Locale;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given arguments and returns a SortCommand for execution.
     *
     * @throws ParseException if the arguments are not a supported sort attribute.
     */
    @Override
    public SortCommand parse(String args) throws ParseException {
        String attribute = args.trim().toLowerCase(Locale.ROOT);

        return switch (attribute) {
            case "name" -> new SortCommand("name");
            default -> throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        };
    }
}
