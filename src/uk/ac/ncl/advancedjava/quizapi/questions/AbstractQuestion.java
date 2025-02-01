package uk.ac.ncl.advancedjava.quizapi.questions;

/**
 * AbstractQuestion - an abstract implementation of the {@link Question} interface. It provides the base functionality
 * for all questions by storing the question text and implementing the {@code getQuestionText()} method.
 * Specific question types should extend this class to add further functionality.
 *
 * @author Filip Kovarik - S24039999
 */
abstract class AbstractQuestion implements Question {

    final String questionText;

    /**
     * Constructs an {@code AbstractQuestion} with the given question text. Package-private to prevent instantiating
     * this class instead of the specific subclasses.
     *
     * @param questionText  the text of the question, must not be null or empty
     * @throws IllegalArgumentException if the question text is null or empty
     */
    AbstractQuestion(String questionText) {
        if (questionText == null || questionText.isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty");
        }
        this.questionText = questionText;
    }

    /**
     * Retrieves the text of the question.
     *
     * @return the text of the question
     */
    @Override
    public String getQuestionText() {
        return questionText;
    }
}
