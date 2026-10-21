package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.storage.CsvExporter;

/**
 * Exports the people in the current filtered list to a CSV file.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_EXPORT_SUCCESS = "Exported %d people to %s.";
    public static final String MESSAGE_EXPORT_FAILURE = "Could not export current results: %s";

    private static final Path EXPORT_DIRECTORY = Path.of("exports");

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> currentResults = model.getFilteredPersonList();

        try {
            Path outputPath = CsvExporter.export(currentResults, EXPORT_DIRECTORY);

            return new CommandResult(
                String.format(MESSAGE_EXPORT_SUCCESS, currentResults.size(), outputPath));
        } catch (IOException ioe) {
            throw new CommandException(
                String.format(MESSAGE_EXPORT_FAILURE, ioe.getMessage()), ioe);
        }
    }

    @Override
    public boolean requiresSave() {
        return false;
    }
}
