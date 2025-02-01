package uk.ac.ncl.advancedjava.quizapi.questions;

/**
 * FreeResponseQuestion - represents a free-response question. It extends the {@link AbstractQuestion} class
 * and includes logic to check whether the student's answer matches the correct answer.
 *
 * @author Filip Kovarik - S24039999
 */
final class FreeResponseQuestion extends AbstractQuestion {

    private final String correctAnswer;
    private final QuestionType TYPE = QuestionType.FREE_RESPONSE;

    /**
     * Constructs a {@code FreeResponseQuestion} with the given question text and correct answer.
     *
     * @param questionText  the text of the question
     * @param correctAnswer the correct answer to the question
     * @throws IllegalArgumentException if the correct answer is null or empty
     */
    FreeResponseQuestion(String questionText, String correctAnswer) {
        super(questionText);
        if (correctAnswer == null || correctAnswer.isEmpty()) {
            throw new IllegalArgumentException("Correct answer cannot be null or empty");
        }
        this.correctAnswer = correctAnswer;
    }

    /**
     * Checks whether the given answer matches the correct answer. The comparison ignores case and extra whitespace,
     * normalizing both the student's answer and the correct answer for comparison.
     *
     * @param answer the student's answer to the question as a {@code String}
     * @return {@code true} if the student's answer matches the correct answer, {@code false} otherwise
     */
    @Override
    public boolean isCorrectAnswer(String answer) {
        if (answer == null) {
            return false;
        }
        String sanitizedCorrectAnswer = sanitizeAnswer(correctAnswer);
        String sanitizedStudentAnswer = sanitizeAnswer(answer);
        return sanitizedCorrectAnswer.equals(sanitizedStudentAnswer);
    }

    /**
     * Retrieves the type of this question, which is {@code FREE_RANGE}.
     *
     * @return the {@code QuestionType.FREE_RANGE}
     */
    @Override
    public QuestionType getType() {
        return TYPE;
    }

    /**
     * Sanitizes an answer by converting it to lowercase, trimming it, and replacing multiple spaces with
     * a single space.
     *
     * @param input the answer to sanitize
     * @return the sanitized version of the answer
     */
    private String sanitizeAnswer(String input) {
        return input.toLowerCase().trim().replaceAll("\\s+", " ");
    }

    /**
     * Compares this question to another object for equality. Two {@code FreeResponseQuestion} objects
     * are considered equal if they have the same question text and correct answer.
     *
     * @param o the object to compare to
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FreeResponseQuestion that)) return false;

        return getQuestionText().equals(that.getQuestionText()) && correctAnswer.equals(that.correctAnswer);
    }

    /**
     * Generates a hash code for this question based on the question text and correct answer.
     *
     * @return the hash code of this question
     */
    @Override
    public int hashCode() {
        int result = getQuestionText().hashCode();
        result = 31 * result + correctAnswer.hashCode();
        return result;
    }

    /**
     * Returns a string representation of the {@code FreeResponseQuestion}.
     *
     * @return a string representation of the question, including the question text
     */
    @Override
    public String toString() {
        return "FreeRangeQuestion{" +
                "questionText='" + questionText + '\'' +
                "}";
    }
}
