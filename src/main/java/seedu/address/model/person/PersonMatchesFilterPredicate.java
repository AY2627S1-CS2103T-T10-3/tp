package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Tests member criteria, with OR within each field and AND between fields.
 */
public class PersonMatchesFilterPredicate implements Predicate<Person> {
    private final Set<Status> statuses;
    private final Set<InstrumentCriterion> instrumentCriteria;
    private final Set<MemberType> memberTypes;
    private final Set<Role> roles;

    /**
     * Creates a predicate with optional status, instrument, member type, and role criteria.
     * An empty set leaves its field unrestricted. Roles match the whole name, ignoring case and surrounding whitespace.
     */
    public PersonMatchesFilterPredicate(Set<Status> statuses, Set<InstrumentCriterion> instrumentCriteria,
            Set<MemberType> memberTypes, Set<Role> roles) {
        this.statuses = Set.copyOf(requireNonNull(statuses));
        this.instrumentCriteria = Set.copyOf(requireNonNull(instrumentCriteria));
        this.memberTypes = Set.copyOf(requireNonNull(memberTypes));
        this.roles = Set.copyOf(requireNonNull(roles));
    }

    @Override
    public boolean test(Person person) {
        boolean matchesStatus = statuses.isEmpty() || statuses.contains(person.getStatus());
        boolean matchesInstrument = instrumentCriteria.isEmpty() || instrumentCriteria.stream()
                .anyMatch(criterion -> person.getInstruments().stream()
                        .anyMatch(criterion::matches));
        boolean matchesMemberType = memberTypes.isEmpty() || memberTypes.contains(person.getMemberType());
        boolean matchesRole = roles.isEmpty() || person.getRole()
                .map(role -> roles.stream().anyMatch(criterion -> criterion.value.trim()
                        .equalsIgnoreCase(role.value.trim())))
                .orElse(false);
        return matchesStatus && matchesInstrument && matchesMemberType && matchesRole;
    }
}
