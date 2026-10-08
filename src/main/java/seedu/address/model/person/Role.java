package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a person's free-text role in HiveMind.
 * Guarantees: immutable; is valid as declared in {@link #isValidRole(String)}.
 */
public class Role {

    public static final String MESSAGE_CONSTRAINTS = "Roles should not be blank";

    public final String value;

    /**
     * Constructs a {@code Role}.
     *
     * @param role A non-blank role.
     */
    public Role(String role) {
        requireNonNull(role);
        checkArgument(isValidRole(role), MESSAGE_CONSTRAINTS);
        value = role;
    }

    /**
     * Returns true if the given string contains non-whitespace characters.
     */
    public static boolean isValidRole(String test) {
        requireNonNull(test);
        return !test.isBlank();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Role otherRole)) {
            return false;
        }
        return value.equals(otherRole.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
