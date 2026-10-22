package seedu.address.logic.commands;

import static java.nio.charset.StandardCharsets.UTF_8;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ImportCommandTest {

    private static final Path IMPORT_DIRECTORY = Path.of("imports");
    private static final String FILE_NAME = "import-command-test.csv";
    private static final Path IMPORT_FILE = IMPORT_DIRECTORY.resolve(FILE_NAME);
    private static final String HEADER = "name,phone,email,status,memberType,role,instruments,groups";

    private Model model;

    @BeforeEach
    public void setUp() throws IOException {
        Files.createDirectories(IMPORT_DIRECTORY);
        Files.deleteIfExists(IMPORT_FILE);
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(IMPORT_FILE);
    }

    @Test
    public void constructor_nullFileName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ImportCommand(null));
    }

    @Test
    public void execute_validPeople_success() throws IOException {
        Person existingPerson = new PersonBuilder().withName("Existing Person").withPhone("81111111")
                .withEmail("existing@example.com").build();
        Person importedPerson = new PersonBuilder().withName("Alice Tan").withPhone("91234567")
                .withEmail("alice@example.com").withRole("Section Leader")
                .withInstruments("Violin:8").withGroups("Orchestra").build();
        model.addPerson(existingPerson);
        writeImportFile(HEADER,
                "Alice Tan,91234567,alice@example.com,member,student,Section Leader,Violin:8,Orchestra");

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(importedPerson);

        assertCommandSuccess(new ImportCommand(FILE_NAME), model,
                String.format(ImportCommand.MESSAGE_SUCCESS, 1, IMPORT_FILE), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_failure() throws IOException {
        Person existingPerson = new PersonBuilder().withName("Existing Person").withPhone("81111111")
                .withEmail("existing@example.com").build();
        model.addPerson(existingPerson);
        writeImportFile(HEADER, "Existing Person,81111111,existing@example.com,member,student,,,");

        assertCommandFailure(new ImportCommand(FILE_NAME), model, ImportCommand.MESSAGE_DUPLICATE_PERSON);
    }

    private void writeImportFile(String... lines) throws IOException {
        Files.write(IMPORT_FILE, String.join(System.lineSeparator(), lines).getBytes(UTF_8));
    }
}
