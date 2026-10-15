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

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Instrument;
import seedu.address.model.person.Role;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_STATUS,
                        PREFIX_MEMBER_TYPE, PREFIX_ROLE, PREFIX_INSTRUMENT);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_STATUS,
                PREFIX_MEMBER_TYPE, PREFIX_ROLE);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            editPersonDescriptor.setStatus(ParserUtil.parseStatus(argMultimap.getValue(PREFIX_STATUS).get()));
        }
        if (argMultimap.getValue(PREFIX_MEMBER_TYPE).isPresent()) {
            editPersonDescriptor.setMemberType(
                    ParserUtil.parseMemberType(argMultimap.getValue(PREFIX_MEMBER_TYPE).get()));
        }
        if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
            editPersonDescriptor.setRole(parseRoleForEdit(argMultimap.getValue(PREFIX_ROLE).get()));
        }
        parseInstrumentsForEdit(argMultimap.getAllValues(PREFIX_INSTRUMENT))
                .ifPresent(editPersonDescriptor::setInstruments);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code role} into the role to edit to. An empty {@code role} clears the person's role.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    private Optional<Role> parseRoleForEdit(String role) throws ParseException {
        requireNonNull(role);
        if (role.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parseRole(role));
    }

    /**
     * Parses {@code Collection<String> instruments} into a {@code Set<Instrument>} if {@code instruments}
     * is non-empty.
     * If {@code instruments} contains only one element which is an empty string, it will be parsed into a
     * {@code Set<Instrument>} containing zero instruments.
     */
    private Optional<Set<Instrument>> parseInstrumentsForEdit(Collection<String> instruments)
            throws ParseException {
        requireNonNull(instruments);

        if (instruments.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> instrumentSet = instruments.size() == 1 && instruments.contains("")
                ? Collections.emptySet()
                : instruments;
        return Optional.of(ParserUtil.parseInstruments(instrumentSet));
    }

}
