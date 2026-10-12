package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Tests whether a person plays any of the specified instruments, matching whole names and ignoring case.
 */
public class PersonMatchesFilterPredicate implements Predicate<Person> {
    private final Set<String> instrumentNames;

    /**
     * Creates a predicate matching the specified instrument name.
     */
    public PersonMatchesFilterPredicate(String instrumentName) {
        this(Set.of(requireNonNull(instrumentName)));
    }

    /**
     * Creates a predicate matching any of the specified instrument names.
     */
    public PersonMatchesFilterPredicate(Set<String> instrumentNames) {
        this.instrumentNames = Set.copyOf(requireNonNull(instrumentNames));
    }

    @Override
    public boolean test(Person person) {
        return instrumentNames.stream().anyMatch(instrumentName -> person.getInstruments().stream()
                .anyMatch(instrument -> instrument.hasName(instrumentName)));
    }
}
