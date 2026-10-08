package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesFilterPredicate;
import seedu.address.model.tag.Tag;

/**
 * Lists people with the specified tag, ignoring case.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists people with the specified tag (case-insensitive).\n"
            + "Parameters: t/TAG\n"
            + "Example: " + COMMAND_WORD + " t/friends";

    private final Tag tag;

    /**
     * Creates a filter command with the specified tag.
     */
    public FilterCommand(Tag tag) {
        this.tag = requireNonNull(tag);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new PersonMatchesFilterPredicate(tag));
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

        return tag.equals(otherCommand.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
