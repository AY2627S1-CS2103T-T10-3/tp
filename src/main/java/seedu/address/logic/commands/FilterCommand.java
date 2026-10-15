package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.Objects;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesFilterPredicate;
import seedu.address.model.person.Status;

/**
 * Lists people matching status and instrument criteria, with OR within each field and AND between fields.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists people matching statuses and instruments (OR within a field, AND between fields).\n"
            + "Parameters: [" + PREFIX_STATUS + "STATUS]... [" + PREFIX_INSTRUMENT + "INSTRUMENT]...\n"
            + "At least one status or instrument is required.\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STATUS + "member "
            + PREFIX_STATUS + "active-alumnus " + PREFIX_INSTRUMENT + "Violin";

    private final Set<Status> statuses;
    private final Set<String> instrumentNames;

    /**
     * Creates a filter command with OR within each field and AND between fields.
     * An empty set leaves its field unrestricted.
     */
    public FilterCommand(Set<Status> statuses, Set<String> instrumentNames) {
        this.statuses = Set.copyOf(requireNonNull(statuses));
        this.instrumentNames = Set.copyOf(requireNonNull(instrumentNames));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new PersonMatchesFilterPredicate(statuses, instrumentNames));
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

        return statuses.equals(otherCommand.statuses) && instrumentNames.equals(otherCommand.instrumentNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statuses, instrumentNames);
    }
}
