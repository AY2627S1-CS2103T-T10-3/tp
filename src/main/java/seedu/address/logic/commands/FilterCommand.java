package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBER_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.Objects;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.InstrumentCriterion;
import seedu.address.model.person.MemberType;
import seedu.address.model.person.PersonMatchesFilterPredicate;
import seedu.address.model.person.Role;
import seedu.address.model.person.Status;

/**
 * Lists people matching member criteria, with OR within each field and AND between fields.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists people matching member criteria (OR within a field, AND between fields).\n"
            + "Parameters: [" + PREFIX_STATUS + "STATUS]... [" + PREFIX_INSTRUMENT + "NAME[:MIN_YEARS]]... "
            + "[" + PREFIX_MEMBER_TYPE + "TYPE]... [" + PREFIX_ROLE + "ROLE]...\n"
            + "At least one criterion is required. TYPE is student or teacher; ROLE matches the whole role, "
            + "ignoring case.\n"
            + "MIN_YEARS is an inclusive minimum from 0 to 99; omitted years match any experience.\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STATUS + "member "
            + PREFIX_STATUS + "active-alumnus " + PREFIX_INSTRUMENT + "Violin:3";

    private final Set<Status> statuses;
    private final Set<InstrumentCriterion> instrumentCriteria;
    private final Set<MemberType> memberTypes;
    private final Set<Role> roles;

    /**
     * Creates a filter command with optional status, instrument, member type, and role criteria.
     * An empty set leaves its field unrestricted.
     */
    public FilterCommand(Set<Status> statuses, Set<InstrumentCriterion> instrumentCriteria,
            Set<MemberType> memberTypes, Set<Role> roles) {
        this.statuses = Set.copyOf(requireNonNull(statuses));
        this.instrumentCriteria = Set.copyOf(requireNonNull(instrumentCriteria));
        this.memberTypes = Set.copyOf(requireNonNull(memberTypes));
        this.roles = Set.copyOf(requireNonNull(roles));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(
                new PersonMatchesFilterPredicate(statuses, instrumentCriteria, memberTypes, roles));
        return new CommandResult(String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean requiresSave() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand otherCommand)) {
            return false;
        }

        return statuses.equals(otherCommand.statuses) && instrumentCriteria.equals(otherCommand.instrumentCriteria)
                && memberTypes.equals(otherCommand.memberTypes) && roles.equals(otherCommand.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statuses, instrumentCriteria, memberTypes, roles);
    }
}
