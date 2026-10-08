package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents the filter command under development.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Filter command not implemented yet";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
