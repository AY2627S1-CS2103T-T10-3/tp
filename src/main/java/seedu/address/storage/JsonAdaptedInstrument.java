package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Instrument;

/**
 * Jackson-friendly version of {@link Instrument}.
 */
class JsonAdaptedInstrument {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Instrument's %s field is missing!";

    private final String name;
    private final Integer years;

    /**
     * Constructs a {@code JsonAdaptedInstrument} with the given instrument details.
     */
    @JsonCreator
    public JsonAdaptedInstrument(@JsonProperty("name") String name, @JsonProperty("years") Integer years) {
        this.name = name;
        this.years = years;
    }

    /**
     * Converts a given {@code Instrument} into this class for Jackson use.
     */
    public JsonAdaptedInstrument(Instrument source) {
        name = source.getName();
        years = source.getYears();
    }

    /**
     * Converts this Jackson-friendly adapted instrument object into the model's {@code Instrument} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted instrument.
     */
    public Instrument toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "name"));
        }
        if (!Instrument.isValidName(name)) {
            throw new IllegalValueException(Instrument.MESSAGE_NAME_CONSTRAINTS);
        }

        if (years == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "years"));
        }
        if (!Instrument.isValidYears(years)) {
            throw new IllegalValueException(Instrument.MESSAGE_YEARS_CONSTRAINTS);
        }

        return new Instrument(name, years);
    }
}
