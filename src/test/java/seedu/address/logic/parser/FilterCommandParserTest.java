package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.Instrument;
import seedu.address.model.person.InstrumentCriterion;
import seedu.address.model.person.MemberType;
import seedu.address.model.person.Role;
import seedu.address.model.person.Status;

public class FilterCommandParserTest {
    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_validInstrument_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(Set.of(),
                Set.of(new InstrumentCriterion("Violin", 0)), Set.of(), Set.of());
        assertParseSuccess(parser, "i/Violin", expectedCommand);
        assertParseSuccess(parser, "  i/  Violin  ", expectedCommand);
        assertParseSuccess(parser, "i/Bass Guitar",
                new FilterCommand(Set.of(), Set.of(new InstrumentCriterion("Bass Guitar", 0)), Set.of(), Set.of()));
    }

    @Test
    public void parse_missingCriteria_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);
        assertParseFailure(parser, "Violin", expectedMessage);
    }

    @Test
    public void parse_tagPrefix_throwsParseException() {
        assertParseFailure(parser, "t/friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unsupportedPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "s/member n/Alice", expectedMessage);
        assertParseFailure(parser, "ty/student s/member n/Alice", expectedMessage);
        assertParseFailure(parser, "i/Violin n/Alice", expectedMessage);
        assertParseFailure(parser, "i/Violin ty/teacher r/Conductor n/Alice", expectedMessage);
        assertParseFailure(parser, "i/Violin p/91234567 e/alice@example.com", expectedMessage);
    }

    @Test
    public void parse_unexpectedPreamble_throwsParseException() {
        assertParseFailure(parser, "1 i/Violin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidInstrument_throwsParseException() {
        assertParseFailure(parser, "i/", Instrument.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "i/:3", Instrument.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "i/Violin i/", Instrument.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "i/:3 i/Cello", Instrument.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_multipleInstruments_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(Set.of(),
                Set.of(new InstrumentCriterion("Violin", 0), new InstrumentCriterion("Cello", 0)), Set.of(), Set.of());
        assertParseSuccess(parser, "i/Violin i/Cello", expectedCommand);
        assertParseSuccess(parser, "i/ Cello i/ Violin i/Violin", expectedCommand);
    }

    @Test
    public void parse_validStatus_returnsFilterCommand() {
        for (Status status : Status.values()) {
            FilterCommand expectedCommand = new FilterCommand(Set.of(status), Set.of(), Set.of(), Set.of());
            assertParseSuccess(parser, "s/" + status, expectedCommand);
        }
        assertParseSuccess(parser, "  s/  ACTIVE-ALUMNUS  ",
                new FilterCommand(Set.of(Status.ACTIVE_ALUMNUS), Set.of(), Set.of(), Set.of()));
    }

    @Test
    public void parse_multipleStatuses_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS), Set.of(), Set.of(), Set.of());
        assertParseSuccess(parser, "s/member s/active-alumnus", expectedCommand);
        assertParseSuccess(parser, "s/ACTIVE-ALUMNUS s/member s/MEMBER", expectedCommand);
    }

    @Test
    public void parse_statusesAndInstruments_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(
                Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS),
                Set.of(new InstrumentCriterion("Violin", 0), new InstrumentCriterion("Cello", 0)), Set.of(), Set.of());
        assertParseSuccess(parser, "s/member i/Violin s/active-alumnus i/Cello", expectedCommand);
        assertParseSuccess(parser, "i/Cello s/active-alumnus i/Violin s/member", expectedCommand);
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        assertParseFailure(parser, "s/", Status.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "s/alumnus", Status.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "s/student", Status.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "s/member active-alumnus", Status.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "s/member s/", Status.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "s/unknown s/member", Status.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "i/Violin s/unknown", Status.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_statusWithInvalidInstrument_throwsParseException() {
        assertParseFailure(parser, "s/member i/", Instrument.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "s/member i/:3", Instrument.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_statusWithUnexpectedPreamble_throwsParseException() {
        assertParseFailure(parser, "1 s/member",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_instrumentWithMinimumYears_returnsFilterCommand() {
        for (int years : new int[] {0, 3, Instrument.MAX_YEARS}) {
            FilterCommand expectedCommand = new FilterCommand(Set.of(),
                    Set.of(new InstrumentCriterion("Violin", years)), Set.of(), Set.of());
            assertParseSuccess(parser, "i/Violin:" + years, expectedCommand);
        }
        assertParseSuccess(parser, "  i/  BASS   guitar : 3  ",
                new FilterCommand(Set.of(), Set.of(new InstrumentCriterion("Bass Guitar", 3)), Set.of(), Set.of()));
        assertParseSuccess(parser, "i/Violin i/VIOLIN:0",
                new FilterCommand(Set.of(), Set.of(new InstrumentCriterion("Violin", 0)), Set.of(), Set.of()));
    }

    @Test
    public void parse_mixedInstrumentCriteriaAndStatuses_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(Set.of(Status.MEMBER, Status.ACTIVE_ALUMNUS),
                Set.of(new InstrumentCriterion("Violin", 3), new InstrumentCriterion("Cello", 0)), Set.of(), Set.of());
        assertParseSuccess(parser, "s/member i/Violin:3 s/active-alumnus i/Cello", expectedCommand);
        assertParseSuccess(parser, "i/Cello s/ACTIVE-ALUMNUS i/violin:3 s/member", expectedCommand);
    }

    @Test
    public void parse_repeatedInstrumentWithDifferentMinimumYears_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(Set.of(),
                Set.of(new InstrumentCriterion("Violin", 3), new InstrumentCriterion("Violin", 5)), Set.of(), Set.of());
        assertParseSuccess(parser, "i/Violin:3 i/violin:5 i/VIOLIN:3", expectedCommand);
    }

    @Test
    public void parse_invalidMinimumYears_throwsParseException() {
        for (String years : new String[] {"", "-1", "100", "1.5", "3.0", "abc", "+3", "1e1",
            "999999999999999999999", "3:4"}) {
            assertParseFailure(parser, "i/Violin:" + years, InstrumentCriterion.MESSAGE_MINIMUM_YEARS_CONSTRAINTS);
            assertParseFailure(parser, "s/member i/Violin:" + years + " i/Cello",
                    InstrumentCriterion.MESSAGE_MINIMUM_YEARS_CONSTRAINTS);
        }
    }

    @Test
    public void parse_memberTypes_returnsFilterCommand() {
        assertParseSuccess(parser, "ty/ TEACHER ",
                new FilterCommand(Set.of(), Set.of(), Set.of(MemberType.TEACHER), Set.of()));
        assertParseSuccess(parser, "ty/student ty/teacher ty/STUDENT",
                new FilterCommand(Set.of(), Set.of(), Set.of(MemberType.STUDENT, MemberType.TEACHER), Set.of()));
    }

    @Test
    public void parse_roles_returnsFilterCommand() {
        assertParseSuccess(parser, "r/ Section Leader ",
                new FilterCommand(Set.of(), Set.of(), Set.of(), Set.of(new Role("Section Leader"))));
        assertParseSuccess(parser, "r/Conductor r/Section Leader r/Conductor",
                new FilterCommand(Set.of(), Set.of(), Set.of(),
                        Set.of(new Role("Conductor"), new Role("Section Leader"))));
    }

    @Test
    public void parse_allFields_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(Set.of(Status.MEMBER),
                Set.of(new InstrumentCriterion("Violin", 3)), Set.of(MemberType.TEACHER),
                Set.of(new Role("Conductor")));
        assertParseSuccess(parser, "ty/teacher r/Conductor s/member i/Violin:3", expected);
        assertParseSuccess(parser, "i/Violin:3 s/member r/Conductor ty/teacher", expected);
    }

    @Test
    public void parse_invalidMemberTypeOrRole_throwsParseException() {
        for (String value : new String[] {"", "member", "teacher ty/"}) {
            assertParseFailure(parser, "ty/" + value, MemberType.MESSAGE_CONSTRAINTS);
        }
        assertParseFailure(parser, "r/ ", Role.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "r/Conductor r/", Role.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "ty/teacher r/", Role.MESSAGE_CONSTRAINTS);
    }
}
