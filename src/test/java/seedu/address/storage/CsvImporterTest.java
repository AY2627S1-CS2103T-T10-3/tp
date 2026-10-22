package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Group;
import seedu.address.model.person.Instrument;
import seedu.address.model.person.Person;

public class CsvImporterTest {

    private static final String HEADER = "name,phone,email,status,memberType,role,instruments,groups";

    @TempDir
    public Path temporaryFolder;

    @Test
    public void importPeople_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> CsvImporter.importPeople(null));
    }

    @Test
    public void importPeople_emptyFile_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv("");

        assertThrows(IllegalValueException.class, "CSV file is empty.", () -> CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_invalidHeader_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv("name,phone,email");
        String expectedMessage = "CSV header must be name,phone,email,status,memberType,role,instruments,groups.";

        assertThrows(IllegalValueException.class, expectedMessage, () -> CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_headerOnly_returnsEmptyList() throws Exception {
        Path csvFile = writeCsv(HEADER);

        assertEquals(List.of(), CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_quotedCommaAndEscapedQuote_parsesRole() throws Exception {
        Path csvFile = writeCsv(
                HEADER,
                "Alice Tan,91234567,alice@example.com,member,student,\"Assistant, \"\"Conductor\"\"\",Violin:1,");

        List<Person> people = CsvImporter.importPeople(csvFile);

        assertEquals(1, people.size());
        assertEquals("Assistant, \"Conductor\"", people.getFirst().getRole().orElseThrow().toString());
    }

    @Test
    public void importPeople_unclosedQuotedValue_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv(HEADER,
                "Alice Tan,91234567,alice@example.com,member,student,\"Section Leader,Violin:1,");
        String expectedMessage = "Unclosed quoted value at line 2.";

        assertThrows(IllegalValueException.class, expectedMessage, () -> CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_invalidInstrumentFormat_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv(HEADER, "Alice Tan,91234567,alice@example.com,member,student,,Violin,");
        String expectedMessage = "Invalid value at line 2: " + Instrument.MESSAGE_FORMAT_CONSTRAINTS;

        assertThrows(IllegalValueException.class, expectedMessage, () -> CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_instrumentWithoutYears_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv(HEADER, "Alice Tan,91234567,alice@example.com,member,student,,Violin:,");
        String expectedMessage = "Invalid value at line 2: " + Instrument.MESSAGE_FORMAT_CONSTRAINTS;

        assertThrows(IllegalValueException.class, expectedMessage, () -> CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_nonNumericInstrumentYears_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv(HEADER, "Alice Tan,91234567,alice@example.com,member,student,,Violin:many,");
        String expectedMessage = "Invalid value at line 2: For input string: \"many\"";

        assertThrows(IllegalValueException.class, expectedMessage, () -> CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_outOfRangeInstrumentYears_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv(HEADER, "Alice Tan,91234567,alice@example.com,member,student,,Violin:100,");
        String expectedMessage = "Invalid value at line 2: " + Instrument.MESSAGE_YEARS_CONSTRAINTS;

        assertThrows(IllegalValueException.class, expectedMessage, () -> CsvImporter.importPeople(csvFile));
    }

    @Test
    public void importPeople_blankGroup_throwsIllegalValueException() throws Exception {
        Path csvFile = writeCsv(HEADER, "Alice Tan,91234567,alice@example.com,member,student,,Violin:1,\" \"");
        String expectedMessage = "Invalid value at line 2: " + Group.MESSAGE_CONSTRAINTS;

        assertThrows(IllegalValueException.class, expectedMessage, () -> CsvImporter.importPeople(csvFile));
    }

    private Path writeCsv(String... lines) throws Exception {
        Path csvFile = temporaryFolder.resolve("members.csv");
        Files.writeString(csvFile, String.join(System.lineSeparator(), lines));
        return csvFile;
    }
}
