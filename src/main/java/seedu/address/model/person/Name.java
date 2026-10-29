package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names must start with a letter or number and contain only letters, numbers, spaces, "
            + "apostrophes, hyphens or full stops. The markers s/o, d/o, a/l and a/p may appear between "
            + "name parts";

    /**
     * Supports international letters and combining accents while excluding CLI prefix separators.
     * Requiring a letter or number first also rejects blank or punctuation-only names.
     */
    private static final String NAME_PART = "[\\p{L}\\p{N}][\\p{L}\\p{M}\\p{N} .'\u2019-]*";

    /**
     * Allows {@code /} only within the standalone relationship markers s/o, d/o, a/l and a/p (ASCII, any case),
     * so other slashes, such as mistyped prefixes, are still rejected.
     */
    public static final String VALIDATION_REGEX = NAME_PART + "(?: (?i:[sd]/o|a/[lp]) +" + NAME_PART + ")*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name otherName)) {
            return false;
        }

        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
