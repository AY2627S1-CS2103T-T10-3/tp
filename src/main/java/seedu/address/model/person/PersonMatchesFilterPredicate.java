package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.model.tag.Tag;

/**
 * Tests whether a person has the specified tag, ignoring case.
 */
public class PersonMatchesFilterPredicate implements Predicate<Person> {
    private final Tag tag;

    /**
     * Creates a predicate matching the specified tag.
     */
    public PersonMatchesFilterPredicate(Tag tag) {
        this.tag = requireNonNull(tag);
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().stream()
                .anyMatch(personTag -> personTag.tagName.equalsIgnoreCase(tag.tagName));
    }
}
