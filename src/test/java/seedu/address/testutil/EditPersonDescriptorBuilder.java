package seedu.address.testutil;

import java.util.Optional;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.MemberType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.Status;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setEmail(person.getEmail());
        descriptor.setStatus(person.getStatus());
        descriptor.setMemberType(person.getMemberType());
        descriptor.setRole(person.getRole());
        descriptor.setInstruments(person.getInstruments());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Status} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withStatus(String status) {
        descriptor.setStatus(Status.fromString(status));
        return this;
    }

    /**
     * Sets the {@code MemberType} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withMemberType(String memberType) {
        descriptor.setMemberType(MemberType.fromString(memberType));
        return this;
    }

    /**
     * Sets the {@code Role} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRole(String role) {
        descriptor.setRole(Optional.of(new Role(role)));
        return this;
    }

    /**
     * Sets the {@code EditPersonDescriptor} that we are building to clear the role.
     */
    public EditPersonDescriptorBuilder withoutRole() {
        descriptor.setRole(Optional.empty());
        return this;
    }

    /**
     * Parses the {@code NAME:YEARS} {@code instruments} into a {@code Set<Instrument>} and sets it to the
     * {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withInstruments(String... instruments) {
        descriptor.setInstruments(SampleDataUtil.getInstrumentSet(instruments));
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
