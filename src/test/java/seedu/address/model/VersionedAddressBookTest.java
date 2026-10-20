package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class VersionedAddressBookTest {

    @Test
    public void constructor_newVersionedAddressBook_canUndoIsFalse() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        assertFalse(vab.canUndo());
        assertEquals(new AddressBook(), vab);
    }

    @Test
    public void commit_thenAddAlice_canUndoTrue() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.addPerson(ALICE);
        vab.commit();
        assertTrue(vab.canUndo());
        vab.undo();
        assertEquals(0, vab.getPersonList().size());
        assertFalse(vab.canUndo());
    }

    @Test
    public void undo_withNothingToUndo_throwsNoUndoableStateException() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        assertThrows(VersionedAddressBook.NoUndoableStateException.class, vab::undo);
    }

    @Test
    public void commit_afterUndo_discardsLaterStates() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.addPerson(ALICE);
        vab.commit();
        vab.undo();

        Person zed = new PersonBuilder().withName("Zed").build();
        vab.addPerson(zed);
        vab.commit();
        vab.undo();

        assertEquals(0, vab.getPersonList().size());
        assertFalse(vab.canUndo());
    }

    @Test
    public void equals_variousScenarios() {
        VersionedAddressBook vab1 = new VersionedAddressBook(new AddressBook());
        VersionedAddressBook vab2 = new VersionedAddressBook(new AddressBook());

        assertTrue(vab1.equals(vab2));
        assertFalse(vab1.equals(null));
        assertFalse(vab1.equals("Not a VersionedAddressBook"));

        vab1.addPerson(ALICE);
        vab1.commit();
        assertFalse(vab1.equals(vab2));

        vab2.addPerson(ALICE);
        vab2.commit();
        assertTrue(vab1.equals(vab2));

        // same data but different undo history -> equal
        assertTrue(vab1.equals(new VersionedAddressBook(vab1)));
    }
}
