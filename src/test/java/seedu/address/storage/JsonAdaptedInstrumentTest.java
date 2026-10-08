package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedInstrument.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.person.Instrument;

public class JsonAdaptedInstrumentTest {
    private static final String VALID_NAME = "Trumpet";
    private static final int VALID_YEARS = 3;

    @Test
    public void toModelType_validInstrumentDetails_returnsInstrument() throws Exception {
        Instrument instrument = new Instrument(VALID_NAME, VALID_YEARS);
        assertEquals(instrument, new JsonAdaptedInstrument(instrument).toModelType());
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedInstrument instrument = new JsonAdaptedInstrument(null, VALID_YEARS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "name");
        assertThrows(IllegalValueException.class, expectedMessage, instrument::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedInstrument instrument = new JsonAdaptedInstrument(" ", VALID_YEARS);
        assertThrows(IllegalValueException.class, Instrument.MESSAGE_NAME_CONSTRAINTS, instrument::toModelType);
    }

    @Test
    public void toModelType_nullYears_throwsIllegalValueException() {
        JsonAdaptedInstrument instrument = new JsonAdaptedInstrument(VALID_NAME, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "years");
        assertThrows(IllegalValueException.class, expectedMessage, instrument::toModelType);
    }

    @Test
    public void toModelType_invalidYears_throwsIllegalValueException() {
        JsonAdaptedInstrument instrument = new JsonAdaptedInstrument(VALID_NAME, -1);
        assertThrows(IllegalValueException.class, Instrument.MESSAGE_YEARS_CONSTRAINTS, instrument::toModelType);
    }

    @Test
    public void toJsonString_validInstrument_usesNameAndYearsKeys() throws Exception {
        String json = JsonUtil.toJsonString(new JsonAdaptedInstrument(new Instrument(VALID_NAME, VALID_YEARS)));
        assertEquals(new Instrument(VALID_NAME, VALID_YEARS),
                JsonUtil.fromJsonString(json, JsonAdaptedInstrument.class).toModelType());
        assertEquals("{\n  \"name\" : \"Trumpet\",\n  \"years\" : 3\n}",
                json.replace("\r\n", "\n"));
    }
}
