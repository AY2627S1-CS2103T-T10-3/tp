package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents whether a HiveMind member is a student or teacher.
 */
public enum MemberType {
    STUDENT("student"),
    TEACHER("teacher");

    public static final String MESSAGE_CONSTRAINTS = "Member type should be student or teacher";

    private final String keyword;

    MemberType(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns true if the given string names a member type, ignoring case and surrounding whitespace.
     */
    public static boolean isValidMemberType(String test) {
        requireNonNull(test);
        for (MemberType memberType : values()) {
            if (memberType.keyword.equalsIgnoreCase(test.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the member type named by the given string, ignoring case and surrounding whitespace.
     *
     * @throws IllegalArgumentException if the string does not name a member type
     */
    public static MemberType fromString(String value) {
        requireNonNull(value);
        for (MemberType memberType : values()) {
            if (memberType.keyword.equalsIgnoreCase(value.trim())) {
                return memberType;
            }
        }
        throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
    }

    @Override
    public String toString() {
        return keyword;
    }
}
