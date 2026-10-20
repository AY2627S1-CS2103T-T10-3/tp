package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;

public class VersionedAddressBookTest {

    private static AddressBook bookOf(Person... persons) {
        AddressBook book = new AddressBook();
        for (Person p : persons) {
            book.addPerson(p);
        }
        return book;
    }

    @Test
    public void constructor_initialState_cannotUndo() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        assertFalse(vab.canUndo());
        assertEquals(bookOf(), vab);
    }

    @Test
    public void commit_noChange_cannotUndo() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.commit();
        assertFalse(vab.canUndo());
    }

    @Test
    public void undo_afterCommittedChange_restoresPreviousState() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.addPerson(ALICE);
        vab.commit();
        assertTrue(vab.canUndo());
        vab.undo();
        assertEquals(bookOf(), vab);
        assertFalse(vab.canUndo());
    }

    @Test
    public void undo_multipleTimes_restoresStatesInOrder() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.addPerson(ALICE);
        vab.commit();
        vab.addPerson(BENSON);
        vab.commit();
        vab.undo();
        assertEquals(bookOf(ALICE), vab);
        vab.undo();
        assertEquals(bookOf(), vab);
        assertFalse(vab.canUndo());
    }

    @Test
    public void undo_uncommittedChange_revertsToCurrentState() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.addPerson(ALICE);
        vab.commit();
        vab.addPerson(BENSON);
        assertTrue(vab.canUndo());
        vab.undo();
        assertEquals(bookOf(ALICE), vab);
        assertTrue(vab.canUndo());
        vab.undo();
        assertEquals(bookOf(), vab);
    }

    @Test
    public void undo_afterRolledBackUndo_revertsAgain() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.addPerson(ALICE);
        vab.commit();
        vab.addPerson(BENSON);
        vab.commit();
        vab.undo();
        vab.resetData(bookOf(ALICE, BENSON));
        vab.undo();
        assertEquals(bookOf(ALICE), vab);
        vab.undo();
        assertEquals(bookOf(), vab);
        assertFalse(vab.canUndo());
    }

    @Test
    public void undo_nothingToUndo_throwsNoUndoableStateException() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        assertThrows(VersionedAddressBook.NoUndoableStateException.class, vab::undo);
    }

    @Test
    public void commit_afterUndo_discardsLaterStates() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        vab.addPerson(ALICE);
        vab.commit();
        vab.undo();
        vab.addPerson(BENSON);
        vab.commit();
        vab.undo();
        assertEquals(bookOf(), vab);
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
