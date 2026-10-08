package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesFilterPredicate;
import seedu.address.model.tag.Tag;

/**
 * Lists people with any of the specified tags, ignoring case.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists people with any specified tag (case-insensitive).\n"
            + "Parameters: t/TAG [t/MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " t/friends t/colleague";

    private final Set<Tag> tags;

    /**
     * Creates a filter command with the specified tag.
     */
    public FilterCommand(Tag tag) {
        this(Set.of(requireNonNull(tag)));
    }

    /**
     * Creates a filter command matching any of the specified tags.
     */
    public FilterCommand(Set<Tag> tags) {
        this.tags = Set.copyOf(requireNonNull(tags));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(new PersonMatchesFilterPredicate(tags));
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

        return tags.equals(otherCommand.tags);
    }

    @Override
    public int hashCode() {
        return tags.hashCode();
    }
}
