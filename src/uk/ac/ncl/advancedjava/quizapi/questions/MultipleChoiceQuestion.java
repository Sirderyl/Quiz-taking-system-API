package uk.ac.ncl.advancedjava.quizapi.questions;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MultipleChoiceQuestion - represents a multiple-choice question with a set of options and correct answers.
 * This class extends the {@link AbstractQuestion} class and implements the {@link OptionableQuestion} interface,
 * which allows the retrieval of multiple-choice options.
 *
 * @author Filip Kovarik - S24039999
 */
final class MultipleChoiceQuestion extends AbstractQuestion implements OptionableQuestion {

    private final Map<Character, String> options; // Option (a,b,c,d) -> Option text
    private final Set<Character> correctOptions;
    private final QuestionType TYPE = QuestionType.MULTIPLE_CHOICE;

    /**
     * Constructs a {@code MultipleChoiceQuestion} with the given question text, options, and correct answers.
     * It is package private to prevent users from instantiating this class directly instead of using
     * {@link QuestionFactory}.
     *
     * @param questionText      the text of the question
     * @param options           a map of option characters (e.g. 'A', 'B', 'C') to their corresponding option text
     * @param correctOptions    a set of characters representing the correct options
     * @throws IllegalArgumentException if the options or correct options are null or empty, if the number of options
     *                                  is not between 2 and 4, or if the number of correct options exceeds the number
     *                                  of available options
     */
    MultipleChoiceQuestion(String questionText, Map<Character, String> options, Set<Character> correctOptions) {
        super(questionText);

        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("Options cannot be null or empty");
        } else if (correctOptions == null || correctOptions.isEmpty()) {
            throw new IllegalArgumentException("Correct options cannot be null or empty");
        } else if (options.size() < 2 || options.size() > 4) {
            throw new IllegalArgumentException("Number of options must be between 2 and 4");
        } else if (correctOptions.size() > options.size()) {
            throw new IllegalArgumentException("Number of correct options exceeds the number of available options");
        }

        this.options = options;
        this.correctOptions = correctOptions.stream()
                .map(Character::toLowerCase)
                .collect(Collectors.toSet());
    }

    /**
     * Checks whether the given answer is correct. The answer should be a comma-separated string of option characters
     * (e.g. "a,b"). The method compares the provided answer to the correct options.
     *
     * @param answer a comma-separated string of option characters representing the student's answer
     * @return {@code true} if the student's answer matches the correct options, {@code false} otherwise
     */
    @Override
    public boolean isCorrectAnswer(String answer) {
        if (answer == null || answer.isEmpty()) {
            return false;
        }
        String[] studentInputArray = (answer).toLowerCase().split(",");

        Set<Character> studentAnswers = Arrays.stream(studentInputArray)
                .map(String::trim)
                .map(s -> s.charAt(0))
                .collect(Collectors.toSet());

        return correctOptions.equals(studentAnswers);
    }

    /**
     * Retrieves the options for this multiple-choice question.
     *
     * @return a map of option characters to their corresponding text
     */
    @Override
    public Map<Character, String> getOptions() {
        return options;
    }

    /**
     * Retrieves the type of this question, which is {@code MULTIPLE_CHOICE}
     *
     * @return the {@code QuestionType.MULTIPLE_CHOICE}
     */
    @Override
    public QuestionType getType() {
        return TYPE;
    }

    /**
     * Compares this question to another object for equality. Two {@code MultipleChoiceQuestion} objects are considered
     * equal if they have the same question text, options, and correct answers.
     *
     * @param o the object to compare to
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultipleChoiceQuestion that)) return false;

        return getQuestionText().equals(that.getQuestionText()) && getOptions().equals(that.getOptions()) &&
                correctOptions.equals(that.correctOptions);
    }

    /**
     * Generates a hash code for this question based on the question text, options, and correct answers.
     *
     * @return the hash code of this question
     */
    @Override
    public int hashCode() {
        int result = getQuestionText().hashCode();
        result = 31 * result * getOptions().hashCode();
        result = 31 * result + correctOptions.hashCode();
        return result;
    }

    /**
     * Returns a string representation of the {@code MultipleChoiceQuestion}.
     *
     * @return a string representation of the question, including the question text and options
     */
    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "questionText='" + questionText + '\'' +
                ", options=" + options +
                "}";
    }
}
