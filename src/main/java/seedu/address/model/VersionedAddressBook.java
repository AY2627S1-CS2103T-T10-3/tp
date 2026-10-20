package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link AddressBook} that supports undo operations by maintaining
 * a history of previous states.
 */
public class VersionedAddressBook extends AddressBook {

    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

    /**
     * Constructs a {@code VersionedAddressBook} with the given initial state.
     *
     * @param initialState the initial state of the address book
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Commits the current state to the history. If the pointer is not at the end of the list,
     * all states after the current pointer are discarded before adding the new state.
     */
    public void commit() {
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;
        assert currentStatePointer >= 0 && currentStatePointer < addressBookStateList.size();
    }

    /**
     * Returns {@code true} if there are states available to undo.
     *
     * @return {@code true} if {@code currentStatePointer > 0}
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Undoes the most recent change by reverting to the previous state in the history.
     *
     * @throws NoUndoableStateException if there is no state to undo to
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
        assert currentStatePointer >= 0 && currentStatePointer < addressBookStateList.size();
    }

    /**
     * Exception thrown when an undo operation is attempted but no previous states exist.
     */
    public static class NoUndoableStateException extends RuntimeException {
        public NoUndoableStateException() {
            super("Current state pointer at start of addressBookState list, unable to undo.");
        }
    }
}
