package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents an instrument a person plays, with the number of years they have played it.
 * Guarantees: immutable; name is valid as declared in {@link #isValidName(String)};
 * years is valid as declared in {@link #isValidYears(int)}.
 */
public class Instrument {

    public static final int MAX_YEARS = 99;

    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Instrument names should not be blank and should not contain ':'";

    public static final String MESSAGE_YEARS_CONSTRAINTS =
            "Years played should be a whole number from 0 to " + MAX_YEARS;

    private final String name;
    private final int years;

    /**
     * Constructs an {@code Instrument}.
     * Surrounding whitespace in the name is removed and inner whitespace is collapsed to single spaces.
     *
     * @param name A valid instrument name.
     * @param years A valid number of years played.
     */
    public Instrument(String name, int years) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_NAME_CONSTRAINTS);
        checkArgument(isValidYears(years), MESSAGE_YEARS_CONSTRAINTS);
        this.name = normalizeName(name);
        this.years = years;
    }

    /**
     * Returns true if the given string is a valid instrument name.
     */
    public static boolean isValidName(String test) {
        requireNonNull(test);
        return !test.isBlank() && !test.contains(":");
    }

    /**
     * Returns true if the given number is a valid number of years played.
     */
    public static boolean isValidYears(int test) {
        return test >= 0 && test <= MAX_YEARS;
    }

    public String getName() {
        return name;
    }

    public int getYears() {
        return years;
    }

    /**
     * Returns true if this instrument has the given name, ignoring case and extra whitespace.
     */
    public boolean hasName(String otherName) {
        requireNonNull(otherName);
        return toKey(name).equals(toKey(normalizeName(otherName)));
    }

    private static String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }

    private static String toKey(String normalizedName) {
        return normalizedName.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Instrument otherInstrument)) {
            return false;
        }

        return hasName(otherInstrument.name) && years == otherInstrument.years;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toKey(name), years);
    }

    /**
     * Formats the instrument in the same {@code NAME:YEARS} form that users type.
     */
    @Override
    public String toString() {
        return name + ":" + years;
    }
}
