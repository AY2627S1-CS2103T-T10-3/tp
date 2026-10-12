package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUMENT;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a FilterCommand.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses one or more instrument name arguments and returns a FilterCommand.
     *
     * @throws ParseException if the arguments do not conform to the expected format.
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args.trim(), PREFIX_INSTRUMENT);

        if (!argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_INSTRUMENT).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        Set<String> instrumentNames = new HashSet<>();
        for (String instrumentName : argMultimap.getAllValues(PREFIX_INSTRUMENT)) {
            instrumentNames.add(ParserUtil.parseInstrumentName(instrumentName));
        }
        return new FilterCommand(instrumentNames);
    }
}
