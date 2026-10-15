package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Tests status and instrument criteria, with OR within each field and AND between fields.
 */
public class PersonMatchesFilterPredicate implements Predicate<Person> {
    private final Set<Status> statuses;
    private final Set<Instrument> instrumentCriteria;

    /**
     * Creates a predicate matching an instrument name and its minimum years.
     */
    public PersonMatchesFilterPredicate(Instrument instrumentCriterion) {
        this(Set.of(requireNonNull(instrumentCriterion)));
    }

    /**
     * Creates a predicate matching any of the specified instrument criteria.
     */
    public PersonMatchesFilterPredicate(Set<Instrument> instrumentCriteria) {
        this(Set.of(), instrumentCriteria);
    }

    /**
     * Creates a predicate with OR within each field and AND between fields.
     * An empty set leaves its field unrestricted.
     * Each instrument criterion's years represent an inclusive minimum for that same instrument.
     */
    public PersonMatchesFilterPredicate(Set<Status> statuses, Set<Instrument> instrumentCriteria) {
        this.statuses = Set.copyOf(requireNonNull(statuses));
        this.instrumentCriteria = Set.copyOf(requireNonNull(instrumentCriteria));
    }

    @Override
    public boolean test(Person person) {
        boolean matchesStatus = statuses.isEmpty() || statuses.contains(person.getStatus());
        boolean matchesInstrument = instrumentCriteria.isEmpty()
                || instrumentCriteria.stream().anyMatch(criterion -> person.getInstruments().stream()
                    .anyMatch(instrument -> instrument.hasName(criterion.getName())
                            && instrument.getYears() >= criterion.getYears()));
        return matchesStatus && matchesInstrument;
    }
}
