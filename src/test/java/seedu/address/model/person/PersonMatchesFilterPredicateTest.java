package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonMatchesFilterPredicateTest {

    @Test
    public void test_multipleTags_matchesEitherTag() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(new Tag("friend"), new Tag("colleague")));
        assertTrue(predicate.test(new PersonBuilder().withTags("FRIEND").build()));
        assertTrue(predicate.test(new PersonBuilder().withTags("colleague").build()));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "colleague").build()));
        assertFalse(predicate.test(new PersonBuilder().withTags("family").build()));
        assertFalse(predicate.test(new PersonBuilder().withTags().build()));
    }

    @Test
    public void test_matchingTag_returnsTrue() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(new Tag("friend"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));
        assertTrue(predicate.test(new PersonBuilder().withTags("colleague", "FRIEND").build()));
    }

    @Test
    public void test_nonMatchingTag_returnsFalse() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(new Tag("friend"));
        assertFalse(predicate.test(new PersonBuilder().withTags("colleague").build()));
        assertFalse(predicate.test(new PersonBuilder().withTags("friends").build()));
        assertFalse(predicate.test(new PersonBuilder().withTags().build()));
        assertFalse(predicate.test(new PersonBuilder().withName("friend").withTags("colleague").build()));
    }
}
