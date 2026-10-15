package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Tests status and instrument criteria, with OR within each field and AND between fields.
 */
public class PersonMatchesFilterPredicate implements Predicate<Person> {
    private final Set<Status> statuses;
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
        this(Set.of(), instrumentNames);
    }

    /**
     * Creates a predicate with OR within each field and AND between fields.
     * An empty set leaves its field unrestricted.
     */
    public PersonMatchesFilterPredicate(Set<Status> statuses, Set<String> instrumentNames) {
        this.statuses = Set.copyOf(requireNonNull(statuses));
        this.instrumentNames = Set.copyOf(requireNonNull(instrumentNames));
    }

    @Override
    public boolean test(Person person) {
        boolean matchesStatus = statuses.isEmpty() || statuses.contains(person.getStatus());
        boolean matchesInstrument = instrumentNames.isEmpty()
                || instrumentNames.stream().anyMatch(instrumentName -> person.getInstruments().stream()
                    .anyMatch(instrument -> instrument.hasName(instrumentName)));
        return matchesStatus && matchesInstrument;
    }
}
