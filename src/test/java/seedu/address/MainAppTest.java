package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class MainAppTest {
    @TempDir
    public Path temporaryFolder;

    @Test
    public void initModelManager_invalidAddressBook_recoversWithoutOverwritingData() throws Exception {
        Path addressBookPath = temporaryFolder.resolve("addressbook.json");
        String invalidJson = "not valid JSON";
        Files.writeString(addressBookPath, invalidJson);
        StorageManager storageManager = createStorageManager(addressBookPath);

        TestMainApp app = new TestMainApp();
        Model model = app.initializeModelManager(storageManager);

        assertTrue(model.getAddressBook().getPersonList().isEmpty());
        assertEquals(invalidJson, Files.readString(addressBookPath.resolveSibling("addressbook.json.bak")));
        assertThrows(IOException.class, () -> storageManager.saveAddressBook(new AddressBook()));
        assertEquals(invalidJson, Files.readString(addressBookPath));
    }

    @Test
    public void initModelManager_backupFails_throwsIllegalStateException() {
        Path addressBookPath = temporaryFolder.resolve("addressbook.json");
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(addressBookPath) {
            @Override
            public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
                throw new DataLoadingException(new IOException("invalid address book"));
            }

            @Override
            public Path backupAddressBook() throws IOException {
                throw new IOException("backup failed");
            }
        };
        StorageManager storageManager = new StorageManager(addressBookStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("preferences.json")));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                new TestMainApp().initializeModelManager(storageManager));

        assertInstanceOf(IOException.class, exception.getCause());
    }

    @Test
    public void init_usesStorageManagerToInitializeModel() throws Exception {
        TestMainApp app = new TestMainApp();

        app.init();

        assertInstanceOf(StorageManager.class, app.storage);
        assertSame(app.storage, app.storageProvidedToModelInitialization);
        assertSame(app.modelToInitialize, app.model);
    }

    private StorageManager createStorageManager(Path addressBookPath) {
        return new StorageManager(new JsonAddressBookStorage(addressBookPath),
                new JsonUserPrefsStorage(temporaryFolder.resolve("preferences.json")));
    }

    private static class TestMainApp extends MainApp {
        private final Model modelToInitialize = new ModelManager();
        private StorageManager storageProvidedToModelInitialization;

        @Override
        protected UserPrefs initPrefs(JsonUserPrefsStorage storage) {
            return new UserPrefs();
        }

        @Override
        protected Model initModelManager(StorageManager storage, ReadOnlyUserPrefs userPrefs) {
            storageProvidedToModelInitialization = storage;
            return modelToInitialize;
        }

        private Model initializeModelManager(StorageManager storage) {
            return super.initModelManager(storage, new UserPrefs());
        }
    }
}
