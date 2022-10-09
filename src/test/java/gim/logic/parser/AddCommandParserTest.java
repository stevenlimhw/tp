package gim.logic.parser;

import static gim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static gim.logic.commands.CommandTestUtil.DATE_DESC;
import static gim.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static gim.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static gim.logic.commands.CommandTestUtil.INVALID_REP_DESC;
import static gim.logic.commands.CommandTestUtil.INVALID_SETS_DESC;
import static gim.logic.commands.CommandTestUtil.INVALID_WEIGHT_DESC;
import static gim.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static gim.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static gim.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static gim.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static gim.logic.commands.CommandTestUtil.REP_DESC_AMY;
import static gim.logic.commands.CommandTestUtil.REP_DESC_BOB;
import static gim.logic.commands.CommandTestUtil.SETS_DESC_AMY;
import static gim.logic.commands.CommandTestUtil.SETS_DESC_BOB;
import static gim.logic.commands.CommandTestUtil.VALID_DATE;
import static gim.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static gim.logic.commands.CommandTestUtil.VALID_REP_BOB;
import static gim.logic.commands.CommandTestUtil.VALID_SETS_BOB;
import static gim.logic.commands.CommandTestUtil.VALID_WEIGHT_BOB;
import static gim.logic.commands.CommandTestUtil.WEIGHT_DESC_AMY;
import static gim.logic.commands.CommandTestUtil.WEIGHT_DESC_BOB;
import static gim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static gim.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static gim.testutil.TypicalExercises.AMY;
import static gim.testutil.TypicalExercises.BOB;

import gim.logic.commands.AddCommand;
import gim.model.exercise.Exercise;
import gim.model.exercise.Name;
import gim.model.exercise.Rep;
import gim.model.exercise.Sets;
import gim.model.exercise.Weight;
import gim.model.tag.Date;
import gim.testutil.ExerciseBuilder;
import org.junit.jupiter.api.Test;




public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Exercise expectedExercise = new ExerciseBuilder(BOB).withDates(VALID_DATE).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB
                + REP_DESC_BOB + DATE_DESC, new AddCommand(expectedExercise));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB
                + REP_DESC_BOB + DATE_DESC, new AddCommand(expectedExercise));

        // multiple weights - last weight accepted
        assertParseSuccess(parser, NAME_DESC_BOB + WEIGHT_DESC_AMY + WEIGHT_DESC_BOB + SETS_DESC_BOB
                + REP_DESC_BOB + DATE_DESC, new AddCommand(expectedExercise));

        // multiple setss - last sets accepted
        assertParseSuccess(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_AMY + SETS_DESC_BOB
                + REP_DESC_BOB + DATE_DESC, new AddCommand(expectedExercise));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB + REP_DESC_AMY
                + REP_DESC_BOB + DATE_DESC, new AddCommand(expectedExercise));

        // multiple tags - all accepted
        Exercise expectedExerciseMultipleTags = new ExerciseBuilder(BOB).withDates(VALID_DATE)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB + REP_DESC_BOB
                + DATE_DESC + DATE_DESC, new AddCommand(expectedExerciseMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Exercise expectedExercise = new ExerciseBuilder(AMY).withDates().build();
        assertParseSuccess(parser, NAME_DESC_AMY + WEIGHT_DESC_AMY + SETS_DESC_AMY + REP_DESC_AMY,
                new AddCommand(expectedExercise));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB + REP_DESC_BOB,
                expectedMessage);

        // missing weight prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_WEIGHT_BOB + SETS_DESC_BOB + REP_DESC_BOB,
                expectedMessage);

        // missing sets prefix
        assertParseFailure(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + VALID_SETS_BOB + REP_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB + VALID_REP_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_WEIGHT_BOB + VALID_SETS_BOB + VALID_REP_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + WEIGHT_DESC_BOB + SETS_DESC_BOB + REP_DESC_BOB
                + DATE_DESC + DATE_DESC, Name.MESSAGE_CONSTRAINTS);

        // invalid weight
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_WEIGHT_DESC + SETS_DESC_BOB + REP_DESC_BOB
                + DATE_DESC + DATE_DESC, Weight.MESSAGE_CONSTRAINTS);

        // invalid sets
        assertParseFailure(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + INVALID_SETS_DESC + REP_DESC_BOB
                + DATE_DESC + DATE_DESC, Sets.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB + INVALID_REP_DESC
                + DATE_DESC + DATE_DESC, Rep.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB + REP_DESC_BOB
                + INVALID_DATE_DESC + VALID_DATE, Date.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + WEIGHT_DESC_BOB + SETS_DESC_BOB + INVALID_REP_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + WEIGHT_DESC_BOB + SETS_DESC_BOB
                + REP_DESC_BOB + DATE_DESC + DATE_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
