package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void format_personWithInstruments_listsInstrumentsAlphabetically() {
        String formatted = Messages.format(new PersonBuilder().withName("Amy Bee").withPhone("85355255")
                .withEmail("amy@gmail.com").withInstruments("violin:5", "Cello:2", "Bass Guitar:1").build());
        assertEquals("Amy Bee; Phone: 85355255; Email: amy@gmail.com; Status: member; Member type: student; "
                + "Instruments: Bass Guitar:1, Cello:2, violin:5", formatted);
    }

    @Test
    public void format_personWithoutInstruments_showsEmptyInstruments() {
        String formatted = Messages.format(new PersonBuilder().withName("Amy Bee").withPhone("85355255")
                .withEmail("amy@gmail.com").withRole("Conductor").build());
        assertEquals("Amy Bee; Phone: 85355255; Email: amy@gmail.com; Status: member; Member type: student; "
                + "Role: Conductor; Instruments: ", formatted);
    }

    @Test
    public void format_personWithGroups_listsGroupsAlphabetically() {
        String formatted = Messages.format(new PersonBuilder().withGroups("Training", "Chamber Orchestra").build());
        assertEquals("Amy Bee; Phone: 85355255; Email: amy@gmail.com; Status: member; Member type: student; "
                + "Instruments: ; Groups: Chamber Orchestra, Training", formatted);
    }
}
