package gim.logic.commands;

import static gim.commons.util.CollectionUtil.requireAllNonNull;
import static gim.logic.parser.CliSyntax.PREFIX_LEVEL;

import java.util.ArrayList;

import gim.commons.core.index.Index;
import gim.logic.commands.exceptions.CommandException;
import gim.model.Model;

/**
 * Generates a sample workout based on existing PRs of the specified exercises,
 * according to the difficulty level specified.
 * Difficulty levels supported: {easy, medium, hard}
 */
public class GenerateCommand extends Command {

    public static final String COMMAND_WORD = ":gen";
    public static final String DIFFICULTY_LEVELS = "{easy, medium, hard}";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Generates a sample workout based on PRs (Personal Records) "
            + "of the exercise(s) identified by the indices number used in the displayed exercise list, "
            + "according to the difficulty level specified. " + "\n"
            + "Difficulty levels supported are: " + DIFFICULTY_LEVELS + "\n"
            + "Parameters: INDEX_1 [, INDEX_2, ...] (each index must be a positive integer) "
            + PREFIX_LEVEL + "LEVEL (must be one of " + DIFFICULTY_LEVELS + ") " + "\n"
            + "Example: " + COMMAND_WORD + " 2,3 " + PREFIX_LEVEL + "easy";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "Generate command not implemented yet";


    private final ArrayList<Index> indices;
    private final String level;


    /**
     * @param indices of the exercises in the filtered exercise list
     * @param level difficulty level of the workout generated
     */
    public GenerateCommand(ArrayList<Index> indices, String level) {
        requireAllNonNull(indices, level);
        this.indices = indices;
        this.level = level;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        indices.forEach(Index::getOneBased);
        throw new CommandException(indices + level + "\n" + MESSAGE_USAGE);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GenerateCommand)) {
            return false;
        }

        // state check
        GenerateCommand e = (GenerateCommand) other;
        return indices.equals(e.indices)
                && level.equals(e.level);
    }

    @Override
    public String toString() {
        return indices.toString() + " l/" + level;
    }
}

