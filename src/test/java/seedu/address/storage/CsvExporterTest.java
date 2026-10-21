package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.DANIEL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CsvExporterTest {
    @TempDir
    public Path testFolder;

    @Test
    public void export_onePerson_writesHeaderAndPerson() throws Exception {
        Path outputPath = testFolder.resolve("members.csv");

        Path returnedPath = CsvExporter.export(List.of(DANIEL), outputPath);

        assertEquals(outputPath, returnedPath);
        assertEquals(String.join(System.lineSeparator(),
                CsvExporter.HEADER,
                "\"Daniel Meier\",\"87652533\",\"cornelia@example.com\","
                        + "\"member\",\"teacher\",\"Conductor\",\"Violin:20\""),
                Files.readString(outputPath));
    }
}
