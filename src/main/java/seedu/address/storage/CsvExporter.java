package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.util.FileUtil;
import seedu.address.model.person.Instrument;
import seedu.address.model.person.Person;

/**
 * A class that writes supplied member information to CSV files.
 */

public class CsvExporter {
    public static final String HEADER = "name,phone,email,status,memberType,role,instruments";

    /**
     * Exports the given people to a CSV file at the specified path.
     *
     * @param people the people to export.
     * @param outputPath the path of the CSV file to create or replace.
     * @return the path of the written CSV file.
     * @throws IOException if the file cannot be written.
     */
    public static Path export(List<Person> people, Path outputPath) throws IOException {
        requireNonNull(people);
        requireNonNull(outputPath);

        String csvContent = buildCsvContent(people);

        FileUtil.createIfMissing(outputPath);
        FileUtil.writeToFile(outputPath, csvContent);

        return outputPath;
    }

    private static String buildCsvContent(List<Person> people) {
        StringBuilder csvContent = new StringBuilder(HEADER);

        for (Person person : people) {
            csvContent.append(System.lineSeparator());
            csvContent.append(toCsvRow(person));
        }

        return csvContent.toString();
    }

    private static String toCsvRow(Person person) {
        return String.join(",",
            escape(person.getName().toString()),
            escape(person.getPhone().toString()),
            escape(person.getEmail().toString()),
            escape(person.getStatus().toString()),
            escape(person.getMemberType().toString()),
            escape(person.getRole().map(Object::toString).orElse("")),
            escape(formatInstruments(person.getInstruments())));
    }

    private static String formatInstruments(Set<Instrument> instruments) {
        return instruments.stream()
            .sorted(Comparator.comparing(Instrument::getName, String.CASE_INSENSITIVE_ORDER))
            .map(Instrument::toString)
            .collect(Collectors.joining(";"));
    }

    private static String escape(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
