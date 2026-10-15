package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Instrument;
import seedu.address.model.person.Person;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsDistinctPeopleWithInstruments() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertTrue(samplePersons.length > 0);
        for (int i = 0; i < samplePersons.length; i++) {
            for (int j = i + 1; j < samplePersons.length; j++) {
                assertFalse(samplePersons[i].isSamePerson(samplePersons[j]));
            }
        }
        assertTrue(List.of(samplePersons).stream().anyMatch(person -> !person.getInstruments().isEmpty()));
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
