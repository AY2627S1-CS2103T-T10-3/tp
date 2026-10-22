package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts the displayed persons by the specified attribute.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the displayed members by name in ascending alphabetical order, "
            + "ignoring case.\n"
            + "Parameters: name\n"
            + "Example: " + COMMAND_WORD + " name";

    public static final String MESSAGE_SUCCESS = "Sorted displayed members by name.";

    private final String attribute;

    /**
     * Creates a SortCommand to sort by the specified attribute.
     */
    public SortCommand(String attribute) {
        this.attribute = requireNonNull(attribute);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Comparator<Person> comparator = switch (attribute) {
            case "name" -> Comparator.comparing(
                    person -> person.getName().fullName,
                    String.CASE_INSENSITIVE_ORDER);
            default -> throw new IllegalArgumentException(
                    "Unsupported sort attribute: " + attribute);
        };

        model.updateSortedPersonList(comparator);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean requiresSave() {
        return false;
    }
}
