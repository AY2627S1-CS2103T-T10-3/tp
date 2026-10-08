package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonMatchesFilterPredicateTest {

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
