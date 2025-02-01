package uk.ac.ncl.advancedjava.quizapi.questions;

/**
 * Question - defines the common functionality for different types of quiz questions.
 * Implementations of this interface can represent various question types such as free-response or multiple-choice.
 *
 * @author Filip Kovarik - S24039999
 */
public interface Question {

    /**
     * Retrieves the text of the question.
     *
     * @return the question text as a {@code String}
     */
    String getQuestionText();

    /**
     * Checks whether the provided answer is correct for the question.
     *
     * @param answer the answer to check as a {@code String}
     * @return {@code true} if the provided answer is correct, {@code false} otherwise
     */
    boolean isCorrectAnswer(String answer);

    /**
     * Retrieves the type of the question, represented by the {@link QuestionType} enum.
     *
     * @return the {@link QuestionType} of this question
     */
    QuestionType getType();
}
