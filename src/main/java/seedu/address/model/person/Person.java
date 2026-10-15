package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Status status;
    private final MemberType memberType;
    private final Optional<Role> role;
    private final Set<Instrument> instruments = new HashSet<>();

    /**
     * Every field must be present and not null, and no two instruments may share a name.
     */
    public Person(Name name, Phone phone, Email email, Status status, MemberType memberType,
            Optional<Role> role, Set<Instrument> instruments) {
        requireAllNonNull(name, phone, email, status, memberType, role, instruments);
        checkArgument(!Instrument.hasDuplicateNames(instruments), Instrument.MESSAGE_DUPLICATE_NAMES);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.memberType = memberType;
        this.role = role;
        this.instruments.addAll(instruments);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Status getStatus() {
        return status;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    /**
     * Returns the person's role, or an empty optional when no role is recorded.
     */
    public Optional<Role> getRole() {
        return role;
    }

    /**
     * Returns an immutable instrument set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Instrument> getInstruments() {
        return Collections.unmodifiableSet(instruments);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && status.equals(otherPerson.status)
                && memberType.equals(otherPerson.memberType)
                && role.equals(otherPerson.role)
                && instruments.equals(otherPerson.instruments);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, status, memberType, role, instruments);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("status", status)
                .add("memberType", memberType)
                .add("role", role)
                .add("instruments", instruments)
                .toString();
    }

}
