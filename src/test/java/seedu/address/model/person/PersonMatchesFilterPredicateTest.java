package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesFilterPredicateTest {

    @Test
    public void test_groups_matchesEitherWholeGroupIgnoringCaseAndWhitespace() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(Set.of(), Set.of(),
                Set.of(), Set.of(), Set.of(new Group("Chamber Orchestra"), new Group("Training")));
        assertTrue(predicate.test(new PersonBuilder().withGroups("CHAMBER   orchestra").build()));
        assertTrue(predicate.test(new PersonBuilder().withGroups("Training", "Quartet").build()));
        assertTrue(predicate.test(new PersonBuilder().withGroups("Chamber Orchestra", "Training").build()));
        assertTrue(predicate.test(new PersonBuilder().withStatus("inactive-alumnus").withMemberType("teacher")
                .withoutRole().withInstruments().withGroups("Training").build()));
        assertFalse(predicate.test(new PersonBuilder().withGroups("Orchestra").build()));
        assertFalse(predicate.test(new PersonBuilder().withGroups("Junior Chamber Orchestra").build()));
        assertFalse(predicate.test(new PersonBuilder().withGroups("Quartet").build()));
        assertFalse(predicate.test(new PersonBuilder().withGroups().build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Training").withRole("Training")
                .withGroups().build()));
    }

    @Test
    public void test_groupsWithOtherFields_requiresEverySpecifiedField() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS),
                Set.of(new InstrumentCriterion("Violin", 3), new InstrumentCriterion("Cello", 5)),
                Set.of(MemberType.TEACHER), Set.of(new Role("Conductor")),
                Set.of(new Group("Orchestra"), new Group("Training")));
        Person matching = new PersonBuilder().withStatus("member").withInstruments("Violin:3")
                .withMemberType("teacher").withRole("Conductor").withGroups("Orchestra").build();
        assertTrue(predicate.test(matching));
        assertTrue(predicate.test(new PersonBuilder(matching).withStatus("active-alumnus")
                .withInstruments("Cello:5").withGroups("Training").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withStatus("inactive-alumnus").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withInstruments("Violin:2", "Piano:10").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withMemberType("student").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withoutRole().build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withGroups("Quartet").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withGroups().build()));
    }

    @Test
    public void test_omittedGroups_doesNotRestrictMatches() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER), Set.of(), Set.of(), Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withGroups("Orchestra").build()));
        assertTrue(predicate.test(new PersonBuilder().withGroups().build()));
    }

    @Test
    public void test_mutatingOriginalGroups_doesNotChangePredicate() {
        Set<Group> groups = new HashSet<>(Set.of(new Group("Orchestra")));
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(), Set.of(), Set.of(), groups);
        groups.clear();
        groups.add(new Group("Training"));
        assertTrue(predicate.test(new PersonBuilder().withGroups("Orchestra").build()));
        assertFalse(predicate.test(new PersonBuilder().withGroups("Training").build()));
    }

    @Test
    public void test_multipleInstruments_matchesEitherInstrument() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(new InstrumentCriterion("Violin", 0), new InstrumentCriterion("Cello", 0)),
                Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withInstruments("VIOLIN:3").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Cello:1").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Violin:2", "Cello:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Flute:5").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments().build()));
    }

    @Test
    public void test_matchingInstrument_returnsTrue() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(new InstrumentCriterion("violin", 0)), Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Violin:0").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Cello:2", "VIOLIN:7").build()));

        // extra whitespace inside the name is ignored
        predicate = new PersonMatchesFilterPredicate(Set.of(),
                Set.of(new InstrumentCriterion("bass   guitar", 0)), Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Bass Guitar:3").build()));
    }

    @Test
    public void test_nonMatchingInstrument_returnsFalse() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(new InstrumentCriterion("Guitar", 0)), Set.of(), Set.of());
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Piano:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Bass Guitar:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments().build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Guitar").withInstruments("Piano:1").build()));
    }

    @Test
    public void test_statusOnly_matchesSelectedStatusesWithOrWithoutInstruments() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS), Set.of(), Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withStatus("member").withInstruments().build()));
        assertTrue(predicate.test(new PersonBuilder().withStatus("active-alumnus")
                .withInstruments("Violin:3").build()));
        assertFalse(predicate.test(new PersonBuilder().withStatus("inactive-alumnus").build()));
    }

    @Test
    public void test_inactiveAlumnus_doesNotMatchActiveAlumnus() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.INACTIVE_ALUMNUS), Set.of(), Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withStatus("inactive-alumnus").build()));
        assertFalse(predicate.test(new PersonBuilder().withStatus("active-alumnus").build()));
        assertFalse(predicate.test(new PersonBuilder().withStatus("member").build()));
    }

    @Test
    public void test_statusesAndInstruments_requiresBothGroupsWithOrWithinEachGroup() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS),
                Set.of(new InstrumentCriterion("Violin", 0), new InstrumentCriterion("Cello", 0)), Set.of(), Set.of());
        for (String status : new String[] {"member", "active-alumnus"}) {
            assertTrue(predicate.test(new PersonBuilder().withStatus(status).withInstruments("VIOLIN:0").build()));
            assertTrue(predicate.test(new PersonBuilder().withStatus(status).withInstruments("Cello:4").build()));
            assertFalse(predicate.test(new PersonBuilder().withStatus(status).withInstruments("Piano:5").build()));
            assertFalse(predicate.test(new PersonBuilder().withStatus(status).withInstruments().build()));
        }
        assertFalse(predicate.test(new PersonBuilder().withStatus("inactive-alumnus")
                .withInstruments("Violin:3", "Cello:2").build()));
    }

    @Test
    public void test_minimumYears_matchesThresholdAndHigherOnSameInstrument() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(new InstrumentCriterion("Guitar", 3)), Set.of(), Set.of());
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Guitar:2").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("GUITAR:3").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Guitar:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Guitar:2", "Piano:20").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Bass Guitar:20").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments().build()));
    }

    @Test
    public void test_maximumYears_matchesOnlyMaximumExperience() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(new InstrumentCriterion("Violin", Instrument.MAX_YEARS)), Set.of(), Set.of());
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Violin:98").build()));
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Violin:99").build()));
    }

    @Test
    public void test_minimumYearsWithStatuses_preservesOrWithinGroupsAndAndBetweenGroups() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS),
                Set.of(new InstrumentCriterion("Violin", 3), new InstrumentCriterion("Cello", 5)), Set.of(), Set.of());
        for (String status : new String[] {"member", "active-alumnus"}) {
            assertTrue(predicate.test(new PersonBuilder().withStatus(status).withInstruments("Violin:3").build()));
            assertTrue(predicate.test(new PersonBuilder().withStatus(status).withInstruments("Cello:5").build()));
            assertFalse(predicate.test(new PersonBuilder().withStatus(status)
                    .withInstruments("Violin:2", "Cello:4").build()));
            assertFalse(predicate.test(new PersonBuilder().withStatus(status).withInstruments().build()));
        }
        assertFalse(predicate.test(new PersonBuilder().withStatus("inactive-alumnus")
                .withInstruments("Violin:20", "Cello:20").build()));
    }

    @Test
    public void test_repeatedInstrumentWithDifferentMinimumYears_matchesEitherThreshold() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(new InstrumentCriterion("Violin", 3), new InstrumentCriterion("Violin", 5)),
                Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Violin:4").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Violin:2").build()));
    }

    @Test
    public void test_mixedInstrumentCriteria_matchesInstrumentWithoutMinimumYears() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(new InstrumentCriterion("Violin", 3), new InstrumentCriterion("Cello", 0)),
                Set.of(), Set.of());
        assertTrue(predicate.test(new PersonBuilder().withInstruments("Cello:0").build()));
        assertFalse(predicate.test(new PersonBuilder().withInstruments("Violin:2").build()));
    }

    @Test
    public void test_memberTypes_matchesEitherTypeWithoutRequiringRoleOrInstruments() {
        PersonMatchesFilterPredicate teacherOnly = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(), Set.of(MemberType.TEACHER), Set.of());
        Person student = new PersonBuilder().withMemberType("student").withoutRole().withInstruments().build();
        Person teacher = new PersonBuilder(student).withMemberType("teacher").build();
        assertTrue(teacherOnly.test(teacher));
        assertFalse(teacherOnly.test(student));

        PersonMatchesFilterPredicate bothTypes = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(), Set.of(MemberType.STUDENT, MemberType.TEACHER), Set.of());
        assertTrue(bothTypes.test(student));
        assertTrue(bothTypes.test(teacher));
    }

    @Test
    public void test_roles_matchesEitherWholeRoleIgnoringCaseAndSurroundingWhitespace() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(), Set.of(), Set.of(), Set.of(new Role("Conductor"), new Role("Section Leader")));
        assertTrue(predicate.test(new PersonBuilder().withRole(" conductor ").build()));
        assertTrue(predicate.test(new PersonBuilder().withRole("SECTION LEADER").build()));
        assertFalse(predicate.test(new PersonBuilder().withRole("Leader").build()));
        assertFalse(predicate.test(new PersonBuilder().withRole("Assistant Conductor").build()));
        assertFalse(predicate.test(new PersonBuilder().withoutRole().build()));
    }

    @Test
    public void test_allFields_requiresEverySpecifiedField() {
        PersonMatchesFilterPredicate predicate = new PersonMatchesFilterPredicate(
                Set.of(Status.MEMBER), Set.of(new InstrumentCriterion("Violin", 3)),
                Set.of(MemberType.TEACHER), Set.of(new Role("Conductor")));
        Person matching = new PersonBuilder().withStatus("member").withInstruments("Violin:3")
                .withMemberType("teacher").withRole("Conductor").build();
        assertTrue(predicate.test(matching));
        assertFalse(predicate.test(new PersonBuilder(matching).withStatus("inactive-alumnus").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withInstruments("Violin:2", "Piano:10").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withMemberType("student").build()));
        assertFalse(predicate.test(new PersonBuilder(matching).withoutRole().build()));
    }
}
