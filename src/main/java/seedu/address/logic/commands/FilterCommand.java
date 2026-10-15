package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUMENT;

import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesFilterPredicate;

/**
 * Lists people who play any of the specified instruments, ignoring case.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists people who play any specified instrument (case-insensitive).\n"
            + "Parameters: " + PREFIX_INSTRUMENT + "INSTRUMENT [" + PREFIX_INSTRUMENT + "MORE_INSTRUMENTS]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_INSTRUMENT + "Violin " + PREFIX_INSTRUMENT + "Viola";

    private final Set<String> instrumentNames;

    /**
     * Creates a filter command matching the specified instrument name.
     */
    public FilterCommand(String instrumentName) {
        this(Set.of(requireNonNull(instrumentName)));
    }

    /**
     * Creates a filter command matching any of the specified instrument names.
     */
    public FilterCommand(Set<String> instrumentNames) {
        this.instrumentNames = Set.copyOf(requireNonNull(instrumentNames));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new PersonMatchesFilterPredicate(instrumentNames));
        return new CommandResult(String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand otherCommand)) {
            return false;
        }

        return instrumentNames.equals(otherCommand.instrumentNames);
    }

    @Override
    public int hashCode() {
        return instrumentNames.hashCode();
    }
}
