package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBER_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Instrument;
import seedu.address.model.person.Status;

/**
 * Parses input arguments and creates a FilterCommand.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Prefixes of other fields that {@code filter} does not support yet. They are tokenized only so that they are
     * rejected instead of being silently read as part of an instrument name.
     */
    private static final Prefix[] UNSUPPORTED_PREFIXES = {
        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_MEMBER_TYPE, PREFIX_ROLE
    };

    /**
     * Parses status and instrument criteria, including optional minimum years, and returns a FilterCommand.
     *
     * @throws ParseException if the arguments do not conform to the expected format.
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        Prefix[] prefixes = Stream.concat(Stream.of(PREFIX_STATUS, PREFIX_INSTRUMENT), Stream.of(UNSUPPORTED_PREFIXES))
                .toArray(Prefix[]::new);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args.trim(), prefixes);

        boolean hasUnsupportedPrefix = Stream.of(UNSUPPORTED_PREFIXES)
                .anyMatch(prefix -> argMultimap.getValue(prefix).isPresent());
        boolean hasCriteria = argMultimap.getValue(PREFIX_STATUS).isPresent()
                || argMultimap.getValue(PREFIX_INSTRUMENT).isPresent();
        if (!argMultimap.getPreamble().isEmpty() || !hasCriteria
                || hasUnsupportedPrefix) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        Set<Status> statuses = new HashSet<>();
        for (String status : argMultimap.getAllValues(PREFIX_STATUS)) {
            statuses.add(ParserUtil.parseStatus(status));
        }
        Set<Instrument> instrumentCriteria = new HashSet<>();
        for (String instrument : argMultimap.getAllValues(PREFIX_INSTRUMENT)) {
            instrumentCriteria.add(parseInstrumentCriterion(instrument));
        }
        return new FilterCommand(statuses, instrumentCriteria);
    }

    /**
     * Parses a name with optional minimum years, defaulting to zero when years are omitted.
     */
    private Instrument parseInstrumentCriterion(String instrument) throws ParseException {
        if (instrument.contains(":")) {
            return ParserUtil.parseInstrument(instrument);
        }
        return new Instrument(ParserUtil.parseInstrumentName(instrument), 0);
    }
}
