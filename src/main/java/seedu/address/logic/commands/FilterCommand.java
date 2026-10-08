package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Represents the filter command under development.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters people by one tag.\n"
            + "Parameters: t/TAG\n"
            + "Example: " + COMMAND_WORD + " t/friends";
    public static final String MESSAGE_ARGUMENTS = "Tag: %1$s";

    private final Tag tag;

    /**
     * Creates a filter command with the specified tag.
     */
    public FilterCommand(Tag tag) {
        this.tag = requireNonNull(tag);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(String.format(MESSAGE_ARGUMENTS, tag.tagName));
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
