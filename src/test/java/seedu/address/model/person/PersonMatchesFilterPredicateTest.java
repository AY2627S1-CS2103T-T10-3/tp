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

    @Test
    public void test_statusOnly_matchesSelectedStatusesWithOrWithoutInstruments() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withStatus("member").withInstruments().build()));
        assertTrue(predicate.test(new PersonBuilder().withStatus("active-alumnus")
                .withInstruments("Violin:3").build()));
        assertFalse(predicate.test(new PersonBuilder().withStatus("inactive-alumnus").build()));
    }

    @Test
    public void test_inactiveAlumnus_doesNotMatchActiveAlumnus() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.INACTIVE_ALUMNUS), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withStatus("inactive-alumnus").build()));
        assertFalse(predicate.test(new PersonBuilder().withStatus("active-alumnus").build()));
        assertFalse(predicate.test(new PersonBuilder().withStatus("member").build()));
    }

    @Test
    public void test_statusesAndInstruments_requiresBothGroupsWithOrWithinEachGroup() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS), Set.of("Violin", "Cello"));
        for (String status : new String[] {"member", "active-alumnus"}) {
            assertTrue(predicate.test(new PersonBuilder().withStatus(status).withInstruments("VIOLIN:0").build()));
            assertTrue(predicate.test(new PersonBuilder().withStatus(status).withInstruments("Cello:4").build()));
            assertFalse(predicate.test(new PersonBuilder().withStatus(status).withInstruments("Piano:5").build()));
            assertFalse(predicate.test(new PersonBuilder().withStatus(status).withInstruments().build()));
        }
        assertFalse(predicate.test(new PersonBuilder().withStatus("inactive-alumnus")
                .withInstruments("Violin:3", "Cello:2").build()));
    }
}
