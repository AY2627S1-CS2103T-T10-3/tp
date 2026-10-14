package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Instrument;
import seedu.address.model.person.MemberType;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsDistinctPeopleWithRepresentativeFields() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(7, samplePersons.length);
        for (int i = 0; i < samplePersons.length; i++) {
            for (int j = i + 1; j < samplePersons.length; j++) {
                assertFalse(samplePersons[i].isSamePerson(samplePersons[j]));
            }
        }

        List<Person> people = List.of(samplePersons);
        assertTrue(people.stream().anyMatch(person -> person.getStatus() == Status.MEMBER));
        assertTrue(people.stream().anyMatch(person -> person.getStatus() == Status.ACTIVE_ALUMNUS));
        assertTrue(people.stream().anyMatch(person -> person.getStatus() == Status.INACTIVE_ALUMNUS));
        assertTrue(people.stream().anyMatch(person -> person.getMemberType() == MemberType.STUDENT));
        assertTrue(people.stream().anyMatch(person -> person.getMemberType() == MemberType.TEACHER));
        assertTrue(people.stream().anyMatch(person -> person.getRole().isPresent()));
        assertTrue(people.stream().anyMatch(person -> person.getRole().isEmpty()));
        assertTrue(people.stream().anyMatch(person -> person.getInstruments().isEmpty()));
        assertTrue(people.stream().anyMatch(person -> person.getInstruments().size() > 1));
    }

    @Test
    public void getSampleAddressBook_containsAllSamplePersons() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();
        assertEquals(List.of(SampleDataUtil.getSamplePersons()), sampleAddressBook.getPersonList());
    }

    @Test
    public void getInstrumentSet_parsesNameAndYears() {
        assertEquals(Set.of(new Instrument("Violin", 6), new Instrument("Bass Guitar", 1)),
                SampleDataUtil.getInstrumentSet("Violin:6", "Bass Guitar:1"));
        assertEquals(Set.of(), SampleDataUtil.getInstrumentSet());
    }
}
