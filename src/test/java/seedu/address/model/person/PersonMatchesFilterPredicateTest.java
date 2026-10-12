package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesFilterPredicateTest {

    @Test
    public void test_multipleInstruments_matchesEitherInstrument() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(Set.of("Violin", "Cello"));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("VIOLIN:3").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Cello:1").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Violin:2", "Cello:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Flute:5").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments().build()));
    }

    @Test
    public void test_matchingInstrument_returnsTrue() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate("violin");
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Violin:0").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Cello:2", "VIOLIN:7").build()));

        // extra whitespace inside the name is ignored
        predicate = new PersonMatchesFilterPredicate("bass   guitar");
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Bass Guitar:3").build()));
    }

    @Test
    public void test_nonMatchingInstrument_returnsFalse() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate("Guitar");
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Piano:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Bass Guitar:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments().build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Guitar").withInstruments("Piano:1").build()));
    }
}
