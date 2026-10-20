package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code AddressBook} that keeps a history of its committed states so that changes can be undone.
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
     * Records the current data as a new state, discarding any states after the current one.
     * Does nothing if the data is unchanged since the current state, so commands that do not
     * modify data do not create empty undo steps.
     */
    public void commit() {
        if (!hasUncommittedChanges()) {
            return;
        }
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;
        assert currentStatePointer == addressBookStateList.size() - 1;
    }

    /**
     * Returns true if there is a change that {@link #undo()} can revert.
     */
    public boolean canUndo() {
        return currentStatePointer > 0 || hasUncommittedChanges();
    }

    /**
     * Reverts the most recent change.
     * If the data has uncommitted changes (e.g. a change whose save failed),
     * the data is restored to the current state. Otherwise, it is restored to the previous state.
     *
     * @throws NoUndoableStateException if there is no change to revert.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        if (!hasUncommittedChanges()) {
            currentStatePointer--;
        }
        resetData(addressBookStateList.get(currentStatePointer));
        assert currentStatePointer >= 0 && currentStatePointer < addressBookStateList.size();
    }

    /**
     * Returns true if the data differs from the current state in the history.
     */
    private boolean hasUncommittedChanges() {
        return !equals(addressBookStateList.get(currentStatePointer));
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
