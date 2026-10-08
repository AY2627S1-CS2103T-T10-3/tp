package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a person's membership status in HiveMind.
 */
public enum Status {
    MEMBER("member"),
    ACTIVE_ALUMNUS("active-alumnus"),
    INACTIVE_ALUMNUS("inactive-alumnus");

    public static final String MESSAGE_CONSTRAINTS =
            "Status should be member, active-alumnus, or inactive-alumnus";

    private final String keyword;

    Status(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns true if the given string names a membership status, ignoring case and surrounding whitespace.
     */
    public static boolean isValidStatus(String test) {
        requireNonNull(test);
        for (Status status : values()) {
            if (status.keyword.equalsIgnoreCase(test.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the membership status named by the given string, ignoring case and surrounding whitespace.
     *
     * @throws IllegalArgumentException if the string does not name a membership status
     */
    public static Status fromString(String value) {
        requireNonNull(value);
        for (Status status : values()) {
            if (status.keyword.equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
    }

    @Override
    public String toString() {
        return keyword;
    }
}
