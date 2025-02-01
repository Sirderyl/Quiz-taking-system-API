package uk.ac.ncl.advancedjava.quizapi.questions;

import java.util.Map;

/**
 * OptionableQuestion - extends the {@link Question} interface and adds functionality for questions that have a set of
 * answer options. This is typically used for multiple-choice questions.
 */
public interface OptionableQuestion extends Question {

    /**
     * Retrieves the available answer options for the question.
     * The options are represented as a {@link Map}, where the key is a {@code Character} representing the option ID
     * (e.g., 'A', 'B', 'C'), and the value is the option text as a {@code String}.
     *
     * @return a {@link Map} of option IDs and corresponding option texts
     */
    Map<Character, String> getOptions();
}
