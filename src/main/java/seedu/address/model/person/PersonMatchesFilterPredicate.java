package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.tag.Tag;

/**
 * Tests whether a person has any of the specified tags, ignoring case.
 */
public class PersonMatchesFilterPredicate implements Predicate<Person> {
    private final Set<Tag> tags;

    /**
     * Creates a predicate matching the specified tag.
     */
    public PersonMatchesFilterPredicate(Tag tag) {
        this(Set.of(requireNonNull(tag)));
    }

    /**
     * Creates a predicate matching any of the specified tags.
     */
    public PersonMatchesFilterPredicate(Set<Tag> tags) {
        this.tags = Set.copyOf(requireNonNull(tags));
    }

    @Override
    public boolean test(Person person) {
        return tags.stream().anyMatch(tag -> person.getTags().stream()
                .anyMatch(personTag -> personTag.tagName.equalsIgnoreCase(tag.tagName)));
    }
}
