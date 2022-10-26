package gim.logic.generators;

import gim.model.exercise.Exercise;

/**
 * Generator for easy workout session.
 */
public class HardGenerator implements Generator {
    private Exercise exercisePR;

    public HardGenerator(Exercise exercisePR) {
        this.exercisePR = exercisePR;
    }

    @Override
    public String suggest() {
        String setsAndReps = "3sets x 8reps";
        double factor = 0.8;
        double suggestedWeight = factor * Double.parseDouble(exercisePR.getWeight().value);
        return String.format("%s: %.2fkg %s", exercisePR.getName(), suggestedWeight, setsAndReps);
    }
}
